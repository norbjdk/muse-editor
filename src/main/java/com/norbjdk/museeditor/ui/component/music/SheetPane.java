package com.norbjdk.museeditor.ui.component.music;

import com.norbjdk.museeditor.core.document.Project;
import com.norbjdk.museeditor.core.model.score.ScorePartwise;
import com.norbjdk.museeditor.ui.model.Presentable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SheetPane extends ScrollPane implements Presentable {

    private Consumer<String> onContentChanged;
    private Consumer<String> onSelectionChanged;
    private Runnable onDocumentReady;

    private final BooleanProperty editorReady = new SimpleBooleanProperty(false);
    private Project boundProject;

    List<PagePane> pages = new ArrayList<>();

    public SheetPane() {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {

    }

    @Override
    public void setupLayout() {

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
