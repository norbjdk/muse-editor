package com.muse.server.after.repository;

import com.muse.server.after.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    List<ProjectMemberEntity> findByUserId(Long id);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
