package com.muse.editor.event.editor;

import com.muse.editor.event.model.AppEvent;

public class SelectNoteEvent extends AppEvent {
    private final int noteId;

    public SelectNoteEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }
}
