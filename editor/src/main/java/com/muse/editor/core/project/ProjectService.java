package com.muse.editor.core.project;

import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.core.model.music.Measure;
import com.muse.editor.core.model.music.Note;
import com.muse.editor.core.model.music.Part;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.*;
import com.muse.editor.event.view.ChangeViewEvent;
import com.muse.editor.gui.dialog.PublishDialog;
import com.muse.editor.gui.model.Viewable;

import com.muse.editor.gui.util.SnapshotUtil;
import javafx.application.Platform;
import javafx.scene.image.WritableImage;

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
        EventBus.getInstance().subscribe(LoadProjectEvent.class, loadProjectEvent -> {
            handleOpenProject(loadProjectEvent.getScorePartwise());
        });
        EventBus.getInstance().subscribe(PublishProjectEvent.class, publishProjectEvent -> {
            handlePublishProject();
        });
    }

    private void handlePublishProject() {
        PublishDialog dialog = new PublishDialog(
            ScoreManager.getInstance().scoreProperty().get().getWorkTitle(),
            ScoreManager.getInstance().scoreProperty().get().getCreator()
        );

        WritableImage myRenderedImage = SnapshotUtil.getInstance().snapSheet();
        dialog.setPreviewImage(myRenderedImage);

        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Otrzymano credentiale: " + result);
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

    private void handleOpenProject(final ScorePartwise scorePartwise) {
        if (scorePartwise == null) return;

        CompletableFuture
                .supplyAsync(() -> projectManager.openProject(scorePartwise))
                .thenAcceptAsync(project -> Platform.runLater(() -> {
                    project.titleProperty().set(scorePartwise.getWorkTitle());

                    ScoreManager.getInstance().assignScore(project.getScoreProperty().get());

                    EventBus.getInstance().publish(new ProjectOpenedEvent(
                            project.getId(),
                            project.titleProperty().get()
                    ));
                    EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
                }));
    }

    private void onCreateSuccess(Project project) {
        final List<Part> partList = project.getScoreProperty().get().getParts();

        int noteId = 0;

        for (Part part : partList) {
            for (Measure measure : part.getMeasures()) {
                measure.getNotes().add(new Note.Builder()
                        .setId(noteId++)
                        .isRest(true)
                        .setDuration(2)
                        .setType(Note.Type.Whole)
                        .build());
            }
        }

        ScoreManager.getInstance().assignScore(project.getScoreProperty().get());

        EventBus.getInstance().publish(new ProjectCreatedEvent(project.getId(), project.titleProperty().get()));
        EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
    }

    private void onCreateFailure(Throwable ex) {
        ex.printStackTrace();
    }
}
