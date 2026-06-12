package com.muse.server.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "project_members")
public class ProjectMemberEntity {
    @EmbeddedId
    private ProjectMemberId id = new ProjectMemberId();

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public ProjectMemberId getId() {
        return id;
    }

    public void setId(ProjectMemberId id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Embeddable
    public static class ProjectMemberId implements Serializable {
        private Long projectId;
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if ((! (o instanceof ProjectMemberId))) return false;
            ProjectMemberId that = (ProjectMemberId) o;
            return Objects.equals(projectId, that.projectId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(projectId, userId);
        }

    }
}
