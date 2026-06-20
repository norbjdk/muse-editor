package com.muse.server.after.repository;

import com.muse.server.after.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long id);
    List<ProjectEntity> findByIsPublishedTrue(Long id);
}
