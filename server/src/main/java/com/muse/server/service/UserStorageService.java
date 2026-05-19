package com.muse.server.service;

import com.muse.server.model.entity.ProjectEntity;
import com.muse.server.model.repository.ProjectRepository;
import com.muse.server.security.ProjectAccessGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserStorageService {

    @Autowired
    private MinioService minioService;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private ProjectAccessGuard accessGuard;


    public String uploadAvatar(Long userId, MultipartFile file) {
        return minioService.uploadAvatar(userId, file);
    }


    public String uploadProjectFile(Long userId, Long projectId, MultipartFile file) {
        ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId)) {
            throw new RuntimeException("Only owner can upload project file");
        }

        String url = minioService.uploadProjectFile(userId, projectId, file);

        project.setCloudFilePath(url);
        projectRepo.save(project);

        return url;
    }

    public String uploadProjectCover(Long userId, Long projectId, MultipartFile file) {
        ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId)) {
            throw new RuntimeException("Only owner can upload project cover");
        }

        String url = minioService.uploadProjectCover(userId, projectId, file);

        project.setCloudCoverUrl(url);
        projectRepo.save(project);

        return url;
    }

    public String getProjectFileUrl(Long userId, Long projectId) {
        ProjectEntity project = findProject(projectId);

        if (!accessGuard.hasReadAccess(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        return minioService.getProjectFileUrl(project.getOwner().getId(), projectId);
    }

    public void deleteProjectFiles(Long userId, Long projectId) {
        ProjectEntity project = findProject(projectId);

        if (!accessGuard.isOwner(project, userId)) {
            throw new RuntimeException("Only owner can delete project files");
        }

        minioService.deleteProjectFiles(project.getOwner().getId(), projectId);

        project.setCloudFilePath(null);
        project.setCloudCoverUrl(null);
        projectRepo.save(project);
    }

    private ProjectEntity findProject(Long projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}