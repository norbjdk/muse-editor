package com.muse.server.service;

import com.muse.server.model.entity.*;
import com.muse.server.model.repository.*;
import org.springframework.stereotype.Service;

@Service
public class CollabSessionService {

    private final CollabSessionRepository    collabSessionRepository;
    private final ProjectRepository          projectRepository;
    private final SessionParticipantRepository participantRepository;

    public CollabSessionService(CollabSessionRepository collabSessionRepository,
                                ProjectRepository projectRepository,
                                SessionParticipantRepository participantRepository) {
        this.collabSessionRepository  = collabSessionRepository;
        this.projectRepository        = projectRepository;
        this.participantRepository    = participantRepository;
    }

    public Long getOrCreateSession(Long projectId, Long userId) {
        return collabSessionRepository
                .findByProjectIdAndIsActiveTrue(projectId)
                .map(session -> {
                    addParticipant(session, userId);
                    return session.getId();
                })
                .orElseGet(() -> {
                    ProjectEntity project = projectRepository.findById(projectId)
                            .orElseThrow(() -> new RuntimeException("Project not found"));

                    CollabSessionEntity session = new CollabSessionEntity();
                    session.setProject(project);
                    session.setActive(true);
                    collabSessionRepository.save(session);

                    addParticipant(session, userId);
                    return session.getId();
                });
    }

    private void addParticipant(CollabSessionEntity session, Long userId) {
        boolean alreadyIn = session.getParticipants().stream().anyMatch(p -> p.getUser().getId().equals(userId));
        if (alreadyIn) return;

        UserEntity user = new UserEntity();
        user.setId(userId);

        SessionParticipantEntity participant = new SessionParticipantEntity();
        participant.setSession(session);
        participant.setUser(user);
        participantRepository.save(participant);
    }
}