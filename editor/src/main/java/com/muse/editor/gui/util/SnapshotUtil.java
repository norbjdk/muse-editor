package com.muse.editor.gui.util;

import com.muse.editor.gui.component.music.SheetComponent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

public class SnapshotUtil {
    private static final SnapshotUtil instance = new SnapshotUtil();

    private VBox sheetPage;

    public static SnapshotUtil getInstance() {
        return instance;
    }

    public void init(VBox sheetPage) {
        this.sheetPage = sheetPage;
    }

    public VBox getSheetPage() {
        return sheetPage;
    }

    private SnapshotUtil() {}

    public WritableImage snapSheet() {
        return sheetPage.snapshot(new SnapshotParameters(), null);
    }
}
