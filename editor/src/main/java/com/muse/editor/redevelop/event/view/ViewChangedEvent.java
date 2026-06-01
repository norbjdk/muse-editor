package com.muse.editor.redevelop.event.view;

import com.muse.editor.redevelop.event.model.AppEvent;
import com.muse.editor.redevelop.gui.model.Viewable;

public class ViewChangedEvent extends AppEvent {
    private final Viewable view;

    public ViewChangedEvent(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
