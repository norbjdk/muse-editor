package com.muse.editor.redevelop.event.editor;

import com.muse.editor.redevelop.event.model.AppEvent;

public class SelectNoteEvent extends AppEvent {
    private final int noteId;

    public SelectNoteEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }
}
