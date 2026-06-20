package com.muse.server.before.controller;

import com.muse.server.before.model.detail.CustomUserDetails;
import com.muse.server.before.model.dto.project.ProjectRequest;
import com.muse.server.before.model.dto.project.ProjectResponse;
import com.muse.server.before.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.create(user.getId(), request));
    }

    @PostMapping("/{projectId}/publish")
    public ResponseEntity<ProjectResponse> publish(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(projectService.publish(user.getId(), projectId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProjectResponse>> myProjects(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(projectService.getUserProjects(user.getId()));
    }
}
