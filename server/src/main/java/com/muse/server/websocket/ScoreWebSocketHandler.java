package com.muse.server.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.server.service.CollabSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.muse.server.websocket.ScoreMessage.Op.*;

@Component
public class ScoreWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, List<WebSocketSession>> sessionRooms = new ConcurrentHashMap<>();

    @Autowired
    private CollabSessionService collabSessionService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void handleTextMessage(WebSocketSession ws, TextMessage message) throws Exception {
        ScoreMessage msg = objectMapper.readValue(message.getPayload(), ScoreMessage.class);

        Long userId = (Long) ws.getAttributes().get("userId");

        switch (msg.getOp()) {
            case JOIN_SESSION  -> handleJoin(ws, msg, userId);
            case LEAVE_SESSION -> handleLeave(ws, msg);
            case REPLACE_NOTE  -> handleReplaceNote(ws, msg);
        }
    }

    private void handleJoin(WebSocketSession ws, ScoreMessage msg, Long userId) throws Exception {
        Long sessionId = collabSessionService.getOrCreateSession(msg.getProjectId(), userId);

        ws.getAttributes().put("sessionId", sessionId);

        sessionRooms.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(ws);

        ScoreMessage ack = new ScoreMessage();
        ack.setOp(JOIN_SESSION);
        ack.setSessionId(sessionId);
        ws.sendMessage(new TextMessage(objectMapper.writeValueAsString(ack)));
    }

    private void handleLeave(WebSocketSession ws, ScoreMessage msg) {
        Long sessionId = (Long) ws.getAttributes().get("sessionId");
        if (sessionId != null) {
            List<WebSocketSession> room = sessionRooms.get(sessionId);
            if (room != null) room.remove(ws);
        }
    }

    private void handleReplaceNote(WebSocketSession ws, ScoreMessage msg) throws Exception {
        Long sessionId = (Long) ws.getAttributes().get("sessionId");
        if (sessionId == null) return;

        String payload = objectMapper.writeValueAsString(msg);
        List<WebSocketSession> room = sessionRooms.getOrDefault(sessionId, List.of());

        for (WebSocketSession other : room) {
            if (other.isOpen() && !other.getId().equals(ws.getId())) {
                other.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession ws, CloseStatus status) {
        Long sessionId = (Long) ws.getAttributes().get("sessionId");
        if (sessionId != null) {
            List<WebSocketSession> room = sessionRooms.get(sessionId);
            if (room != null) room.remove(ws);
        }
    }
}