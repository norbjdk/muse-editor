package com.muse.server.after.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "session")
public class SessionParticipantEntity {

    @EmbeddedId
    private SessionParticipantId id = new SessionParticipantId();

    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private CollabSessionEntity session;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public SessionParticipantId getId() {
        return id;
    }

    public void setId(SessionParticipantId id) {
        this.id = id;
    }

    public CollabSessionEntity getSession() {
        return session;
    }

    public void setSession(CollabSessionEntity session) {
        this.session = session;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Embeddable
    public static class SessionParticipantId implements Serializable {
        private Long sessionId;
        private Long userId;

        public Long getSessionId() {
            return sessionId;
        }

        public void setSessionId(Long sessionId) {
            this.sessionId = sessionId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
