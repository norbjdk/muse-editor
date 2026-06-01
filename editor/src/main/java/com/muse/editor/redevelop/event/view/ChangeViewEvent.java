package com.muse.editor.redevelop.event.view;

import com.muse.editor.redevelop.event.model.AppEvent;
import com.muse.editor.redevelop.gui.model.Viewable;

public class ChangeViewEvent extends AppEvent {
    private final Viewable.Name viewName;

    public ChangeViewEvent(Viewable.Name viewName) {
        this.viewName = viewName;
    }

    public Viewable.Name getViewName() {
        return viewName;
    }
}
