package com.muse.editor.model.event;

public class RemoveInstrumentFromProjectEvent implements AppEvent {
    final String name;

    public RemoveInstrumentFromProjectEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
