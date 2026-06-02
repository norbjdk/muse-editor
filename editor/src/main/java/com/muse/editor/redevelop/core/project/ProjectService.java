package com.muse.editor.redevelop.core.project;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.CreateProjectEvent;
import com.muse.editor.redevelop.event.project.ProjectCreatedEvent;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.model.Viewable;

import javafx.application.Platform;

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
                    Platform.runLater(() -> onCreateSuccess());
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> onCreateFailure(throwable));
                    return null;
                });
    }

    private void onCreateSuccess() {
        EventBus.getInstance().publish(new ProjectCreatedEvent());
        EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
    }

    private void onCreateFailure(Throwable ex) {
        ex.printStackTrace();
    }
}
