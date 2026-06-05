package com.muse.editor.redevelop.event.editor;

import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.event.model.AppEvent;

public class SetNoteInputEvent extends AppEvent {
    private final Note.Type type;
    private final boolean   isRest;

    public SetNoteInputEvent(Note.Type type, boolean isRest) {
        this.type   = type;
        this.isRest = isRest;
    }

    public Note.Type getType() {
        return type;
    }

    public boolean isRest() {
        return isRest;
    }
}
