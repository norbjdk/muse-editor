package com.muse.editor.core.model.music;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class Measure implements Comparable<Measure> {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Attributes> attributes = new SimpleObjectProperty<>(null);
    private final ObservableList<Note> notes = FXCollections.observableArrayList();

    private Measure(Builder builder) {
        setId(builder.id);
        setAttributes(builder.attributes);
        if (builder.notes != null) {
            this.notes.setAll(builder.notes);
        }
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public Attributes getAttributes() { return attributes.get(); }
    public ObjectProperty<Attributes> attributesProperty() { return attributes; }
    public void setAttributes(Attributes attributes) { this.attributes.set(attributes); }

    public ObservableList<Note> getNotes() { return notes; }

    public static class Builder {
        private int id;
        private Attributes attributes;
        private List<Note> notes = new ArrayList<>();

        public Builder(Measure existing) {
            this.id = existing.getId();
            this.attributes = existing.getAttributes() != null ? new Attributes.Builder(existing.getAttributes()).build() : null;
            this.notes = new ArrayList<>();
            for (Note note : existing.getNotes()) {
                this.notes.add(new Note.Builder(note).build());
            }
        }

        public Builder() {}

        public Builder setId(int id) { this.id = id; return this; }
        public Builder setAttributes(Attributes attributes) { this.attributes = attributes; return this; }
        public Builder addNote(Note note) {
            if (note != null) this.notes.add(note);
            return this;
        }

        public Measure build() { return new Measure(this); }
    }

    @Override
    public int compareTo(Measure other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
