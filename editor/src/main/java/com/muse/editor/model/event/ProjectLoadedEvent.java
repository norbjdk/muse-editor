package com.muse.editor.model.event;

import com.muse.editor.core.project.Project;

public class ProjectLoadedEvent implements AppEvent{
    private final Project project;

    public ProjectLoadedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
