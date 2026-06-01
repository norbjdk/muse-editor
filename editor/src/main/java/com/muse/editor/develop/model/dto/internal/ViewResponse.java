package com.muse.editor.develop.model.dto.internal;

import com.muse.editor.develop.ui.model.Viewable;

public class ViewResponse {
    private final Viewable view;

    public ViewResponse(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
