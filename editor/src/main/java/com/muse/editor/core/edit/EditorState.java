package com.muse.editor.core.edit;

import com.muse.editor.core.model.music.Note;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.*;
import com.muse.editor.event.editor.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class EditorState {
    private static final EditorState instance = new EditorState();

    private InputType inputType = InputType.NOTE;

    private Note.Type selectedNoteType = Note.Type.Quarter;
    private boolean   isRestNote       = false;

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

        EventBus.getInstance().subscribe(SetNoteInputEvent.class, setNoteInputEvent -> {
            this.inputType = InputType.NOTE;

            this.selectedNoteType = setNoteInputEvent.getType();
            this.isRestNote       = setNoteInputEvent.isRest();
        });

        EventBus.getInstance().subscribe(SetRestInputEvent.class, setRestInputEvent -> {
            this.inputType = InputType.REST;
        });

        EventBus.getInstance().subscribe(SetClefInputEvent.class, setClefInputEvent -> {
            this.inputType = InputType.CLEF;
        });

        EventBus.getInstance().subscribe(SetAccidentalInputEvent.class, setAccidentalInputEvent -> {
            this.inputType = InputType.ACCIDENTAL;
        });

        EventBus.getInstance().subscribe(SetDynamicInputEvent.class, setDynamicInputEvent -> {
            this.inputType = InputType.DYNAMIC;
        });

        EventBus.getInstance().subscribe(SetTimeSigInputEvent.class, setTimeSigInputEvent -> {
            this.inputType = InputType.TIME_SIG;
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

    public InputType getInputType() {
        return inputType;
    }

    public Note.Type getSelectedNoteType() {
        return selectedNoteType;
    }

    public boolean isRestNote() {
        return isRestNote;
    }
}
