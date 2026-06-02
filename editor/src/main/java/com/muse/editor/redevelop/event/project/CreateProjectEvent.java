package com.muse.editor.redevelop.event.project;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.event.model.AppEvent;

public class CreateProjectEvent extends AppEvent {
    private final NewProjectRequest request;

    public CreateProjectEvent(NewProjectRequest request) {
        this.request = request;
    }

    public NewProjectRequest getRequest() {
        return request;
    }
}
