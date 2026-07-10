package com.muse.server.after.service;

import com.muse.server.after.dto.project.ProjectRequest;
import com.muse.server.after.dto.project.ProjectResponse;
import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.entity.ProjectMemberEntity;
import com.muse.server.after.entity.UserEntity;
import com.muse.server.after.repository.ProjectMemberRepository;
import com.muse.server.after.repository.ProjectRepository;
import com.muse.server.after.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private MinioService minioService;

    @Transactional
    public ProjectResponse create(Long userId, ProjectRequest request) {
        final UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final ProjectEntity project = new ProjectEntity();
        project.setOwner(owner);
        project.setTitle(request.getTitle());
        project.setCreator(request.getCreator());
        project.setPublished(false);
        project.setFilePath(null);

        final ProjectMemberEntity ownerMember = new ProjectMemberEntity();
        ownerMember.setProject(project);
        ownerMember.setUser(owner);
        project.getCollaborators().add(ownerMember);

        if (request.getCollaboratorsIds() != null && !request.getCollaboratorsIds().isEmpty()) {
            List<UserEntity> collaborators = userRepository.findAllById(request.getCollaboratorsIds());

            if (collaborators.size() != request.getCollaboratorsIds().size()) {
                throw new RuntimeException("One or more collaborators not found");
            }

            collaborators.forEach(user -> {
                final ProjectMemberEntity memberEntity = new ProjectMemberEntity();
                memberEntity.setProject(project);
                memberEntity.setUser(user);

                project.getCollaborators().add(memberEntity);
            });
        }

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
        return projectRepository.findAllByOwnerIdOrCollaboratorUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProjectResponse toResponse(ProjectEntity entity) {
        final ProjectResponse response = new ProjectResponse();

        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setComposer(entity.getCreator());
        response.setPublic(entity.getPublished());
        response.setFilePath(entity.getFilePath());
        response.setCoverPath(entity.getCoverPath());

        return response;
    }
}
