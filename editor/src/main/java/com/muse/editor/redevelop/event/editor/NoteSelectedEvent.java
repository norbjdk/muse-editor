package com.muse.editor.redevelop.event.editor;

import com.muse.editor.redevelop.event.model.AppEvent;

public class NoteSelectedEvent extends AppEvent {
    private final int noteId;

    public NoteSelectedEvent(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }
}
