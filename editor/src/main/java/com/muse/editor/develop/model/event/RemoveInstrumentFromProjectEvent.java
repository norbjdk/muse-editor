package com.muse.editor.develop.model.event;

public class RemoveInstrumentFromProjectEvent implements AppEvent {
    final String name;

    public RemoveInstrumentFromProjectEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
