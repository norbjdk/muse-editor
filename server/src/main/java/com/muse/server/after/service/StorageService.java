package com.muse.server.after.service;

import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.repository.ProjectRepository;
import com.muse.server.after.util.ProjectAccessGuard;
import jakarta.transaction.Transactional;
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

    public String uploadProjectFile(Long userId, Long projectId, MultipartFile file, boolean toPublish) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId))
            throw new RuntimeException("Only owner can upload project file");

        final String url = minioService.uploadProjectFile(userId, projectId, file, toPublish);

        project.setFilePath(url);
        projectRepository.save(project);

        return url;
    }

    public String uploadProjectCover(Long userId, Long projectId, MultipartFile file, Boolean toPublish) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId))
            throw new RuntimeException("Only owner can upload project cover");

        final String url = minioService.uploadProjectCover(userId, projectId, file, toPublish);

        project.setCoverPath(url);
        projectRepository.save(project);

        return url;
    }

    public String getSharedProjectFileUrl(Long userId, Long projectId) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.hasReadAccess(project, userId))
            throw new RuntimeException("Access denied");

        return minioService.extractSharedProjectFileUrl(
                project.getOwner().getId(), projectId
        );
    }

    public String getSharedCoverUrl(Long userId, Long projectId) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.hasReadAccess(project, userId))
            throw new RuntimeException("Access denied");

        return minioService.extractSharedCoverUrl(
                project.getOwner().getId(), projectId
        );
    }

    public String getProjectFileUrl(Long userId, Long projectId) {
        final ProjectEntity project = findProject(projectId);

        if (!accessGuard.hasReadAccess(project, projectId))
            throw new RuntimeException("Access denied");

        return minioService.extractProjectFileUrl(project.getOwner().getId(), projectId);
    }

    @Transactional
    public void autoSave(Long userId, Long projectId, byte[] content) {
        final ProjectEntity project = findProject(projectId);

        System.out.println("autoSave(byte[]) userId=" + userId + " projectId=" + projectId);


        if (!accessGuard.isMember(project, userId) && !accessGuard.isOwner(project, userId))
            throw new RuntimeException("Access denied");

        final Long ownerId = project.getOwner().getId();
        final String url = minioService.uploadSharedFileBytes(ownerId, projectId, content);
        project.setFilePath(url);
        projectRepository.save(project);
    }

    @Transactional
    public void autoSave(Long userId, Long projectId, MultipartFile file) {
        final ProjectEntity project = findProject(projectId);

        System.out.println("autoSave(MultipartFile) userId=" + userId + " projectId=" + projectId);


        if (!accessGuard.isMember(project, userId) && !accessGuard.isOwner(project, userId))
            throw new RuntimeException("Access denied");

        final Long ownerId = project.getOwner().getId();
        final String url = minioService.uploadSharedCoverFile(ownerId, projectId, file);

        project.setCoverPath(url);
        projectRepository.save(project);
    }

    private ProjectEntity findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
