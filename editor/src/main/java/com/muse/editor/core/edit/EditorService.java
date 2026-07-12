package com.muse.editor.core.edit;

import com.muse.editor.core.cloud.CloudSyncService;
import com.muse.editor.core.model.music.Note;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.AddValueEvent;
import com.muse.editor.event.editor.DeleteValueEvent;
import com.muse.editor.util.Debug;

import java.util.*;


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
        EventBus.getInstance().subscribe(DeleteValueEvent.class, deleteValueEvent -> {
            if (!editorState.inputModeProperty().get()) return;
            removeNote();
        });
    }

    private void removeNote() {
        final Note current = scoreManager.getNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex()
        );

        if (current == null || current.isRest()) {
            System.out.println("Cannot remove: not a note");
            return;
        }

        final Note.Type type = current.getType();
        final int duration = BeatCalculator.noteValue(type);

        final Note restNote = new Note.Builder()
                .setId(ScoreManager.getInstance().nextNoteId())
                .isRest(true)
                .setType(type)
                .setDuration(duration)
                .setStep(current.getStep())
                .setOctave(current.getOctave())
                .build();

        scoreManager.replaceNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex(),
                List.of(restNote)
        );

        System.out.println("Removed note at position: " + cursorModel.getNoteIndex());
        System.out.println("Replaced with rest of type: " + type);


        CloudSyncService.getInstance().markDirty();
        CloudSyncService.getInstance().forceSave();
    }

    private void addNote(char step, int octave) {
        final Note.Type type    = editorState.getSelectedNoteType();
        final Note      current = scoreManager.getNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex()
        );

        if (!BeatCalculator.canBeUsed(current.getType(), type)) return;

        final List<Note> toAdd = new ArrayList<>();

        final Note newNote = new Note.Builder(current)
                .setId(ScoreManager.getInstance().nextNoteId())
                .isRest(false)
                .setType(type)
                .setStep(step)
                .setOctave(octave)
                .setDuration(BeatCalculator.noteValue(type))
                .build();

        toAdd.add(newNote);

        int remaining = BeatCalculator.leftDifference(current.getType(), type);

        while (remaining > 0) {
            int finalRemaining = remaining;
            Note.Type restType = Arrays.stream(Note.Type.values())
                    .sorted(Comparator.comparingInt(BeatCalculator::noteValue).reversed())
                    .filter(t -> BeatCalculator.noteValue(t) <= finalRemaining)
                    .findFirst()
                    .orElseThrow();

            toAdd.add(new Note.Builder()
                    .setId(ScoreManager.getInstance().nextNoteId() + toAdd.size())
                    .isRest(true)
                    .setType(restType)
                    .setDuration(BeatCalculator.noteValue(restType))
                    .build());

            remaining -= BeatCalculator.noteValue(restType);
        }

        scoreManager.replaceNote(
                cursorModel.getPartId(),
                cursorModel.getMeasureIndex(),
                cursorModel.getNoteIndex(),
                toAdd
        );

        scoreManager.getMeasureProperties().forEach(
                measure -> {
                    Debug.check("Measure ID:" + measure.get().getId());
                    measure.get().getNotes().forEach(note -> {
                        Debug.check("Note id: " + note.getId());
                        Debug.check("Note octave: " + note.getOctave() + ", step: " + note.getStep());
                    });
                }
        );

        CloudSyncService.getInstance().markDirty();
        CloudSyncService.getInstance().forceSave();
    }
}
