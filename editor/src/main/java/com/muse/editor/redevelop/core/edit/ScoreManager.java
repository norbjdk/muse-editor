package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.model.music.ScorePartwise;
import com.muse.editor.redevelop.core.project.ProjectManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ScoreManager {
    private static final ScoreManager instance = new ScoreManager();
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private final ObjectProperty<ScorePartwise> scoreProperty = new SimpleObjectProperty<>(null);

    private final ObservableList<ObjectProperty<Part>>    partProperties    = FXCollections.observableList(new LinkedList<>());
    private final ObservableList<ObjectProperty<Measure>> measureProperties = FXCollections.observableList(new LinkedList<>());
    private final ObservableList<ObjectProperty<Note>>    noteProperties    = FXCollections.observableList(new LinkedList<>());

    public static ScoreManager getInstance() { return instance; }

    private ScoreManager() {}

    public void assignScore(ScorePartwise scorePartwise) {
        scoreProperty.set(scorePartwise);

        scoreProperty.get().getParts().forEach(part -> {
            partProperties.add(new SimpleObjectProperty<>(part));
            part.getMeasures().forEach(measure -> {
                measureProperties.add(new SimpleObjectProperty<>(measure));
                measure.getNotes().forEach(note -> {
                    noteProperties.add(new SimpleObjectProperty<>(note));
                });
            });
        });

        System.out.println("==============");
        System.out.println("Total parts: " + partProperties.size());
        System.out.println("Total measures: " + measureProperties.size());
        System.out.println("Total notes: " + noteProperties.size());
        System.out.println("Score assigned:");

        List<String> link = new LinkedList<>();

    }

    public Measure getMeasure(String partId, int measureIndex) {
        return getPart(partId).get()
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

    public void replaceNote(String partId, int measureIndex, int noteIndex, List<Note> newNotes) {
        final ObservableList<Note> notes = getMeasure(partId, measureIndex).getNotes();

        notes.set(noteIndex, newNotes.getFirst());

        for (int i = 1; i < newNotes.size(); i++) {
            notes.add(noteIndex + i, newNotes.get(i));
        }
    }

    public ObjectProperty<Part> getPart(String partId) {
        return partProperties.stream()
                .filter(partObjectProperty -> partObjectProperty.get().getId().equals(partId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Part not found: " + partId));
    }

    public ObservableList<ObjectProperty<Measure>> getMeasures(String partId) {
        List<Measure> partMeasures = getPart(partId).get().getMeasures();

        ObservableList<ObjectProperty<Measure>> result = FXCollections.observableList(new LinkedList<>());

        measureProperties.stream()
                .filter(mp -> partMeasures.contains(mp.get()))
                .forEach(result::add);

        return result;
    }

    public ObservableList<ObjectProperty<Part>> getPartProperties() {
        return partProperties;
    }

    public ObservableList<ObjectProperty<Measure>> getMeasureProperties() {
        return measureProperties;
    }

    public ObservableList<ObjectProperty<Note>> getNoteProperties() {
        return noteProperties;
    }
}
