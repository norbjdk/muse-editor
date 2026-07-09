package com.muse.server.after.controller;

import com.muse.server.after.detail.CustomUserDetails;
import com.muse.server.after.dto.msg.ScoreUpdatedMessage;
import com.muse.server.after.dto.session.SessionResponse;
import com.muse.server.after.dto.user.UserResponse;
import com.muse.server.after.entity.CollabSessionEntity;
import com.muse.server.after.repository.CollabSessionRepository;
import com.muse.server.after.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private CollabService collabService;

    @Autowired
    private CollabSessionRepository collabSessionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/projects/{projectId}/join")
    public ResponseEntity<SessionResponse> join(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(collabService.joinOrCreate(projectId, user.getId()));
    }

    @PostMapping("/{sessionId}/leave")
    public ResponseEntity<Void> leave(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long sessionId
    ) {
        collabService.leave(sessionId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/projects/{projectId}/active")
    public ResponseEntity<SessionResponse> getActive(@PathVariable Long projectId) {
        final SessionResponse response = collabService.getActiveSessionForProject(projectId);
        if (response == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{projectId}/collaborators")
    public ResponseEntity<List<UserResponse>> getActiveCollaborators(@PathVariable Long projectId) {
        final List<UserResponse> responses = collabService.getActiveCollaboratorsForProject(projectId);

        if (responses == null || responses.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{sessionId}/broadcast")
    public ResponseEntity<Void> broadcast(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long sessionId
    ) {
        final CollabSessionEntity session = collabSessionRepository
                .findById(sessionId)
                .orElse(null);

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        messagingTemplate.convertAndSend(
                "/topic/session." + sessionId,
                new ScoreUpdatedMessage(sessionId, user.getUsername())
        );

        return ResponseEntity.ok().build();
    }
}
