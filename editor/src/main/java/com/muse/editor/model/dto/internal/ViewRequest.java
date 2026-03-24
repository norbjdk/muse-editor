package com.muse.editor.model.dto.internal;

import com.muse.editor.ui.model.ViewName;

public class ViewRequest {
    private final ViewName viewName;

    public ViewRequest(ViewName viewName) {
        this.viewName = viewName;
    }

    public ViewName getViewName() {
        return viewName;
    }
}
