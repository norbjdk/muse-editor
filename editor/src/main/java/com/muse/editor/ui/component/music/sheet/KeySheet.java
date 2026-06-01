package com.muse.editor.ui.component.music.sheet;

import com.muse.editor.core.edit.Instrument;
import javafx.scene.layout.FlowPane;

public class KeySheet extends SheetPane{
    protected KeySheet(Instrument instrument) {
        super(instrument);
    }

    @Override
    protected void publishChange() {

    }

    @Override
    protected FlowPane buildMeasures() {
        return null;
    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
