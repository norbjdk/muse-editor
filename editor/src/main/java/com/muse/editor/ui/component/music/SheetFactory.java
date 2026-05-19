package com.muse.editor.ui.component.music;

import com.muse.editor.core.edit.Instrument;

public class SheetFactory {
    private SheetFactory() {}

    public static SheetPane createSheetPane(Instrument instrument) {
        return switch (instrument.getName()) {
            case Violin, Cello, Viola -> new StringSheet(instrument);
            case null, default -> throw new IllegalArgumentException();
        };
    }
}
