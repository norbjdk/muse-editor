package com.muse.editor.develop.model.event;

import com.muse.editor.develop.core.model.dto.NewProjectRequest;

public class CreateProjectRequestedEvent implements AppEvent {
    private final NewProjectRequest request;

    public CreateProjectRequestedEvent(NewProjectRequest request) {
        this.request = request;
    }

    public NewProjectRequest getRequest() {
        return request;
    }
}
