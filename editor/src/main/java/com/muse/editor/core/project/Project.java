package com.muse.editor.core.project;

import com.muse.editor.core.model.music.ScorePartwise;
import javafx.beans.property.*;

import java.nio.file.Path;
import java.util.Objects;

public class Project {
    private final String id;

    private Long serverId;

    private final StringProperty title   = new SimpleStringProperty("Unnamed");

    private final ObjectProperty<Path>          filePath      = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ScorePartwise> scoreProperty = new SimpleObjectProperty<>(null);

    private final BooleanProperty isSaved = new SimpleBooleanProperty(false);

    private Project(final String id)  {
        this.id = id;
    }

    static Project createNew() {
        final Project project = new Project(generateId());

        project.title.set("New music sheet");

        return project;
    }

    static Project createNew(final String title) {
        final Project project = new Project(generateId());

        project.title.set(title);

        return project;
    }

    public void markUnsaved() {
        isSaved.set(false);
    }

    public void markSaved() {
        isSaved.set(true);
    }

    public BooleanProperty isSavedProperty() {
        return isSaved;
    }

    public ObjectProperty<ScorePartwise> getScoreProperty() {
        return scoreProperty;
    }

    public String getId() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public Long getServerId() {
        return serverId;
    }
    public void setServerId(Long id) {
        this.serverId = id;
    }

    private static String generateId() {
        return "mxml-" + System.nanoTime();
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
}
