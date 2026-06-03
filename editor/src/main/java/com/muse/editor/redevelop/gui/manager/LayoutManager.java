package com.muse.editor.redevelop.gui.manager;

import com.muse.editor.redevelop.gui.component.music.MeasureComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LayoutManager {
    private static final LayoutManager instance = new LayoutManager();

    public static LayoutManager getInstance() {
        return instance;
    }

    private LayoutManager() {}

    private final ObservableList<MeasureComponent> measureComponents = FXCollections.observableArrayList();

    public void register(MeasureComponent measureComponent)  {
        if (measureComponents.contains(measureComponent)) return;

        measureComponents.add(measureComponent);
        sync();
    }

    private void sync() {}
}
