package com.muse.editor.core.io;

import com.muse.editor.app.AppManager;
import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.*;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class FileService {
    enum Chooser {
        OPEN,
        SAVE
    }
    private final Stage primaryStage;

    private static final FileService instance = new FileService();

    public static FileService getInstance() {
        return instance;
    }

    private FileService() {
        primaryStage = AppManager.getInstance().getPrimaryStage();

        EventBus.getInstance().subscribe(SaveProjectEvent.class, saveProjectEvent -> handleSaveProjectEvent());
        EventBus.getInstance().subscribe(OpenProjectEvent.class,openProjectEvent -> handleOpenProjectEvent());
        EventBus.getInstance().subscribe(DownloadProjectEvent.class, downloadProjectEvent -> handleDownloadProjectEvent(downloadProjectEvent.getId()));
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

    private void handleDownloadProjectEvent(Long projectId) {
        new Thread(() -> {
            try {
                File file = downloadFile(projectId);

                if (file == null) return;

                Platform.runLater(() -> openFile(file.toPath()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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

    private File downloadFile(Long projectId) {
        final Map response;
        try {
            response = ApiBuilder.getAsMap(
                    "/api/v1/storage/projects/" + projectId + "/shared/get",
                    Map.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String fileUrl = (String) response.get("url");

        System.out.println("DEBUG: Wygenerowany URL z serwera: " + fileUrl);

        final Request request = new Request.Builder()
                .url(fileUrl)
                .get()
                .build();

        try (Response httpResponse = ApiConfig.getClient().newCall(request).execute()) {
            if (!httpResponse.isSuccessful()) throw new IOException("Download failed: " + httpResponse.code());

            final File temp = Files.createTempFile("muse_download_", ".musicxml").toFile();
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                fos.write(httpResponse.body().bytes());
            }

            return temp;
        } catch (IOException e) {
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
