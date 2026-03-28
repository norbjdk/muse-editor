package com.muse.editor.model.event;

public class ProjectLoadFailedEvent implements AppEvent {
    private final String reason;

    public ProjectLoadFailedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
