package com.muse.editor.redevelop.event.project;

import com.muse.editor.redevelop.event.model.AppEvent;

public class ProjectOpenedEvent extends AppEvent {
    private final String id;
    private final String title;

    public ProjectOpenedEvent(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
