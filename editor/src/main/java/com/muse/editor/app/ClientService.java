package com.muse.editor.app;

import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.UserManager;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

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
                return InvitationMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                InvitationMessage invitationMessage = (InvitationMessage) payload;

                Platform.runLater(() -> {
                    System.out.println("Notification received: " + invitationMessage.content());
                });
            }
        });
    }

    public void sendInvitation(String targetUser) {
        if (stompSession == null || !stompSession.isConnected()) {
            Debug.fail("Cannot send invitation - STOMP session is offline");
            return;
        }

        final InvitationMessage invitationMessage = new InvitationMessage(
                "INVITE",
                UserManager.getInstance().currentUserProperty().get().getUsername(),
                "Come on in");

        stompSession.send("/app/invite/" + targetUser, invitationMessage);
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }
}
