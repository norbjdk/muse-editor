package com.muse.editor.redevelop.core.project;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.CreateProjectEvent;
import com.muse.editor.redevelop.event.project.ProjectCreatedEvent;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.model.Viewable;

import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProjectService {
    private static final ProjectService instance = new ProjectService();

    private final ProjectManager projectManager = ProjectManager.getInstance();

    public static ProjectService getInstance() {
        return instance;
    }

    private ProjectService() {
        EventBus.getInstance().subscribe(CreateProjectEvent.class, event -> {
            handleCreateProject(event.getRequest());
        });
    }

    private void handleCreateProject(final NewProjectRequest request) {
        if (request == null) return;

        CompletableFuture
                .supplyAsync(() -> projectManager.newProject(request))
                .thenAccept(project -> {
                    Platform.runLater(() -> onCreateSuccess(project));
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> onCreateFailure(throwable));
                    return null;
                });
    }

    private void onCreateSuccess(Project project) {
        final List<Part> partList = project.getScoreProperty().get().getParts();

        for (Part part : partList) {
            for (Measure measure : part.getMeasures()) {
                measure.getNotes().add(new Note.Builder()
                        .isRest(true)
                        .setDuration(2)
                        .setType(Note.Type.Whole)
                        .build());
            }
        }

        EventBus.getInstance().publish(new ProjectCreatedEvent(project.getId(), project.titleProperty().get()));
        EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
    }

    private void onCreateFailure(Throwable ex) {
        ex.printStackTrace();
    }
}
