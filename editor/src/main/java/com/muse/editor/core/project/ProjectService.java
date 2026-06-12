package com.muse.editor.core.project;

import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.cloud.CloudSyncService;
import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.io.FileService;
import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.core.model.dto.ProjectRequest;
import com.muse.editor.core.model.dto.ProjectResponse;
import com.muse.editor.core.model.dto.PublishResponse;
import com.muse.editor.core.model.dto.internal.PublishData;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
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
        final Project project = projectManager.currentProjectProperty().get();

        if (project.getServerId() == null) {
            System.err.println("Project not registered on server");
            return;
        }

        PublishDialog dialog = new PublishDialog(
                ScoreManager.getInstance().scoreProperty().get().getWorkTitle(),
                ScoreManager.getInstance().scoreProperty().get().getCreator()
        );

        WritableImage scoreImg = SnapshotUtil.getInstance().snapSheet();
        dialog.setPreviewImage(scoreImg);

        dialog.showAndWait().ifPresent(result -> {
            CompletableFuture.runAsync(() -> {
                try {
                    CloudSyncService.getInstance().forceSave();
                    ApiBuilder.post(
                            "/api/v1/projects/" + project.getServerId() + "/publish",
                            ProjectResponse.class,
                            new ProjectRequest()
                    );

                    if (scoreImg != null) {
                        final File coverFile = SnapshotUtil.getInstance().saveToTempFile(scoreImg);
                        if (coverFile != null) {
                            ApiBuilder.post(
                                    "/api/v1/storage/projects/" + project.getServerId() + "/cover/upload",
                                    ProjectResponse.class,
                                    coverFile
                            );
                        }
                    }

                    Platform.runLater(() -> onPublishSuccess(project));
                } catch (IOException e) {
                    Platform.runLater(() -> onPublushFailure(e));
                }
            });
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

                    if (project.getServerId() != null) {
                        CloudSyncService.getInstance().attach(project);
                    }

                    EventBus.getInstance().publish(new ProjectOpenedEvent(
                            project.getId(), project.titleProperty().get()
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

        CompletableFuture.runAsync(() -> {
            try {
                final ProjectRequest req = new ProjectRequest();
                req.setTitle(project.getScoreProperty().get().getWorkTitle());
                req.setCreator(project.getScoreProperty().get().getCreator());

                final ProjectResponse response = ApiBuilder.post(
                        "/api/v1/projects", ProjectResponse.class, req
                );

                if (response != null) {
                    project.setServerId(response.getId());
                    CloudSyncService.getInstance().attach(project);
                    System.out.println("Project registered on server: ID=" + response.getId());
                }
            } catch (IOException e) {
                System.err.println("Server registration failed, offline mode: " + e.getMessage());
            }
        });

        EventBus.getInstance().publish(new ProjectCreatedEvent(project.getId(), project.titleProperty().get()));
        EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
    }

    private void onPublishSuccess(Project project) {

    }

    private void onPublushFailure(Throwable ex) {
        ex.printStackTrace();
    }

    private void onCreateFailure(Throwable ex) {
        ex.printStackTrace();
    }

    private File saveTempFile(Project project) {
        try {
            final File temp = Files.createTempFile("muse_publish", ".musicxml").toFile();
            FileService.getInstance().save(project.getScoreProperty().get(), temp.toPath());

            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
