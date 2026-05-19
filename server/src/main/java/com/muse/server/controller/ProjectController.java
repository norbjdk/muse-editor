package com.muse.server.controller;

import com.muse.server.model.detail.CustomUserDetails;
import com.muse.server.model.dto.ProjectRequest;
import com.muse.server.model.dto.ProjectResponse;
import com.muse.server.service.ProjectService;
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
            @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(user.getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, user.getId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        projectService.deleteProject(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(projectService.getUserProjects(user.getId()));
    }

    @GetMapping("/public")
    public ResponseEntity<List<ProjectResponse>> getPublicProjects() {
        return ResponseEntity.ok(projectService.getPublicProjects());
    }
}