package com.muse.server.model.repository;

import com.muse.server.model.entity.SessionParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipantEntity, SessionParticipantEntity.SessionParticipantId> {
    List<SessionParticipantEntity> findBySessionId(Long sessionId);
}
