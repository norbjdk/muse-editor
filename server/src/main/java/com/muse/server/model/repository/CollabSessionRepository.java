package com.muse.server.model.repository;

import com.muse.server.model.entity.CollabSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollabSessionRepository extends JpaRepository<CollabSessionEntity, Long> {
    List<CollabSessionEntity> findByProjectIdAndIsActiveTrue(Long projectId);
}
