package com.muse.editor.develop.model.event;

import com.muse.editor.develop.model.dto.internal.ViewResponse;

public class ViewChangedEvent implements AppEvent {
    private final ViewResponse response;

    public ViewChangedEvent(ViewResponse response) {
        this.response = response;
    }

    public ViewResponse getResponse() {
        return response;
    }
}
