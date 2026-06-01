package com.muse.editor.develop.core.edit;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.model.event.edit.InputModeOff;
import com.muse.editor.develop.model.event.edit.InputModeOn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class EditorState {
    private static final EditorState instance = new EditorState();

    public static EditorState getInstance() {
        return instance;
    }

    private String  selectedNoteType = "quarter";
    private int     selectedAlter    = 0;
    private boolean dotted           = false;
    private boolean restMode         = false;

    private final BooleanProperty inputMode = new SimpleBooleanProperty(false);

    private EditorState() {
        inputMode.addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                EventBus.getInstance().publish(new InputModeOn());
                System.out.println("InputMode On");
            }
            else {
                EventBus.getInstance().publish(new InputModeOff());
                System.out.println("InputMode Off");
            }
        }));
    }

    public BooleanProperty inputModeProperty() {
        return inputMode;
    }

    public void switchMode() {
        inputMode.set(!inputMode.getValue());
    }

    public String getSelectedNoteType() {
        return selectedNoteType;
    }

    public void setSelectedNoteType(String selectedNoteType) {
        this.selectedNoteType = selectedNoteType;
    }

    public int getSelectedAlter() {
        return selectedAlter;
    }

    public void setSelectedAlter(int selectedAlter) {
        this.selectedAlter = selectedAlter;
    }

    public boolean isDotted() {
        return dotted;
    }

    public void setDotted(boolean dotted) {
        this.dotted = dotted;
    }

    public boolean isRestMode() {
        return restMode;
    }

    public void setRestMode(boolean restMode) {
        this.restMode = restMode;
    }
}
