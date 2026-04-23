package com.muse.editor.model.event;

import com.muse.editor.core.project.Project;

public class ProjectCreatedEvent implements AppEvent {
    private final Project project;

    public ProjectCreatedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
