package com.muse.editor.redevelop.core.io;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.core.model.music.ScorePartwise;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.SaveProjectEvent;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class FileService {
    private Stage primaryStage;

    private static final FileService instance = new FileService();

    public static FileService getInstance() {
        return instance;
    }

    private FileService() {
        EventBus.getInstance().subscribe(SaveProjectEvent.class, saveProjectEvent -> handleSaveProjectEvent());
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void handleSaveProjectEvent() {
        Platform.runLater(() -> {
            File file = showFileChooser();

            if (file == null) return;

            saveFile(file.toPath());
        });
    }

    private void saveFile(Path path) {
        final ScorePartwise scorePartwise = ProjectManager.getInstance().scoreProperty().get();

        save(scorePartwise, path);
    }

    private File showFileChooser() {
        final FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save Music XML File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MusicXML", "*.musicxml")
        );

        return fileChooser.showSaveDialog(primaryStage);
    }

    public void save(ScorePartwise scorePartwise, Path path) {
        try {
            MXMLWriter.write(scorePartwise, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
