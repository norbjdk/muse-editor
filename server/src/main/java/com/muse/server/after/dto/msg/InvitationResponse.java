package com.muse.server.after.dto.msg;

public class InvitationResponse {
    private String responder;
    private Long   responderId;
    private String from;
    private boolean accepted;

    public InvitationResponse() {}

    public InvitationResponse(String responder, Long responderId, String from, boolean accepted) {
        this.responder   = responder;
        this.responderId = responderId;
        this.from        = from;
        this.accepted    = accepted;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public Long getResponderId() {
        return responderId;
    }

    public void setResponderId(Long responderId) {
        this.responderId = responderId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isAccepted() {
        return accepted; }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
