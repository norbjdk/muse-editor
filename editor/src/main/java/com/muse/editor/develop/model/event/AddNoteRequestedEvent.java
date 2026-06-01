package com.muse.editor.develop.model.event;

public class AddNoteRequestedEvent implements AppEvent {
    private final String type;

    public AddNoteRequestedEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
