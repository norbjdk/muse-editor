package com.muse.editor.event.project;

import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.event.model.AppEvent;

public class CreateProjectEvent extends AppEvent {
    private final NewProjectRequest request;

    public CreateProjectEvent(NewProjectRequest request) {
        this.request = request;
    }

    public NewProjectRequest getRequest() {
        return request;
    }
}
