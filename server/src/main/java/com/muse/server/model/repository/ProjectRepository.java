package com.muse.server.model.repository;

import com.muse.server.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long ownerId);
    List<ProjectEntity> findByIsPublicTrue();
}