package com.muse.server.after.controller;

import com.muse.server.after.detail.CustomUserDetails;
import com.muse.server.after.dto.session.SessionResponse;
import com.muse.server.after.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private CollabService collabSessionService;

    @PostMapping("/projects/{projectId}/join")
    public ResponseEntity<SessionResponse> join(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(collabSessionService.joinOrCreate(projectId, user.getId()));
    }

    @PostMapping("/{sessionId}/leave")
    public ResponseEntity<Void> leave(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long sessionId
    ) {
        collabSessionService.leave(sessionId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/projects/{projectId}/active")
    public ResponseEntity<SessionResponse> getActive(@PathVariable Long projectId) {
        final SessionResponse response = collabSessionService.getActiveSessionForProject(projectId);

        if (response == null) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(response);
    }
}
