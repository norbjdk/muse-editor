package com.muse.server.model.repository;

import com.muse.server.model.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, ProjectMemberEntity.ProjectMemberId> {
    List<ProjectMemberEntity> findByUserId(Long id);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
