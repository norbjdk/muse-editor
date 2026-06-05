package com.muse.editor.redevelop.event.editor;

import com.muse.editor.redevelop.event.model.AppEvent;

public class AddValueEvent extends AppEvent {
    private final int octave;
    private final char step;

    public AddValueEvent(int octave, char step) {
        this.octave = octave;
        this.step   = step;
    }

    public int getOctave() {
        return octave;
    }

    public char getStep() {
        return step;
    }
}
