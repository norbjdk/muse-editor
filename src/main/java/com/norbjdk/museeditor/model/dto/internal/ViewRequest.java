package com.norbjdk.museeditor.model.dto.internal;

import com.norbjdk.museeditor.ui.model.ViewName;

public class ViewRequest {
    private final ViewName viewName;

    public ViewRequest(ViewName viewName) {
        this.viewName = viewName;
    }

    public ViewName getViewName() {
        return viewName;
    }
}
