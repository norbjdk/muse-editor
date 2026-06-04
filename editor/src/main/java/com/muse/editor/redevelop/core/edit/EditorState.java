package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.ChangeInputModeEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class EditorState {
    private static final EditorState instance = new EditorState();

    public static EditorState getInstance() {
        return instance;
    }

    private final BooleanProperty inputMode = new SimpleBooleanProperty(false);

    private EditorState() {
        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(ChangeInputModeEvent.class, changeInputModeEvent -> {
            switchMode();
        });
        inputMode.addListener((observableValue, aBoolean, t1) -> {
            System.out.println("Input mode: " + (t1 ? "on" : "off"));
        });
    }

    public void switchMode() {
        inputMode.set(!inputMode.getValue());
    }

    public BooleanProperty inputModeProperty() {
        return inputMode;
    }
}
