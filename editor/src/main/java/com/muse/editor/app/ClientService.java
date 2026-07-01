package com.muse.editor.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.core.model.message.InvitationResponse;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.UserManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.CollaboratorAnsweredEvent;
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
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private ClientService() {}

    public void connect() {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient client = new WebSocketStompClient(webSocketClient);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        converter.setObjectMapper(objectMapper);
        client.setMessageConverter(converter);

        client.setMessageConverter(new MappingJackson2MessageConverter());

        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + TokenStorage.getToken());

        client.connectAsync(AppConfig.websocketUrlProperty().get(), new WebSocketHttpHeaders(), headers, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                Debug.pass("Connected to the WebSocket server");

                Platform.runLater(() -> status.set(Status.CONNECTED));

                subscribeToNotifications();
            }

            @Override
            public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                Debug.fail("Connection exception or lost link to the server");

                Platform.runLater(() -> status.set(Status.DISCONNECTED));

                exception.printStackTrace();
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                Debug.fail("Transport error encountered");
                Platform.runLater(() -> status.set(Status.DISCONNECTED));
            }
        });
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
                    byte[] bytes = (byte[]) payload;
                    String json = new String(bytes, StandardCharsets.UTF_8);

                    System.out.println("Raw JSON: " + json);

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    InvitationMessage message = mapper.readValue(json, InvitationMessage.class);

                    Platform.runLater(() -> {
                        System.out.println("Invitation from: " + message.getUsername());
                        System.out.println("Content: " + message.getContent());
                        showInvitationDialog(message);

                    });

                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
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
                    Map<String, Object> data = (Map<String, Object>) payload;
                    String responder = (String) data.get("responder");
                    boolean accepted = (boolean) data.get("accepted");

                    Platform.runLater(() -> {
                        if (accepted) {
                            System.out.println(responder + " ACCEPTED your invitation!");
                        } else {
                            System.out.println(responder + " DECLINED your invitation.");
                        }
//                        showResponseNotification(responder, accepted);
                        final InvitationResponse response = new InvitationResponse();

                        response.setAccepted(accepted);
                        response.setFrom("me");
                        response.setResponder(responder);

                        EventBus.getInstance().publish(new CollaboratorAnsweredEvent(response));
                    });

                } catch (Exception e) {
                    System.err.println("Error processing response: " + e.getMessage());
                }
            }
        });
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

        Button accept = new Button("Accept");
        accept.getStyleClass().add("accept-button");

        Button decline = new Button("Decline");
        decline.getStyleClass().add("decline-button");

        accept.setPrefWidth(110);
        decline.setPrefWidth(110);

        accept.setOnAction(e -> {
            ClientService.getInstance().sendInvitationResponse(
                    invitationMessage.getUsername(), true);
            dialog.close();
        });

        decline.setOnAction(e -> {
            ClientService.getInstance().sendInvitationResponse(
                    invitationMessage.getUsername(), false);
            dialog.close();
        });

        HBox buttons = new HBox(15, accept, decline);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(18,
                title,
                new Separator(),
                from,
                message,
                buttons
        );

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
