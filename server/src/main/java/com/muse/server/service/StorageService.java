package com.muse.server.service;

import com.muse.server.model.entity.ProjectEntity;
import com.muse.server.model.repository.ProjectRepository;
import com.muse.server.security.ProjectAccessGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    @Autowired
    private MinioService minioService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectAccessGuard accessGuard;

    public String uploadAvatar(Long userId, MultipartFile file) {
        return minioService.uploadAvatar(userId, file);
    }

    public String uploadProjectFile(Long userId, Long projectId, MultipartFile file) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId))
            throw new RuntimeException("Only owner can upload project file");

        final String url = minioService.uploadProjectFile(userId, projectId, file);

        project.setCloudFilePath(url);
        projectRepository.save(project);

        return url;
    }

    public String uploadProjectCover(Long userId, Long projectId, MultipartFile file) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId))
            throw new RuntimeException("Only owner can upload project cover");

        final String url = minioService.uploadProjectCover(userId, projectId, file);

        project.setCloudCoverUrl(url);
        projectRepository.save(project);

        return url;
    }

    public String getProjectFileUrl(Long userId, Long projectId) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.hasReadAccess(project, projectId))
            throw new RuntimeException("Access denied");

        return minioService.extractProjectFileUrl(project.getOwner().getId(), projectId);
    }

    private ProjectEntity findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
