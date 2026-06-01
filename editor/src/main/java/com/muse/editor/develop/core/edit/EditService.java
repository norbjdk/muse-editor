package com.muse.editor.develop.core.edit;

import com.muse.editor.develop.core.model.score.Measure;
import com.muse.editor.develop.core.model.score.Note;

import java.util.ArrayList;
import java.util.List;

public class EditService {
    private final static EditService instance = new EditService();

    public static EditService getInstance() {
        return instance;
    }

    private List<Instrument> instruments = new ArrayList<>();

    private EditService() {}

    public void handleAddNote(final Measure measure, final Note note) {
        measure.getNotes().add(note);
    }

    public void handleAddNotes(final Measure measure, final List<Note> notes) {
        measure.getNotes().add((Note) notes);
    }

    public void handleAddMeasure(final Measure measure) {

    }
}
