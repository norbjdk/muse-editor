package com.muse.editor.model.event;

import com.muse.editor.model.dto.internal.ViewResponse;

public class ViewChangedEvent implements AppEvent {
    private final ViewResponse response;

    public ViewChangedEvent(ViewResponse response) {
        this.response = response;
    }

    public ViewResponse getResponse() {
        return response;
    }
}
