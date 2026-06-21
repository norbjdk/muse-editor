package com.muse.server.after.util;

import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectAccessGuard {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public boolean isOwner(ProjectEntity project, Long userId) {
        return project.getOwner().getId().equals(userId);
    }

    public boolean isMember(Long projectId, Long userId) {
        return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    public boolean isMember(ProjectEntity project, Long userId) {
        if (isOwner(project, userId)) return true;
        return project.getCollaborators().stream()
                .anyMatch(m -> m.getUser().getId().equals(userId));
    }

    public boolean hasEditAccess(ProjectEntity project, Long userId) {
        return isOwner(project, userId) || isMember(project.getId(), userId);
    }

    public boolean hasReadAccess(ProjectEntity project, Long userId) {
        return project.getPublished() || hasEditAccess(project, userId);
    }
}
