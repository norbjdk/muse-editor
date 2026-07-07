package com.muse.editor.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.io.MXMLParser;
import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.core.model.message.InvitationResponse;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.UserManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.CollaboratorAnsweredEvent;
import com.muse.editor.event.project.ScoreReloadedEvent;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ClientService {
    private static final ClientService instance = new ClientService();

    public static ClientService getInstance() {
        return instance;
    }

    public enum Status {
        CONNECTED,
        DISCONNECTED
    }

    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.DISCONNECTED);
    private StompSession stompSession;
    private Long currentSessionId;

    private ClientService() {}

    public Long getCurrentSessionId() {
        return currentSessionId;
    }

    public void joinSession(Long projectId, Runnable onJoined) {
        CompletableFuture.runAsync(() -> {
            try {
                final Request request = new Request.Builder()
                        .url(AppConfig.serverUrlProperty().get() + "/api/v1/sessions/projects/" + projectId + "/join")
                        .post(RequestBody.create("{}", MediaType.get("application/json")))
                        .build();

                try (Response response = ApiConfig.getClient().newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        final ObjectMapper mapper = ApiConfig.getObjectMapper();
                        final Map<?, ?> body = mapper.readValue(response.body().string(), Map.class);

                        final Long sessionId = ((Number) body.get("sessionId")).longValue();
                        currentSessionId = sessionId;

                        Debug.pass("Joined session: " + sessionId);

                        Platform.runLater(() -> {
                            reconnectWithSession(sessionId);
                            if (onJoined != null) onJoined.run();
                        });
                    } else {
                        Debug.fail("Join session failed: HTTP " + (response != null ? response.code() : "null"));
                    }
                }
            } catch (Exception e) {
                Debug.fail("joinSession error: " + e.getMessage());
            }
        });
    }

    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            stompSession = null;
        }
        currentSessionId = null;
        Platform.runLater(() -> status.set(Status.DISCONNECTED));
    }

    public void leaveSession() {
        if (currentSessionId == null) return;

        final Long sessionId = currentSessionId;
        currentSessionId = null;

        CompletableFuture.runAsync(() -> {
            try {
                final Request request = new Request.Builder()
                        .url(AppConfig.serverUrlProperty().get() + "/api/v1/sessions/" + sessionId + "/leave")
                        .post(RequestBody.create("{}", MediaType.get("application/json")))
                        .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                        .build();

                ApiConfig.getClient().newCall(request).execute();
                Debug.pass("Left session: " + sessionId);
            } catch (Exception e) {
                Debug.fail("leaveSession error: " + e.getMessage());
            }
        });
    }

    public void connect() {
        connectInternal(null);
    }

    private void reconnectWithSession(Long sessionId) {
        if (stompSession != null && stompSession.isConnected())
            stompSession.disconnect();
        connectInternal(sessionId);
    }

    private void connectInternal(@Nullable Long sessionId) {
        final StandardWebSocketClient webSocketClient      = new StandardWebSocketClient();
        final WebSocketStompClient    webSocketStompClient = new WebSocketStompClient(webSocketClient);

        final MappingJackson2MessageConverter converter    = new MappingJackson2MessageConverter();
        final ObjectMapper                    objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(objectMapper);
        webSocketStompClient.setMessageConverter(converter);

        final StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + TokenStorage.getToken());
        if (sessionId != null)
            headers.add("Collab-Session-Id", String.valueOf(sessionId));

        webSocketStompClient.connectAsync(
                AppConfig.websocketUrlProperty().get(),
                new WebSocketHttpHeaders(),
                headers,
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        stompSession = session;
                        Debug.pass("Connected to WS server" + (sessionId != null ? " (session " + sessionId + ")" : ""));
                        Platform.runLater(() -> status.set(Status.CONNECTED));

                        subscribeToNotifications();

                        if (sessionId != null)
                            subscribeToSession(sessionId);
                    }

                    @Override
                    public void handleException(StompSession session, @Nullable StompCommand command,
                                                StompHeaders headers, byte[] payload, Throwable exception) {
                        Debug.fail("WS exception: " + exception.getMessage());
                        Platform.runLater(() -> status.set(Status.DISCONNECTED));
                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        Debug.fail("WS transport error: " + exception.getMessage());
                        Platform.runLater(() -> status.set(Status.DISCONNECTED));
                    }
                }
        );
    }

    private void subscribeToSession(Long sessionId) {
        if (stompSession == null || !stompSession.isConnected()) return;

        stompSession.subscribe("/topic/session." + sessionId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, @Nullable Object payload) {
                try {
                    final Map<String, Object> data = (Map<String, Object>) payload;
                    final String type = (String) data.get("type");
                    final String by   = (String) data.get("by");

                    final String me = UserManager.getInstance().currentUserProperty().get().getUsername();
                    if (me != null && me.equals(by)) return;

                    Platform.runLater(() -> {
                        if ("SCORE_UPDATED".equals(type)) {
                            Debug.check("Remote score update from: " + by);
                            handleRemoteScoreUpdate();
                        } else {
                            Debug.check("Session event [" + type + "] from: " + by);
                        }
                    });
                } catch (Exception e) {
                    Debug.fail("Session frame error: " + e.getMessage());
                }
            }
        });

        Debug.pass("Subscribed to session: " + sessionId);
    }

    private void subscribeToNotifications() {
        if (stompSession == null || !stompSession.isConnected()) return;

        stompSession.subscribe("/user/queue/notifications", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    final String json    = new String((byte[]) payload, StandardCharsets.UTF_8);
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    final InvitationMessage message = mapper.readValue(json, InvitationMessage.class);

                    Platform.runLater(() -> showInvitationDialog(message));
                } catch (Exception e) {
                    System.err.println("Notification error: " + e.getMessage());
                }
            }
        });

        stompSession.subscribe("/user/queue/invitation-responses", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    final Map<String, Object> data       = (Map<String, Object>) payload;
                    final String              responder   = (String)  data.get("responder");
                    final Number              responderId = (Number)  data.get("responderId");
                    final boolean             accepted    = (boolean) data.get("accepted");

                    Platform.runLater(() -> {
                        final InvitationResponse response = new InvitationResponse();
                        response.setAccepted(accepted);
                        response.setFrom("me");
                        response.setResponder(responder);
                        response.setResponderId(responderId.longValue());
                        EventBus.getInstance().publish(new CollaboratorAnsweredEvent(response));
                    });
                } catch (Exception e) {
                    System.err.println("Invitation response error: " + e.getMessage());
                }
            }
        });
    }

    private void handleRemoteScoreUpdate() {
        final Long projectId = getActiveProjectServerId();
        if (projectId == null) {
            Debug.fail("handleRemoteScoreUpdate: no active project serverId");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                final Request urlRequest = new Request.Builder()
                        .url(AppConfig.serverUrlProperty().get()
                                + "/api/v1/storage/projects/" + projectId + "/shared/get")
                        .get()
                        .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                        .build();

                final String fileUrl;
                try (Response urlResponse = ApiConfig.getClient().newCall(urlRequest).execute()) {
                    if (!urlResponse.isSuccessful() || urlResponse.body() == null) {
                        Debug.fail("Failed to get shared file URL: " + urlResponse.code());
                        return;
                    }
                    final Map<?, ?> body = ApiConfig.getObjectMapper()
                            .readValue(urlResponse.body().string(), Map.class);
                    fileUrl = (String) body.get("url");
                }

                final Request fileRequest = new Request.Builder()
                        .url(fileUrl)
                        .get()
                        .build();

                final File temp;
                try (Response fileResponse = ApiConfig.getClient().newCall(fileRequest).execute()) {
                    if (!fileResponse.isSuccessful() || fileResponse.body() == null) {
                        Debug.fail("Failed to download shared file: " + fileResponse.code());
                        return;
                    }
                    temp = Files.createTempFile("muse_sync_", ".musicxml").toFile();
                    try (FileOutputStream fos = new FileOutputStream(temp)) {
                        fos.write(fileResponse.body().bytes());
                    }
                }

                final ScorePartwise score = MXMLParser.parse(temp.toPath());
                temp.delete();

                Platform.runLater(() -> {
                    ScoreManager.getInstance().assignScore(score);
                    EventBus.getInstance().publish(new ScoreReloadedEvent("remote"));
                    Debug.pass("Remote score applied and UI rerender triggered");
                });

            } catch (Exception e) {
                Debug.fail("Remote score update error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private Long getActiveProjectServerId() {
        final var project = ProjectManager.getInstance().currentProjectProperty().get();
        return project != null ? project.getServerId() : null;
    }

    public void sendInvitation(String targetUser) {
        if (stompSession == null || !stompSession.isConnected()) {
            Debug.fail("Cannot send invitation - STOMP session is offline");
            return;
        }

        final Map<String, String> payload = new HashMap<>();
        payload.put("type", "INVITE");
        payload.put("from", UserManager.getInstance().currentUserProperty().get().getUsername());
        payload.put("content", "Come on in");

        stompSession.send("/app/invite/" + targetUser, payload);
        Debug.check("Invited " + targetUser + " as collaborator");
    }

    public void sendInvitationResponse(String fromUser, boolean accepted) {
        if (stompSession == null || !stompSession.isConnected()) {
            Debug.fail("Cannot send response - STOMP session is offline");
            return;
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("responder", UserManager.getInstance().currentUserProperty().get().getUsername());
        response.put("responderId", UserManager.getInstance().currentUserProperty().get().getId());
        response.put("from", fromUser);
        response.put("accepted", accepted);

        stompSession.send("/app/invite/response/" + fromUser, response);
        Debug.check("Sent invitation response to " + fromUser + ": " + (accepted ? "ACCEPTED" : "DECLINED"));
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    private void showInvitationDialog(InvitationMessage invitationMessage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        Label title = new Label("Collaboration Invitation");
        title.getStyleClass().add("dialog-title");

        Label from = new Label("From: " + invitationMessage.getUsername());
        from.getStyleClass().add("dialog-from");

        Label message = new Label("Let's create beautiful music together...");
        message.setWrapText(true);
        message.getStyleClass().add("dialog-message");

        Button accept  = new Button("Accept");
        Button decline = new Button("Decline");
        accept.getStyleClass().add("accept-button");
        decline.getStyleClass().add("decline-button");
        accept.setPrefWidth(110);
        decline.setPrefWidth(110);

        accept.setOnAction(e -> {
            ClientService.getInstance().sendInvitationResponse(invitationMessage.getUsername(), true);
            dialog.close();
        });
        decline.setOnAction(e -> {
            ClientService.getInstance().sendInvitationResponse(invitationMessage.getUsername(), false);
            dialog.close();
        });

        HBox buttons = new HBox(15, accept, decline);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, title, new Separator(), from, message, buttons);
        content.setPadding(new Insets(25));
        content.getStyleClass().add("invitation-dialog");
        content.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));

        Scene scene = new Scene(new StackPane(content));
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/com/muse/editor/styles/dialogs.css")
                ).toExternalForm()
        );

        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
