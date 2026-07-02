package com.muse.editor.event.view;

import com.muse.editor.event.model.AppEvent;
import com.muse.editor.gui.model.Viewable;

public class ViewChangedEvent extends AppEvent {
    private final Viewable view;

    public ViewChangedEvent(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
