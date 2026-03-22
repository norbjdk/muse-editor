package com.norbjdk.museeditor.model.event;

import com.norbjdk.museeditor.core.project.Project;

public class ProjectLoadedEvent implements AppEvent{
    private final Project project;

    public ProjectLoadedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
