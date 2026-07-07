package com.muse.server.after.repository;

import com.muse.server.after.entity.CollabSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollabSessionRepository extends JpaRepository<CollabSessionEntity, Long> {
    Optional<CollabSessionEntity> findByProjectIdAndActiveTrue(Long projectId);
}
