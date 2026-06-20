package com.muse.server.before.service;

import com.muse.server.before.model.dto.project.ProjectRequest;
import com.muse.server.before.model.dto.project.ProjectResponse;
import com.muse.server.before.model.entity.ProjectEntity;
import com.muse.server.before.model.entity.UserEntity;
import com.muse.server.before.model.repository.ProjectRepository;
import com.muse.server.before.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MinioService minioService;

    public ProjectResponse create(Long userId, ProjectRequest request) {
        final UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final ProjectEntity project = new ProjectEntity();
        project.setOwner(owner);
        project.setTitle(request.getTitle());
        project.setComposer(request.getCreator());
        project.setPublic(false);

        final ProjectEntity saved = projectRepository.save(project);

        minioService.createProjectFolders(userId, saved.getId());

        return toResponse(saved);
    }

    public ProjectResponse publish(Long userId, Long projectId) {
        final ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getOwner().getId().equals(userId))
            throw new RuntimeException("Only owner can publish");

        minioService.copySharedToPublished(userId, projectId);

        project.setPublic(true);
        final ProjectEntity saved = projectRepository.save(project);

        return toResponse(saved);
    }

    public List<ProjectResponse> getUserProjects(Long userId) {
        return projectRepository.findByOwnerId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProjectResponse toResponse(ProjectEntity e) {
        final ProjectResponse response = new ProjectResponse();
        response.setId(e.getId());
        response.setTitle(e.getTitle());
        response.setComposer(e.getComposer());
        response.setPublic(e.getPublic());
        response.setCloudFilePath(e.getCloudFilePath());
        return response;
    }
}
