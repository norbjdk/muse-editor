package com.muse.editor.core.io;

import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.LoadProjectEvent;
import com.muse.editor.event.project.OpenProjectEvent;
import com.muse.editor.event.project.SaveProjectEvent;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class FileService {
    enum Chooser {
        OPEN,
        SAVE
    }
    private Stage primaryStage;

    private static final FileService instance = new FileService();

    public static FileService getInstance() {
        return instance;
    }

    private FileService() {
        EventBus.getInstance().subscribe(SaveProjectEvent.class, saveProjectEvent -> handleSaveProjectEvent());
        EventBus.getInstance().subscribe(OpenProjectEvent.class,openProjectEvent -> handleOpenProjectEvent());
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void handleOpenProjectEvent() {
        Platform.runLater(() -> {
            File file = showFileChooser(Chooser.OPEN);

            if (file == null) return;

            openFile(file.toPath());
        });
    }

    private void handleSaveProjectEvent() {
        Platform.runLater(() -> {
            File file = showFileChooser(Chooser.SAVE);

            if (file == null) return;

            saveFile(file.toPath());
        });
    }

    private void saveFile(Path path) {
        final ScorePartwise scorePartwise = ProjectManager.getInstance().scoreProperty().get();

        save(scorePartwise, path);
    }

    private void openFile(Path path) {
        try {
            final ScorePartwise scorePartwise = MXMLParser.parse(path);

            EventBus.getInstance().publish(new LoadProjectEvent(scorePartwise));

        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private File showFileChooser(Chooser chooser) {
        final FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save Music XML File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MusicXML", "*.musicxml")
        );

        return switch (chooser) {
            case OPEN -> fileChooser.showOpenDialog(primaryStage);
            case SAVE -> fileChooser.showSaveDialog(primaryStage);
        };
    }

    public void save(ScorePartwise scorePartwise, Path path) {
        try {
            MXMLWriter.write(scorePartwise, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
