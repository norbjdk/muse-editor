package com.muse.editor.core.edit;

import com.muse.editor.core.model.music.Measure;
import com.muse.editor.core.model.music.Note;
import com.muse.editor.core.model.music.Part;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.NoteSelectedEvent;
import com.muse.editor.event.editor.SelectNoteEvent;
import com.muse.editor.event.project.PartComponentChangedEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

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
    private int measureId;
    private int noteId;

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(PartComponentChangedEvent.class, partComponentChangedEvent -> {
            if (partComponentChangedEvent.getPart() != null) {
                bindPart(partComponentChangedEvent.getPart());
            }
        });

        EventBus.getInstance().subscribe(SelectNoteEvent.class, selectNoteEvent -> {
            if (partProperty.get() == null) return;

            List<Measure> measures = partProperty.get().getMeasures();
            for (int mi = 0; mi < measures.size(); mi++) {
                List<Note> notes = measures.get(mi).getNotes();
                for (int ni = 0; ni < notes.size(); ni++) {
                    if (notes.get(ni).getId() == selectNoteEvent.getNoteId()) {
                        this.noteId      = selectNoteEvent.getNoteId();
                        this.measureId   = measures.get(mi).getId();
                        this.measureIndex = mi;
                        this.noteIndex    = ni;

                        if (!editorState.inputModeProperty().get()) {
                            editorState.inputModeProperty().set(true);
                        }

                        EventBus.getInstance().publish(new NoteSelectedEvent(this.noteId));
                        return;
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

    public int getNoteId() {
        return noteId;
    }

    public int getMeasureId() {
        return measureId;
    }

    public int getMeasureIndex() {
        return measureIndex;
    }

    public int getNoteIndex() {
        return noteIndex;
    }

    public String getPartId() {
        return partProperty.get().getId();
    }
}
