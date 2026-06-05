package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.SetNoteInputEvent;

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
        EventBus.getInstance().subscribe(SetNoteInputEvent.class, setNoteInputEvent -> {
            //addNote(setNoteInputEvent.getType(), setNoteInputEvent.isRest());
        });
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
