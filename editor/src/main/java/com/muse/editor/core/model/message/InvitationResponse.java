package com.muse.editor.core.model.message;

public class InvitationResponse {
    private String responder;
    private String from;
    private boolean accepted;

    public InvitationResponse() {}

    public InvitationResponse(String responder, String from, boolean accepted) {
        this.responder = responder;
        this.from = from;
        this.accepted = accepted;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
