package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.AddValueEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class EditorService {
    private static final EditorService instance = new EditorService();

    private final ScoreManager  scoreManager  = ScoreManager.getInstance();
    private final CursorModel   cursorModel   = CursorModel.getInstance();
    private final EditorState   editorState   = EditorState.getInstance();

    public static EditorService getInstance() {
        return instance;
    }

    private EditorService() {
        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(AddValueEvent.class, event -> {
            if (!editorState.inputModeProperty().get()) return;
            addNote(event.getStep(), event.getOctave());
        });
    }

    private void addNote(char step, int octave) {
        final List<Note> toAdd = new ArrayList<>();
        final Note.Type type = EditorState.getInstance().getSelectedNoteType();

        final Note current = scoreManager.getNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex()
        );

        if (!BeatCalculator.canBeUsed(current.getType(), type)) return;

        final Note newNote = new Note.Builder(current)
                .isRest(false)
                .setType(type)
                .setStep(step)
                .setOctave(octave)
                .build();

        toAdd.add(newNote);

        final int diff = BeatCalculator.leftDifference(current.getType(), newNote.getType());

        if (diff > 0) {
            final Note newRest = new Note.Builder()
                    .setId(current.getId() + 1)
                    .isRest(true)
                    .setType(BeatCalculator.valueToType(diff))
                    .setDuration(diff)
                    .build();

            toAdd.add(newRest);
        }

        scoreManager.replaceNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex(),
                toAdd
        );
    }
}
