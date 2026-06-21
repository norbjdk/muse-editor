package com.muse.server.after.service;

import com.muse.server.after.dto.project.ProjectRequest;
import com.muse.server.after.dto.project.ProjectResponse;
import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.entity.UserEntity;
import com.muse.server.after.repository.ProjectRepository;
import com.muse.server.after.repository.UserRepository;
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
        project.setCreator(request.getCreator());
        project.setPublished(false);

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

        project.setPublished(true);
        final ProjectEntity saved = projectRepository.save(project);

        return toResponse(saved);
    }

    public List<ProjectResponse> getUserProjects(Long userId) {
        return projectRepository.findByOwnerId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProjectResponse toResponse(ProjectEntity entity) {
        final ProjectResponse response = new ProjectResponse();

        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setCreator(entity.getCreator());
        response.setPublic(entity.getPublished());
        response.setFilePath(entity.getFilePath());

        return response;
    }
}
