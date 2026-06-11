package com.muse.editor.event.editor;

import com.muse.editor.event.model.AppEvent;

public class NoteSelectedEvent extends AppEvent {
    private final int noteId;

    public NoteSelectedEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }
}
