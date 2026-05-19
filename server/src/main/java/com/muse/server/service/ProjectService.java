package com.muse.server.service;

import com.muse.server.model.dto.ProjectRequest;
import com.muse.server.model.dto.ProjectResponse;
import com.muse.server.model.entity.ProjectEntity;
import com.muse.server.model.entity.UserEntity;
import com.muse.server.model.repository.ProjectRepository;
import com.muse.server.model.repository.UserRepository;
import com.muse.server.security.ProjectAccessGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectAccessGuard accessGuard;

    public ProjectResponse createProject(Long userId, ProjectRequest request) {
        UserEntity owner = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectEntity project = new ProjectEntity();
        project.setOwner(owner);
        project.setTitle(request.getTitle());
        project.setSubtitle(request.getSubtitle());
        project.setComposer(request.getComposer());
        project.setPublic(request.getIsPublic());

        return toResponse(projectRepo.save(project));
    }

    public ProjectResponse getProjectById(Long projectId, Long userId) {
        ProjectEntity project = findById(projectId);

        if (!accessGuard.hasReadAccess(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(project);
    }

    public ProjectResponse updateProject(Long projectId, Long userId, ProjectRequest request) {
        ProjectEntity project = findById(projectId);

        if (!accessGuard.hasEditAccess(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        project.setTitle(request.getTitle());
        project.setSubtitle(request.getSubtitle());
        project.setComposer(request.getComposer());
        project.setPublic(request.getIsPublic());
        project.setUpdatedAt(LocalDateTime.now());

        return toResponse(projectRepo.save(project));
    }

    public void deleteProject(Long projectId, Long userId) {
        ProjectEntity project = findById(projectId);

        if (!accessGuard.isOwner(project, userId)) {
            throw new RuntimeException("Only owner can delete the project");
        }

        projectRepo.delete(project);
    }

    public List<ProjectResponse> getUserProjects(Long userId) {
        return projectRepo.findByOwnerId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getPublicProjects() {
        return projectRepo.findByIsPublicTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── helpers ──────────────────────────────────────────

    public ProjectEntity findById(Long projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    private ProjectResponse toResponse(ProjectEntity project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setOwnerId(project.getOwner().getId());
        response.setOwnerUsername(project.getOwner().getUsername());
        response.setTitle(project.getTitle());
        response.setSubtitle(project.getSubtitle());
        response.setComposer(project.getComposer());
        response.setCloudCoverUrl(project.getCloudCoverUrl());
        response.setIsPublic(project.getPublic());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}