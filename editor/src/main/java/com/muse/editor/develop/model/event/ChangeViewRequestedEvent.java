package com.muse.editor.develop.model.event;

import com.muse.editor.develop.model.dto.internal.ViewRequest;

public class ChangeViewRequestedEvent implements AppEvent {
    private final ViewRequest request;

    public ChangeViewRequestedEvent(ViewRequest request) {
        this.request = request;
    }

    public ViewRequest getRequest() {
        return request;
    }
}
