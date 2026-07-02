package com.muse.editor.event.project;

import com.muse.editor.event.model.AppEvent;

public class ProjectCreatedEvent extends AppEvent {
    private final String projectID;
    private final String projectTitle;

    public ProjectCreatedEvent(String projectID, String projectTitle) {
        this.projectID    = projectID;
        this.projectTitle = projectTitle;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getProjectTitle() {
        return projectTitle;
    }
}
