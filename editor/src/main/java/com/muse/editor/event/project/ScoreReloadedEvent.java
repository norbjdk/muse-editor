package com.muse.editor.event.project;

import com.muse.editor.event.model.AppEvent;

public class ScoreReloadedEvent extends AppEvent {
    private final String triggeredBy;

    public ScoreReloadedEvent(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }
}
