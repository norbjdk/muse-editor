package com.muse.server.model.repository;

import com.muse.server.model.entity.CollabSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollabSessionRepository extends JpaRepository<CollabSessionEntity, Long> {
    Optional<CollabSessionEntity> findByProjectIdAndIsActiveTrue(Long projectId);

}
