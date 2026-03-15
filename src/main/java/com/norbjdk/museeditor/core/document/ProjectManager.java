package com.norbjdk.museeditor.core.document;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class ProjectManager {
    private static final ProjectManager instance = new ProjectManager();

    public static ProjectManager getInstance() {
        return instance;
    }

    private final ObservableList<Project> openProjects = FXCollections.observableArrayList();

    private final ObjectProperty<Project> currentDocument = new SimpleObjectProperty<>(null);

    private ProjectManager() {}

    public Project newDocument() {
        final Project doc = Project.createNew();

        openProjects.add(doc);
        currentDocument.set(doc);

        return doc;
    }

    public Project newDocument(String title) {
        final Project doc = Project.createNew(title);

        openProjects.add(doc);
        currentDocument.set(doc);

        return doc;
    }

    public void addDocument(Project doc) {
        if (doc.getFilePath().get() != null) {
            Optional<Project> existing = openProjects
                    .stream()
                    .filter(project -> doc.getFilePath().get().equals(project.getFilePath().get()))
                    .findFirst();

            if (existing.isPresent()) {
                currentDocument.set(existing.get());
                return;
            }
        }

        openProjects.add(doc);
        currentDocument.set(doc);
    }

    public boolean closeDocument(Project doc) {
        if (!openProjects.remove(doc)) {
            return false;
        }

        if (doc.equals(currentDocument.get())) {
            if (openProjects.isEmpty()) {
                currentDocument.set(null);
            } else {
                currentDocument.set(openProjects.getLast());
            }
        }

        return true;
    }

    public boolean hasUnsavedChanges() {
        return openProjects.stream().anyMatch(d -> d.getIsSaved().get());
    }

    public ObservableList<Project> getOpenDocuments() {
        return openProjects;
    }

    public Project getCurrentDocument() {
        return currentDocument.get();
    }

    public void setActiveDocument(Project doc) {
        currentDocument.set(doc);
    }

    public boolean isEmpty() {
        return openProjects.isEmpty();
    }

    public int size() {
        return openProjects.size();
    }
}
