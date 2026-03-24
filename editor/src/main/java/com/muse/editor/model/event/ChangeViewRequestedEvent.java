package com.muse.editor.model.event;

import com.muse.editor.model.dto.internal.ViewRequest;

public class ChangeViewRequestedEvent implements AppEvent {
    private final ViewRequest request;

    public ChangeViewRequestedEvent(ViewRequest request) {
        this.request = request;
    }

    public ViewRequest getRequest() {
        return request;
    }
}
