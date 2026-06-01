package com.muse.editor.develop.model.event.project;

import com.muse.editor.develop.model.event.AppEvent;

public class ProjectLoadFailedEvent implements AppEvent {
    private final String reason;

    public ProjectLoadFailedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
