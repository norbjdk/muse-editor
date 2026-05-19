package com.muse.editor.core.project;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.io.service.FileIOService;
import com.muse.editor.core.storage.StorageApiService;
import com.muse.editor.model.dto.external.ProjectResponse;
import com.muse.editor.model.event.PublishProjectRequestedEvent;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class PublishService {
    private static final PublishService instance = new PublishService();

    public static PublishService getInstance() {
        return instance;
    }

    private PublishService() {
        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(PublishProjectRequestedEvent.class,
                event -> handlePublish());
    }

    private void handlePublish() {
        Project project = ProjectManager.getInstance().getCurrentDocument();

        if (project == null) {
            System.err.println("PublishService: no active project");
            return;
        }

        CompletableFuture
                .supplyAsync(() -> publish(project))
                .thenAcceptAsync(response -> onPublishSuccess(project, response), Platform::runLater)
                .exceptionally(ex -> {
                    Platform.runLater(() -> onPublishFailure(ex));
                    return null;
                });
    }

    private ProjectResponse publish(Project project) {
        // 1. Utwórz projekt w bazie → dostajemy projectId
        ProjectResponse response = ProjectApiService.getInstance().createProject(
                project.getTitle().get(),
                project.getScorePartwise().get() != null
                        ? project.getScorePartwise().get().getAlbum()
                        : null,
                project.getScorePartwise().get() != null
                        ? project.getScorePartwise().get().getCreator()
                        : null
        );

        // 2. Zapisz score do pliku tymczasowego
        File tempFile = saveTempFile(project);

        // 3. Uploaduj plik na MinIO
        StorageApiService.getInstance().uploadProjectFile(response.getId(), tempFile);

        // 4. Posprzątaj temp
        tempFile.delete();

        return response;
    }

    private File saveTempFile(Project project) {
        try {
            File temp = Files.createTempFile("muse_publish_", ".musicxml").toFile();
            FileIOService.getInstance().save(project.getScorePartwise().get(), temp.toPath());
            return temp;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save temp file", e);
        }
    }

    private void onPublishSuccess(Project project, ProjectResponse response) {
        System.out.println("Published successfully, projectId: " + response.getId());
        }

    private void onPublishFailure(Throwable ex) {
        System.err.println("Publish failed: " + ex.getMessage());
    }
}