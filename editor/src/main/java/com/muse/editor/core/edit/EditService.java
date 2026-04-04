package com.muse.editor.core.edit;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.io.service.FileIOService;
import com.muse.editor.core.model.score.Measure;
import com.muse.editor.core.model.score.Note;
import com.muse.editor.core.model.score.ScorePartwise;
import com.muse.editor.core.project.Project;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.model.event.AddNoteRequestedEvent;
import com.muse.editor.model.event.AddRestRequestedEvent;

public class EditService {
    private final static EditService instance = new EditService();

    public static EditService getInstance() {
        return instance;
    }

    private EditService() {}

    private final CursorModel cursor = new CursorModel();

    public CursorModel getCursor() {
        return cursor;
    }

    public void init() {
        EventBus.getInstance().subscribe(AddNoteRequestedEvent.class, event -> {
            final Project project = ProjectManager.getInstance().getCurrentDocument();
            if (project == null) return;
            addNote(project, 'C', 4, event.getType());
        });

        EventBus.getInstance().subscribe(AddRestRequestedEvent.class, event -> {
            final Project project = ProjectManager.getInstance().getCurrentDocument();
            if (project == null) return;
            addRest(project, event.getType());
        });
    }

    public void addNote(Project project, char step, int octave, String type) {
        final ScorePartwise score = project.getScorePartwise().get();
        if (score == null) return;

        final Measure measure = getMeasure(score);
        if (measure == null) return;

        final int divisions = getDivisions(score);
        final int duration  = typeToDuration(type, divisions);
        final int staff     = cursor.getStaffIndex() + 1;
        final String stem   = (cursor.getStaffIndex() == 0) ? "up" : "down";

        final Note note = new Note.Builder()
                .isRest(false)
                .whichStep(step)
                .whichOctave(octave)
                .setDuration(duration)
                .whichVoice(cursor.getVoice())
                .whatType(type)
                .stemDirection(stem)
                .whichStaff(staff)
                .build();

        measure.getNotes().add(note);
        project.markUnsaved();
        saveIfPathExists(project, score);
    }

    public void addRest(Project project, String type) {
        final ScorePartwise score = project.getScorePartwise().get();
        if (score == null) return;

        final Measure measure = getMeasure(score);
        if (measure == null) return;

        final int divisions = getDivisions(score);
        final int duration  = typeToDuration(type, divisions);
        final int staff     = cursor.getStaffIndex() + 1;

        final Note rest = new Note.Builder()
                .isRest(true)
                .setDuration(duration)
                .whichVoice(cursor.getVoice())
                .whatType(type)
                .whichStaff(staff)
                .build();

        measure.getNotes().add(rest);
        project.markUnsaved();
        saveIfPathExists(project, score);
    }

    private Measure getMeasure(ScorePartwise score) {
        final int staffIndex = cursor.getStaffIndex();
        if (staffIndex >= score.getParts().size()) return null;
        final var measures = score.getParts().get(staffIndex).getMeasures();
        if (cursor.getMeasureIndex() >= measures.size()) return null;
        return measures.get(cursor.getMeasureIndex());
    }

    private int getDivisions(ScorePartwise score) {
        return score.getParts().get(0).getMeasures().get(0)
                .getAttributes().getDivisions();
    }

    private int typeToDuration(String type, int divisions) {
        return switch (type) {
            case "whole"   -> divisions * 4;
            case "half"    -> divisions * 2;
            case "quarter" -> divisions;
            case "eighth"  -> divisions / 2;
            case "16th"    -> divisions / 4;
            case "32nd"    -> divisions / 8;
            default        -> divisions;
        };
    }

    private void saveIfPathExists(Project project, ScorePartwise score) {
        if (project.getFilePath().get() != null) {
            FileIOService.getInstance().save(score, project.getFilePath().get());
        }
    }
}
