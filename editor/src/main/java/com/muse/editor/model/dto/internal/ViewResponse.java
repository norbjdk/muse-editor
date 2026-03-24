package com.muse.editor.model.dto.internal;

import com.muse.editor.ui.model.Viewable;

public class ViewResponse {
    private final Viewable view;

    public ViewResponse(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
