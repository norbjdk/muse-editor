package com.muse.editor.core.edit;

import com.muse.editor.core.model.music.Measure;
import com.muse.editor.core.model.music.Note;
import com.muse.editor.core.model.music.Part;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.project.ProjectManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;

public class ScoreManager {
    private static final ScoreManager instance = new ScoreManager();
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private final ObjectProperty<ScorePartwise> scoreProperty = new SimpleObjectProperty<>(null);

    private final ObservableList<ObjectProperty<Part>> partProperties =
            FXCollections.observableList(new LinkedList<>(), partProp -> new javafx.beans.Observable[]{partProp});

    private final ObservableList<ObjectProperty<Measure>> measureProperties =
            FXCollections.observableList(new LinkedList<>(), measProp -> new javafx.beans.Observable[]{measProp});

    private final ObservableList<ObjectProperty<Note>> noteProperties =
            FXCollections.observableList(new LinkedList<>(), noteProp -> {
                if (noteProp.get() == null) return new javafx.beans.Observable[]{noteProp};
                return new javafx.beans.Observable[]{
                        noteProp,
                        noteProp.get().stepProperty(),
                        noteProp.get().octaveProperty(),
                        noteProp.get().durationProperty(),
                        noteProp.get().typeProperty()
                };
            });

    public static ScoreManager getInstance() { return instance; }

    private ScoreManager() {
        scoreProperty.addListener((observableValue, oldScore, newScore) -> {
            System.out.println("Główny ScorePartwise uległ zmianie!");
            if (projectManager.currentProjectProperty().get() != null) {
                projectManager.scoreProperty().set(newScore);
            }
        });

        noteProperties.addListener((javafx.collections.ListChangeListener.Change<? extends ObjectProperty<Note>> c) -> {
//            while (c.next()) {
//                if (projectManager.getCurrentProject() != null) {
//                    projectManager.markProjectAsModified();
//                }
//            }
        });
    }


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
    }

    public int nextNoteId() {
        return noteProperties.stream()
                .mapToInt(np -> np.get().getId())
                .max()
                .orElse(0) + 1;
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

    public void replaceNote(String partId, int measureIndex, int noteIndex, List<Note> newNotes) {
        if (newNotes == null || newNotes.isEmpty()) return;

        final ObservableList<Note> notes = getMeasure(partId, measureIndex).getNotes();

        if (noteIndex < 0 || noteIndex >= notes.size()) return;

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

    public ObjectProperty<ScorePartwise> scoreProperty() {
        return scoreProperty;
    }
}
