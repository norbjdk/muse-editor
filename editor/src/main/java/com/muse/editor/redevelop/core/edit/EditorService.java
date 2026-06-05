package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.AddValueEvent;


public class EditorService {
    private static final EditorService instance = new EditorService();

    private final CursorModel cursor = CursorModel.getInstance();

    public static EditorService getInstance() {
        return instance;
    }

    private EditorService() {
        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(AddValueEvent.class, addValueEvent -> {
            addNote(addValueEvent.getOctave(), addValueEvent.getStep());
        });
    }

    private void addNote(int octave, char step) {
        if (!EditorState.getInstance().inputModeProperty().get()) return;

        System.out.println("=================================");
        System.out.println("Current Note ID:" + CursorModel.getInstance().getNoteId());
        System.out.println("Current Measure ID:" + CursorModel.getInstance().getMeasureId());
        System.out.println("Which octave ? " + octave);
        System.out.println("What step ? " +  step);
        System.out.println("Selected type: " + EditorState.getInstance().getInputType().toString());
        System.out.println("Adding...");
    }

//    private void addNote(Note.Type type, boolean isRest) {
//        if (!EditorState.getInstance().inputModeProperty().get()) return;
//
//        Note newNote = new Note.Builder()
//                .setType(type)
//                        .isRest(isRest)
//                                .build();
//
//        ScoreManager.getInstance().replaceNote(
//                cursor.getPartId(),
//                cursor.getMeasureIndex(),
//                cursor.getNoteIndex(),
//                newNote
//        );
//    }
}
