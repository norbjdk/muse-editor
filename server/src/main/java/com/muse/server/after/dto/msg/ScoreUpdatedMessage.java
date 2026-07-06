package com.muse.server.after.dto.msg;

public class ScoreUpdatedMessage {
    private String type = "SCORE_UPDATED";
    private Long sessionId;
    private String by;

    public ScoreUpdatedMessage(Long sessionId, String by) {
        this.sessionId = sessionId;
        this.by = by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }
}