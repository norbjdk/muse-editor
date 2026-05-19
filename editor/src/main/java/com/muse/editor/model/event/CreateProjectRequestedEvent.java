package com.muse.editor.model.event;

import com.muse.editor.core.model.dto.NewProjectRequest;

public class CreateProjectRequestedEvent implements AppEvent {
    private final NewProjectRequest request;

    public CreateProjectRequestedEvent(NewProjectRequest request) {
        this.request = request;
    }

    public NewProjectRequest getRequest() {
        return request;
    }
}
