package com.muse.server.before.model.repository;

import com.muse.server.before.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long id);
    List<ProjectEntity> findByIsPublicTrue(Long id);
}
