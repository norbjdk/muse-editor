package com.muse.editor.core.cloud;

import com.muse.editor.app.AppConfig;
import com.muse.editor.app.ClientService;
import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.io.FileService;
import com.muse.editor.core.project.Project;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.gui.util.SnapshotUtil;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.scene.image.WritableImage;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CloudSyncService {
    private static final CloudSyncService instance = new CloudSyncService();
    public static CloudSyncService getInstance() { return instance; }

    private static final int AUTO_SAVE_INTERVAL_SEC = 5;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "cloud-auto-save");
                t.setDaemon(true);
                return t;
            });

    private final AtomicBoolean dirty   = new AtomicBoolean(false);
    private final AtomicBoolean syncing = new AtomicBoolean(false);

    private Project activeProject;
    private ScheduledFuture<?> autoSaveTask;

    private CloudSyncService() {}

    public void attach(Project project) {
        this.activeProject = project;
        dirty.set(false);

        if (autoSaveTask != null) autoSaveTask.cancel(false);

//        autoSaveTask = scheduler.scheduleAtFixedRate(
//                this::autoSave,
//                AUTO_SAVE_INTERVAL_SEC,
//                AUTO_SAVE_INTERVAL_SEC,
//                TimeUnit.SECONDS
//        );

        System.out.println("CloudSyncService attached to project: " + project.getServerId());
    }

    public void detach() {
        if (autoSaveTask != null) autoSaveTask.cancel(false);
        activeProject = null;
    }

    public void markDirty() {
        dirty.set(true);
    }

    public void forceSave() {
        if (activeProject == null || activeProject.getServerId() == null) return;
        CompletableFuture.runAsync(this::performUpload);
    }

    private void autoSave() {
        if (!dirty.get()) return;
        if (syncing.get()) return;
        if (activeProject == null) return;
        if (activeProject.getServerId() == null) return;
        performUpload();
    }

    private void performUpload() {
        if (!syncing.compareAndSet(false, true)) {
            System.out.println("Auto-save SKIPPED — already syncing (stuck?)");
            return;
        }

        Platform.runLater(() -> {
            try {
                final WritableImage scoreImg = SnapshotUtil.getInstance().snapSheet();
                final File coverFile = SnapshotUtil.getInstance().saveToTempFile(scoreImg);

                CompletableFuture.runAsync(() -> continueUpload(coverFile));

            } catch (Exception e) {
                Debug.fail("Failed to snapshot cover image");
                System.err.println("Snapshot error: " + e.getMessage());
                syncing.set(false);
            }
        });
    }

    private void continueUpload(File coverFile) {
        try {
            try {
                final String coverEndpoint = "/api/v1/storage/projects/"
                        + activeProject.getServerId() + "/shared/cover";

                ApiBuilder.put(coverEndpoint, null, coverFile, "image/png");
                Debug.pass("Updated projects cover image");

                coverFile.delete();
            } catch (IOException e) {
                Debug.fail("Failed to update projects cover image");
                System.err.println("Cover upload error: " + e.getMessage());
            }

            try {
                final File tempFile = saveTempFile();
                final byte[] bytes = Files.readAllBytes(tempFile.toPath());

                final String url = ApiConfig.getURL() + "/api/v1/storage/projects/"
                        + activeProject.getServerId() + "/shared";

                final Request request = new Request.Builder()
                        .url(url)
                        .put(RequestBody.create(bytes, MediaType.get("application/xml")))
                        .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                        .build();

                try (Response response = ApiConfig.getClient().newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        dirty.set(false);
                        System.out.println("Auto-save OK: project " + activeProject.getServerId());
                        broadcastToSession();
                    } else {
                        System.err.println("Auto-save failed: HTTP " + response.code());
                    }
                }

                tempFile.delete();
            } catch (IOException e) {
                System.err.println("Auto-save error: " + e.getMessage());
            }

        } finally {
            syncing.set(false);
        }
    }

    private void broadcastToSession() {
        final Long sessionId = ClientService.getInstance().getCurrentSessionId();
        if (sessionId == null) return;

        try {
            final String url = AppConfig.serverUrlProperty().get()
                    + "/api/v1/sessions/"
                    + sessionId
                    + "/broadcast";

            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("{}", MediaType.get("application/json")))
                    .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                    .build();

            try (Response response = ApiConfig.getClient().newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Broadcast sent to session " + sessionId);
                } else {
                    System.err.println("Broadcast failed: HTTP " + response.code());
                }
            }
        } catch (IOException e) {
            System.err.println("Broadcast error: " + e.getMessage());
        }
    }

    private File saveTempFile() throws IOException {
        final File temp = Files.createTempFile("muse_autosave_", ".musicxml").toFile();
        FileService.getInstance().save(
                activeProject.getScoreProperty().get(),
                temp.toPath()
        );
        return temp;
    }
}
