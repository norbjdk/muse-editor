package com.muse.server.before.security;

import com.muse.server.before.model.entity.ProjectEntity;
import com.muse.server.before.model.repository.ProjectMemberRepository;
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
        return project.getMembers().stream()
                .anyMatch(m -> m.getUser().getId().equals(userId));
    }

    public boolean hasEditAccess(ProjectEntity project, Long userId) {
        return isOwner(project, userId) || isMember(project.getId(), userId);
    }

    public boolean hasReadAccess(ProjectEntity project, Long userId) {
        return project.getPublic() || hasEditAccess(project, userId);
    }
}
