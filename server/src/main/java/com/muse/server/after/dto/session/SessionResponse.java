package com.muse.server.after.dto.session;

import java.util.List;

public class SessionResponse {
    private Long sessionId;
    private Long projectId;
    private List<Long> participantIds;

    public SessionResponse(Long sessionId, Long projectId, List<Long> participantIds) {
        this.sessionId = sessionId;
        this.projectId = projectId;
        this.participantIds = participantIds;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }
}
