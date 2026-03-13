package com.norbjdk.museeditor.model.dto.internal;

import com.norbjdk.museeditor.ui.model.Viewable;

public class ViewResponse {
    private final Viewable view;

    public ViewResponse(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
