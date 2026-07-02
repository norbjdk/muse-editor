package com.muse.editor.event.project;

import com.muse.editor.event.model.AppEvent;

public class ChangePartComponentEvent extends AppEvent {
    private final String partName;

    public ChangePartComponentEvent(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return partName;
    }
}
