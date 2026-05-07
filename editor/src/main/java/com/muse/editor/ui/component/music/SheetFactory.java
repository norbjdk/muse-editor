package com.muse.editor.ui.component.music;

import com.muse.editor.core.edit.Instrument;

public class SheetFactory {
    private SheetFactory() {}

    public static SheetPane createSheetPane(Instrument instrument) {
        switch (instrument.getName()) {
            case Violin -> {
                return new ViolinPane(instrument);
            }
            case Guitar -> {
                return new GuitarPane(instrument);
            }
            case null, default -> throw new IllegalArgumentException();
        }
    }
}
