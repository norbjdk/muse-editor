package com.muse.editor.ui.component.old_music;

import com.muse.editor.ui.util.SheetMetrics;
import javafx.scene.layout.VBox;

public class SystemPane extends VBox {

    private static final double SYSTEM_GAP = SheetMetrics.STAFF_SPACE * 2.5;

    public SystemPane() {
        setSpacing(SYSTEM_GAP);
    }

    public void addStaff(MeasurePane measurePane) {
        getChildren().add(measurePane);
    }
}
