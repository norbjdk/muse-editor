package com.muse.editor.app;

import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.UserManager;
import javafx.application.Platform;
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

    private ClientService() {}

    private final String server = "ws://localhost:8080/ws";
    private StompSession stompSession;
    private String username;
    private String token;

    public void connect() {
        token = TokenStorage.getToken();

        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient client = new WebSocketStompClient(webSocketClient);

        client.setMessageConverter(new MappingJackson2MessageConverter());

        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + token);

        client.connectAsync(server, new WebSocketHttpHeaders(), headers, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                System.out.println("Connected to the server");
                subscribeToNotifications();
            }

            @Override
            public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("No connection to the server");
                exception.printStackTrace();
            }
        });
    }

    private void subscribeToNotifications() {
        stompSession.subscribe("/user/queue/notifications", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return InvitationMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                InvitationMessage invitationMessage = (InvitationMessage) payload;

                Platform.runLater(() -> {
                    System.out.println("Notif: " + invitationMessage.content());
                });
            }
        });
    }

    public void sendInvitation(String targetUser) {
        final InvitationMessage invitationMessage = new InvitationMessage(
                "INVITE",
                UserManager.getInstance().currentUserProperty().get().getUsername(),
                "Come on in");

        stompSession.send("/app/invite/" + targetUser, invitationMessage);
    }
}

