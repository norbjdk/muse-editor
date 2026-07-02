package com.muse.server.after.listener;

import com.muse.server.after.repository.UserRepository;
import com.muse.server.after.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    @Autowired
    private CollabService collabSessionService;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, String> activeUsers = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getUser() != null) {
            String username = headerAccessor.getUser().getName();
            String sessionId = headerAccessor.getSessionId();

            activeUsers.put(sessionId, username);
            System.out.println("User [" + username + "] connected through jwt");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String username = activeUsers.remove(sessionId);

        if (username != null) {
            System.out.println("User [" + username + "] disconnected");
        }

        if (headerAccessor.getSessionAttributes() != null) {
            Long collabSessionId = (Long) headerAccessor.getSessionAttributes().get("collabSessionId");

            if (collabSessionId != null && username != null) {
                userRepository.findByUsername(username).ifPresent(user ->
                        collabSessionService.leave(collabSessionId, user.getId())
                );
            }
        }
    }
}
