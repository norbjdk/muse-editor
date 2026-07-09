package com.muse.server.after.service;

import com.muse.server.after.dto.msg.ParticipantJoinedMessage;
import com.muse.server.after.dto.msg.ParticipantLeftMessage;
import com.muse.server.after.dto.session.SessionResponse;
import com.muse.server.after.dto.user.UserResponse;
import com.muse.server.after.entity.CollabSessionEntity;
import com.muse.server.after.entity.ProjectEntity;
import com.muse.server.after.entity.SessionParticipantEntity;
import com.muse.server.after.entity.UserEntity;
import com.muse.server.after.repository.CollabSessionRepository;
import com.muse.server.after.repository.ProjectRepository;
import com.muse.server.after.repository.SessionParticipantRepository;
import com.muse.server.after.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CollabService {

    @Autowired
    private CollabSessionRepository collabSessionRepository;

    @Autowired
    private SessionParticipantRepository sessionParticipantRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Transactional
    public SessionResponse joinOrCreate(Long projectId, Long userId) {
        final ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        final UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final CollabSessionEntity session = collabSessionRepository
                .findByProjectIdAndActiveTrue(projectId).orElseGet(() -> createSession(project));

        final boolean alreadyJoined = session.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(userId));

        if (!alreadyJoined) {
            final SessionParticipantEntity participant = new SessionParticipantEntity();

            participant.setSession(session);
            participant.setUser(user);
            sessionParticipantRepository.save(participant);

            messagingTemplate.convertAndSend(
                    "/topic/session." + session.getId(),
                    new ParticipantJoinedMessage(session.getId(), userId, user.getUsername())
            );
        }

        return toResponse(session);
    }

    @Transactional
    public void leave(Long sessionId, Long userId) {
        final CollabSessionEntity session = collabSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        sessionParticipantRepository.deleteBySessionIdAndUserId(sessionId, userId);

        messagingTemplate.convertAndSend(
                "/topic/session." + sessionId,
                new ParticipantLeftMessage(sessionId, userId)
        );

        final long remaining = sessionParticipantRepository.findBySessionId(sessionId).size();

        if (remaining == 0) {
            session.setActive(false);
            collabSessionRepository.save(session);
        }
    }

    private CollabSessionEntity createSession(ProjectEntity project) {
        final CollabSessionEntity session = new CollabSessionEntity();

        session.setProject(project);
        session.setActive(true);

        return collabSessionRepository.save(session);
    }

    public SessionResponse getActiveSessionForProject(Long projectId) {
        final CollabSessionEntity session = collabSessionRepository
                .findByProjectIdAndActiveTrue(projectId)
                .orElse(null);

        return session != null ? toResponse(session) : null;
    }

    public List<UserResponse> getActiveCollaboratorsForProject(Long projectId) {
        final CollabSessionEntity session = collabSessionRepository
                .findByProjectIdAndActiveTrue(projectId)
                .orElse(null);

        if (session == null) return null;

        return userService.getAllUsersByUsernames(
                session.getParticipants().stream()
                        .filter(Objects::nonNull)
                        .map(this::toUsername)
                        .collect(Collectors.toList())
        );
    }

    private String toUsername(SessionParticipantEntity participant) {
        return participant.getUser().getUsername();
    }

    private SessionResponse toResponse(CollabSessionEntity session) {
        final List<Long> participantIds = session.getParticipants().stream()
                .map(p -> p.getUser().getId())
                .toList();

        return new SessionResponse(session.getId(), session.getProject().getId(), participantIds);
    }
}
