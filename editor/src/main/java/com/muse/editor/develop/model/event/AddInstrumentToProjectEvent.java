package com.muse.editor.develop.model.event;

public class AddInstrumentToProjectEvent implements AppEvent {
    final String name;

    public AddInstrumentToProjectEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
