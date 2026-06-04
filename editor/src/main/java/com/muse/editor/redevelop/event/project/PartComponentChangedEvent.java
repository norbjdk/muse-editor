package com.muse.editor.redevelop.event.project;

import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.event.model.AppEvent;

public class PartComponentChangedEvent extends AppEvent {
    private final Part part;

    public PartComponentChangedEvent(Part part) {
        this.part = part;
    }

    public Part getPart() {
        return part;
    }
}
