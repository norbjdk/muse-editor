package com.muse.server.before.model.repository;

import com.muse.server.before.model.entity.CollabSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollabSessionRepository extends JpaRepository<CollabSessionEntity, Long> {
    Optional<CollabSessionEntity> findByProjectIdAndIsActiveTrue(Long projectId);

}
