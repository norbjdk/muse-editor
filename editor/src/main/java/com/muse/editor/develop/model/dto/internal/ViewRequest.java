package com.muse.editor.develop.model.dto.internal;

import com.muse.editor.develop.ui.model.ViewName;

public class ViewRequest {
    private final ViewName viewName;

    public ViewRequest(ViewName viewName) {
        this.viewName = viewName;
    }

    public ViewName getViewName() {
        return viewName;
    }
}
