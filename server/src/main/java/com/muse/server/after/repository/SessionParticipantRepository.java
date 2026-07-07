package com.muse.server.after.repository;

import com.muse.server.after.entity.SessionParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionParticipantRepository
        extends JpaRepository<SessionParticipantEntity, SessionParticipantEntity.SessionParticipantId> {

    List<SessionParticipantEntity> findBySessionId(Long sessionId);

    void deleteBySessionIdAndUserId(Long sessionId, Long userId);
}
