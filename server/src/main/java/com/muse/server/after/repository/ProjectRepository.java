package com.muse.server.after.repository;

import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long id);
    List<ProjectEntity> findByIsPublishedTrue(Long id);

    List<ProjectEntity> findByCollaboratorsId(ProjectMemberEntity.ProjectMemberId collaboratorsId);

    @Query("SELECT DISTINCT p FROM ProjectEntity p " +
            "LEFT JOIN p.collaborators c " +
            "WHERE p.owner.id = :userId OR c.user.id = :userId")
    List<ProjectEntity> findAllByOwnerIdOrCollaboratorUserId(@Param("userId") Long userId);
}
