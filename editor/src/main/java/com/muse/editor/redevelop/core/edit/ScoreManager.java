package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.project.ProjectManager;

public class ScoreManager {
    private static final ScoreManager instance = new ScoreManager();
    private final ProjectManager projectManager = ProjectManager.getInstance();

    public static ScoreManager getInstance() { return instance; }

    private ScoreManager() {}

    public Measure getMeasure(String partId, int measureIndex) {
        return getPart(partId)
                .getMeasures()
                .get(measureIndex);
    }

    public Note getNote(String partId, int measureIndex, int noteIndex) {
        return getMeasure(partId, measureIndex)
                .getNotes()
                .get(noteIndex);
    }

    public void replaceNote(String partId, int measureIndex, int noteIndex, Note newNote) {
        getMeasure(partId, measureIndex)
                .getNotes()
                .set(noteIndex, newNote);
    }

    private Part getPart(String partId) {
        return projectManager.scoreProperty().get().getParts().stream()
                .filter(p -> p.getId().equals(partId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Part not found: " + partId));
    }
}
