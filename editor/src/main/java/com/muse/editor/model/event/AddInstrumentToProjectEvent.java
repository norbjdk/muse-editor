package com.muse.editor.model.event;

public class AddInstrumentToProjectEvent implements AppEvent {
    final String name;

    public AddInstrumentToProjectEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
