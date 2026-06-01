package com.muse.editor.redevelop.core.model.music;

import java.util.ArrayList;
import java.util.List;

public class Measure implements Comparable<Measure> {
    private static int counter =  0;

    private final int        id;
    private final Attributes attributes;
    private final List<Note> notes = new ArrayList<>();

    private Measure(Builder builder) {
        this.id         = builder.id;
        this.attributes = builder.attributes;
    }

    public static int getCounter() {
        return counter;
    }

    public int getId() {
        return id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public static class Builder {
        private int        id;
        private Attributes attributes;
        private List<Note> notes;

        public Builder(Measure existing) {
            this.id         = existing.id;
            this.attributes = existing.attributes;
            this.notes      = existing.notes;
        }

        public Builder() {
            notes = new ArrayList<>();
        }

        public Builder setId(int id) {
            this.id = id;

            return this;
        }

        public Builder setAttributes(Attributes attributes) {
            this.attributes = attributes;

            return this;
        }

        public Builder addNote(Note note) {
            if (notes == null) return this;
            if (note  == null) return this;

            notes.add(note);

            return this;
        }

        public Measure build() {
            return new Measure(this);
        }
    }

    @Override
    public int compareTo(Measure other) {
        return Integer.compare(this.id, other.id);
    }
}
