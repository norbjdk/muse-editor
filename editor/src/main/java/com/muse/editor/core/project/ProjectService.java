package com.muse.editor.core.project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.muse.editor.app.ClientService;
import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.cloud.CloudSyncService;
import com.muse.editor.core.edit.BeatCalculator;
import com.muse.editor.core.edit.CursorModel;
import com.muse.editor.core.edit.EditorState;
import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.io.FileService;
import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.core.model.dto.ProjectRequest;
import com.muse.editor.core.model.dto.ProjectResponse;
import com.muse.editor.core.model.dto.UserResponse;
import com.muse.editor.core.model.music.Measure;
import com.muse.editor.core.model.music.Note;
import com.muse.editor.core.model.music.Part;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.*;
import com.muse.editor.event.view.ChangeViewEvent;
import com.muse.editor.event.view.ShowCollaboratorsEvent;
import com.muse.editor.gui.dialog.CollaboratorsDialog;
import com.muse.editor.gui.dialog.PublishDialog;
import com.muse.editor.gui.model.Viewable;

import com.muse.editor.gui.util.SnapshotUtil;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import okhttp3.Request;
import okhttp3.Response;

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
        EventBus.getInstance().subscribe(LoadProjectEvent.class, event -> {
            handleOpenProject(event.getScorePartwise(), event.getServerId());
        });
        EventBus.getInstance().subscribe(PublishProjectEvent.class, publishProjectEvent -> {
            handlePublishProject();
        });
        EventBus.getInstance().subscribe(CloseProjectEvent.class, closeProjectEvent -> {
            handleCloseProjectEvent();
        });
        EventBus.getInstance().subscribe(ShowCollaboratorsEvent.class, showCollaboratorsEvent -> {
            handleShowCollaborators();
        });
    }

    private void handleShowCollaborators() {
        final Project project = projectManager.currentProjectProperty().get();

        if (project.getServerId() == null) {
            Debug.fail("Project is not registered on server");
            return;
        }

        final String url =
                ApiConfig.getURL() +
                "/api/v1/projects/" +
                project.getServerId() +
                "/collaborators";

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .get()
                .build();

        try (Response response = ApiConfig.getClient().newCall(request).execute()){
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                List<UserResponse> responses = ApiConfig.getObjectMapper()
                        .readValue(responseBody, new TypeReference<List<UserResponse>>() {
                        });

                final CollaboratorsDialog dialog = new CollaboratorsDialog(responses);

                dialog.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePublishProject() {
        final Project project = projectManager.currentProjectProperty().get();

        if (project.getServerId() == null) {
            System.err.println("Project not registered on server");
            return;
        }

        Long currentUserId = null;
        try {
            final Request meRequest = new Request.Builder()
                    .url(ApiConfig.getURL() + "/api/v1/users/me")
                    .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                    .get()
                    .build();

            try (Response response = ApiConfig.getClient().newCall(meRequest).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Map<String, Object> userData = ApiConfig.getObjectMapper().readValue(body, Map.class);
                    currentUserId = ((Number) userData.get("id")).longValue();

                    TokenStorage.saveUserId(currentUserId);
                    System.out.println("Updated user ID from /me: " + currentUserId);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to get user ID: " + e.getMessage());
        }

        if (currentUserId == null) {
            currentUserId = TokenStorage.getUserId();
        }

        try {
            final Request ownerRequest = new Request.Builder()
                    .url(ApiConfig.getURL() + "/api/v1/projects/" + project.getServerId() + "/owner")
                    .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                    .get()
                    .build();

            try (Response response = ApiConfig.getClient().newCall(ownerRequest).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Map<String, Object> ownerData = ApiConfig.getObjectMapper().readValue(body, Map.class);
                    Long ownerId = ((Number) ownerData.get("ownerId")).longValue();

                    System.out.println("=== PUBLISH CHECK ===");
                    System.out.println("Owner ID: " + ownerId);
                    System.out.println("Current User ID: " + currentUserId);

                    if (!ownerId.equals(currentUserId)) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Cannot Publish");
                            alert.setHeaderText("Only the project owner can publish");
                            alert.setContentText("You are not the owner of this project.");
                            alert.showAndWait();
                        });
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to check project owner: " + e.getMessage());
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

                    final Request publishRequest = new Request.Builder()
                            .url(ApiConfig.getURL() + "/api/v1/projects/" + project.getServerId() + "/publish")
                            .post(okhttp3.RequestBody.create("{}", okhttp3.MediaType.get("application/json")))
                            .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                            .build();

                    try (Response publishResponse = ApiConfig.getClient().newCall(publishRequest).execute()) {
                        if (!publishResponse.isSuccessful()) {
                            throw new IOException("Publish failed: " + publishResponse.code());
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
                    System.out.println("ID: " + request.getCollaboratorsId());
                    Platform.runLater(() -> onCreateSuccess(project, request.getCollaboratorsId()));
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> onCreateFailure(throwable));
                    return null;
                });
    }

    private void handleOpenProject(final ScorePartwise scorePartwise, final Long serverId) {
        if (scorePartwise == null) return;

        CompletableFuture
                .supplyAsync(() -> projectManager.openProject(scorePartwise))
                .thenAcceptAsync(project -> Platform.runLater(() -> {
                    project.titleProperty().set(scorePartwise.getWorkTitle());
                    ScoreManager.getInstance().assignScore(project.getScoreProperty().get());

                    if (serverId != null) {
                        project.setServerId(serverId);
                        CloudSyncService.getInstance().attach(project);
                        ClientService.getInstance().joinSession(serverId, null);
                    }

                    EventBus.getInstance().publish(new ProjectOpenedEvent(
                            project.getId(), project.titleProperty().get()
                    ));

                    EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.PROJECT));
                }));
    }

    private void handleCloseProjectEvent() {
        CloudSyncService.getInstance().forceSave();

        CloudSyncService.getInstance().detach();

        ClientService.getInstance().leaveSession();

        ScoreManager.getInstance().reset();
        CursorModel.getInstance().reset();
        EditorState.getInstance().reset();

        projectManager.closeProject();

        EventBus.getInstance().publish(new ProjectClosedEvent());
        EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.HOME));
    }

    private void onCreateSuccess(Project project, List<Long> collaborators) {
        final List<Part> partList = project.getScoreProperty().get().getParts();

        int noteId = 0;

        for (Part part : partList) {
            for (Measure measure : part.getMeasures()) {
                measure.getNotes().add(new Note.Builder()
                        .setId(noteId++)
                        .isRest(true)
                        .setDuration(BeatCalculator.noteValue(Note.Type.Whole))
                        .setType(Note.Type.Whole)
                        .setVoice(1)
                        .build());
            }
        }

        ScoreManager.getInstance().assignScore(project.getScoreProperty().get());

        CompletableFuture.runAsync(() -> {
            try {
                final ProjectRequest req = new ProjectRequest();
                req.setTitle(project.getScoreProperty().get().getWorkTitle());
                req.setCreator(project.getScoreProperty().get().getCreator());
                req.setCollaboratorsIds(collaborators);

                Debug.check("Collaborators list size: "  + collaborators.size() + ", " +  collaborators);
                
                final ProjectResponse response = ApiBuilder.post(
                        "/api/v1/projects", ProjectResponse.class, req
                );

                if (response != null) {
                    project.setServerId(response.getId());
                    CloudSyncService.getInstance().attach(project);
                    System.out.println("Project registered on server: ID=" + response.getId());
                    CloudSyncService.getInstance().forceSave();
                    ClientService.getInstance().joinSession(response.getId(), null);
                }
            } catch (IOException e) {
                Debug.fail("Server registration failed, offline mode: " + e.getMessage());
                Debug.check("Token: " + TokenStorage.getToken());
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

    public static File saveTempFile(Project project) {
        try {
            final File temp = Files.createTempFile("muse_publish", ".musicxml").toFile();
            FileService.getInstance().save(project.getScoreProperty().get(), temp.toPath());

            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
