package com.muse.editor.core.project;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.io.service.FileIOService;
import com.muse.editor.core.model.score.ScorePartwise;
import com.muse.editor.model.dto.internal.ViewRequest;
import com.muse.editor.model.event.ChangeViewRequestedEvent;
import com.muse.editor.model.event.OpenProjectRequestedEvent;
import com.muse.editor.model.event.ProjectLoadFailedEvent;
import com.muse.editor.model.event.ProjectLoadedEvent;
import com.muse.editor.ui.model.ViewName;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ProjectService {
    private static final ProjectService instance = new ProjectService();

    public static ProjectService getInstance() {
        return instance;
    }

    private Stage primaryStage;

    private ProjectService() {
        setupEventListeners();
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(OpenProjectRequestedEvent.class, event -> handleOpenProjectRequested());
    }

    private void handleOpenProjectRequested() {
        Platform.runLater(() -> {
            File file = showFileChooser();
            if (file == null) return;

            loadFile(file.toPath());
        });
    }

    private File showFileChooser() {
        final FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Music XML File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("MusicXML", "*.musicxml"),
            new FileChooser.ExtensionFilter("All files", "*.*")
        );

        return fileChooser.showOpenDialog(primaryStage);
    }

    private void loadFile(Path path) {
        CompletableFuture
                .supplyAsync(() -> FileIOService.getInstance().load(path))
                .thenAcceptAsync(score -> onLoadSuccess(path, score), Platform::runLater)
                .exceptionally(ex -> {
                    Platform.runLater(() -> onLoadFailure(ex));
                    return null;
                });
    }

    private void onLoadSuccess(Path path, ScorePartwise scorePartwise) {
        final String title = scorePartwise.getWorkTitle() != null && !scorePartwise.getWorkTitle().isBlank()
                ? scorePartwise.getWorkTitle()
                : path.getFileName().toString();

        final Project project = Project.createNew(title);

        project.getFilePath().set(path);
        project.getScorePartwise().set(scorePartwise);
        project.markSaved();

        updateStatus(project, scorePartwise);
        ProjectManager.getInstance().addDocument(project);

        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.PROJECT)));
        EventBus.getInstance().publish(new ProjectLoadedEvent(project));
    }

    private void onLoadFailure(Throwable ex) {
        final String reason = ex.getCause() != null
                ? ex.getCause().getMessage()
                : ex.getMessage();

        EventBus.getInstance().publish(new ProjectLoadFailedEvent(reason));
    }

    private void updateStatus(Project project, ScorePartwise scorePartwise) {
        if (scorePartwise.getParts() != null) return;

        final int measures = scorePartwise.getParts().stream()
                .mapToInt(p -> p.getMeasures() != null ? p.getMeasures().size() : 0)
                .sum();

        final int notes = scorePartwise.getParts().stream()
                .filter(p -> p.getMeasures() != null)
                .flatMap(p -> p.getMeasures().stream())
                .filter(m -> m.getNotes() != null)
                .mapToInt(m -> m.getNotes().size())
                .sum();

        project.getMeasureCount().set(measures);
        project.getNoteCount().set(notes);
    }
}
