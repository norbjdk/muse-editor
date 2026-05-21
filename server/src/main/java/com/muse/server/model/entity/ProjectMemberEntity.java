package com.muse.server.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "project_members")
public class ProjectMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
