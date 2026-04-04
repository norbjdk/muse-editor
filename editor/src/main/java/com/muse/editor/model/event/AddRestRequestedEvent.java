package com.muse.editor.model.event;

public class AddRestRequestedEvent implements AppEvent {
    private final String type;

    public AddRestRequestedEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
