package com.muse.editor.develop.core.edit;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.model.event.edit.InputModeOff;
import com.muse.editor.develop.model.event.edit.InputModeOn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class EditManager {
    private static final EditManager instance = new EditManager();

    public static EditManager getInstance() {
        return instance;
    }

    private final EditService editService = EditService.getInstance();


    private final BooleanProperty inputMode = new SimpleBooleanProperty(false);

    private EditManager() {
        inputMode.addListener(((observable, oldValue, newValue) -> {
            if (newValue) EventBus.getInstance().publish(new InputModeOn());
            else          EventBus.getInstance().publish(new InputModeOff());
        }));
    }

    public BooleanProperty inputModeProperty() {
        return inputMode;
    }

    public void switchMode() {
        inputMode.set(!inputMode.getValue());
    }
}
