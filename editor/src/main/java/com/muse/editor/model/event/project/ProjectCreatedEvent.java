package com.muse.editor.model.event.project;

import com.muse.editor.core.project.Project;
import com.muse.editor.model.event.AppEvent;

public class ProjectCreatedEvent implements AppEvent {
    private final Project project;

    public ProjectCreatedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
