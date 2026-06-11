package com.muse.editor.event.project;

import com.muse.editor.core.model.music.Part;
import com.muse.editor.event.model.AppEvent;

public class PartComponentChangedEvent extends AppEvent {
    private final Part part;

    public PartComponentChangedEvent(Part part) {
        this.part = part;
    }

    public Part getPart() {
        return part;
    }
}
