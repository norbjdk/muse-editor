package com.muse.editor.event.view;

import com.muse.editor.event.model.AppEvent;
import com.muse.editor.gui.model.Viewable;

public class ChangeViewEvent extends AppEvent {
    private final Viewable.Name viewName;

    public ChangeViewEvent(Viewable.Name viewName) {
        this.viewName = viewName;
    }

    public Viewable.Name getViewName() {
        return viewName;
    }
}
