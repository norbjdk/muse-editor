package com.norbjdk.museeditor.model.event;

import com.norbjdk.museeditor.model.dto.internal.ViewRequest;

public class ChangeViewRequestedEvent implements AppEvent {
    private final ViewRequest request;

    public ChangeViewRequestedEvent(ViewRequest request) {
        this.request = request;
    }

    public ViewRequest getRequest() {
        return request;
    }
}
