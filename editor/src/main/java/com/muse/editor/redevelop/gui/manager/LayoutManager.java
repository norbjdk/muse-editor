package com.muse.editor.redevelop.gui.manager;

import com.muse.editor.redevelop.gui.component.music.MeasureComponent;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
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
        measureComponent.measureWidthProperty().addListener((observableValue, number, t1) -> {
            sync();
        });
    }

    private void sync() {
        final double maxWidth = measureComponents.stream()
                .mapToDouble(m -> m.measureWidthProperty().get())
                .max()
                .orElse(MusicMetrics.BASE_MEASURE_WIDTH);

        measureComponents.forEach(m -> m.measureWidthProperty().set(maxWidth));
    }
}
