package com.muse.editor.ui.component.music;

import com.muse.editor.core.model.score.Measure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class LayoutManager {
    private final static LayoutManager instance = new LayoutManager();

    private final ObservableList<MeasureComponent> measureComponents = FXCollections.observableArrayList();

    public static LayoutManager getInstance() {
        return instance;
    }

    private LayoutManager() {}

    public void register(MeasureComponent measureComponent) {
        measureComponents.add(measureComponent);
        measureComponent.measureWidthProperty().addListener((obs, newValue, oldValue) -> {
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
