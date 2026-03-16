package com.norbjdk.museeditor.core.model.score;

import java.util.ArrayList;
import java.util.List;

public class Measure {
    private static int id = 0;
    private Attributes attributes;
    private final List<Note> notes = new ArrayList<>();

    public Measure() {
        id += 1;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
