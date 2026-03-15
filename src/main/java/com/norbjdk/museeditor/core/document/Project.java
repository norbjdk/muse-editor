package com.norbjdk.museeditor.core.document;

import com.norbjdk.museeditor.core.model.score.ScorePartwise;
import javafx.beans.property.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

public class Project {
    private final String id;

    private final StringProperty title = new SimpleStringProperty("Unnamed");
    private final ObjectProperty<Path> filePath = new SimpleObjectProperty<>(null);
    private final BooleanProperty isSaved = new SimpleBooleanProperty(false);
    private final ObjectProperty<ScorePartwise> scorePartwise = new SimpleObjectProperty<>();
    private final ObjectProperty<Instant> lastModified = new SimpleObjectProperty<>(Instant.now());
    private final IntegerProperty noteCount = new SimpleIntegerProperty(0);
    private final IntegerProperty measureCount = new SimpleIntegerProperty(0);

    public static Project createNew() {
        final Project doc = new Project(generateId());
        doc.title.set("New music sheet");

        return doc;
    }

    public static Project createNew(String title) {
        final Project doc = new Project(generateId());
        doc.title.set(title);

        return doc;
    }

    private Project(String id) {
        this.id = id;
    }

    public void markUnsaved() {
        isSaved.set(false);
        lastModified.set(Instant.now());
    }

    public void markSaved() {
        isSaved.set(true);
    }

    @Override
    public boolean equals(Object o ) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Project d)) {
            return false;
        }

        return Objects.equals(id, d.id);
    }

    @Override
    public String toString() {
        return "Documnent{title = " + title.get() + ", id = " + id + "}";
    }

    private static String generateId() {
        return "mxml-" + System.nanoTime();
    }

    public ObjectProperty<Path> getFilePath() {
        return filePath;
    }

    public String getId() {
        return id;
    }

    public BooleanProperty getIsSaved() {
        return isSaved;
    }

    public StringProperty getTitle() {
        return title;
    }
}
