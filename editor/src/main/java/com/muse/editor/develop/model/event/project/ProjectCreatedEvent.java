package com.muse.editor.develop.model.event.project;

import com.muse.editor.develop.core.project.Project;
import com.muse.editor.develop.model.event.AppEvent;

public class ProjectCreatedEvent implements AppEvent {
    private final Project project;

    public ProjectCreatedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
