package com.norbjdk.museeditor.model.event;

import com.norbjdk.museeditor.model.dto.internal.ViewResponse;

public class ViewChangedEvent implements AppEvent {
    private final ViewResponse response;

    public ViewChangedEvent(ViewResponse response) {
        this.response = response;
    }

    public ViewResponse getResponse() {
        return response;
    }
}
