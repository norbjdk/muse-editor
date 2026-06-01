package com.muse.editor.develop.ui.component.music.sheet;

import com.muse.editor.develop.core.edit.Instrument;
import javafx.scene.layout.FlowPane;

public class WindSheet extends SheetPane{
    protected WindSheet(Instrument instrument) {
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
