package com.muse.editor.app;

import com.muse.editor.core.cloud.ProjectEditMessage;
import com.muse.editor.core.user.TokenStorage;
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

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

            }

            @Override
            public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("No connection to the server");
                exception.printStackTrace();
            }
        });
    }

    private void subscribeNotifications() {

    }

    public void sendTextChange(String projectId, String textChange, int cursor) {
//        ProjectEditMessage change = new ProjectEditMessage(projectId, username, textChange, cursor);
//        stompSession.send("/app/project/f");
    }
}

