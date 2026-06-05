package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.NoteSelectedEvent;
import com.muse.editor.redevelop.event.editor.SelectNoteEvent;
import com.muse.editor.redevelop.event.project.PartComponentChangedEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CursorModel {
    private static final CursorModel instance = new CursorModel();

    private final EditorState editorState = EditorState.getInstance();

    public static CursorModel getInstance() {
        return instance;
    }

    private CursorModel() {
        setupEventListeners();
    }

    private ObjectProperty<Part> partProperty = new SimpleObjectProperty<>(null);

    private int measureIndex;
    private int noteIndex;

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(PartComponentChangedEvent.class, partComponentChangedEvent -> {
            if (partComponentChangedEvent.getPart() != null) {
                bindPart(partComponentChangedEvent.getPart());
            }
        });
        EventBus.getInstance().subscribe(SelectNoteEvent.class, selectNoteEvent -> {
            for (Measure measure : partProperty.get().getMeasures()) {
                for (Note note : measure.getNotes()) {
                    if (note.getId() == selectNoteEvent.getNoteId()) {
                        this.noteIndex    = selectNoteEvent.getNoteId();
                        this.measureIndex = measure.getId();

                        if (!editorState.inputModeProperty().get()) {
                            editorState.inputModeProperty().set(true);
                        }

                        EventBus.getInstance().publish(new NoteSelectedEvent(this.noteIndex)); // ←
                    }
                }
            }
        });
    }

    private void bindPart(Part part) {
        this.partProperty.set(part);

        this.measureId = 0;
        this.noteId = 0;
        this.measureIndex = 0;
        this.noteIndex = 0;

        System.out.println("New bind for part:" + part.getId());
    }

    public ObjectProperty<Part> partProperty() {
        return partProperty;
    }

    public int getNoteIndex() {
        return noteIndex;
    }

    public int getMeasureIndex() {
        return measureIndex;
    }
}
