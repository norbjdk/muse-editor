package com.muse.editor.ui.component;

import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.EventBus;
import com.muse.editor.core.storage.StorageApiService;
import com.muse.editor.model.dto.external.ProjectCardResponse;
import com.muse.editor.model.event.OpenCloudProjectRequestedEvent;
import com.muse.editor.model.event.OpenProjectRequestedEvent;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class ProjectTile extends HBox implements Presentable {
    private StackPane projectCoverContainer;
    private Rectangle projectCoverView;
    private Image     projectCover;
    private ObjectProperty<Image> coverProperty = new SimpleObjectProperty<>(projectCover);

    private VBox projectDataBox;

    private Label projectTitleLabel;
    private Label projectSubtitleLabel;
    private Label projectComposerLabel;

    private final ProjectCardResponse cardResponse;

    public ProjectTile(ProjectCardResponse cardResponse) {
        this.cardResponse = cardResponse;

        present();
    }

    @Override
    public void initComponents() {
        projectCoverContainer = new StackPane();
        projectCoverView      = new Rectangle();

        projectDataBox = new VBox();

        projectTitleLabel    = new Label();
        projectSubtitleLabel = new Label();
        projectComposerLabel = new Label();
    }

    @Override
    public void setupComponents() {
        projectTitleLabel.setText(cardResponse.getTitle());
        projectSubtitleLabel.setText(cardResponse.getSubtitle());
        projectComposerLabel.setText(cardResponse.getComposer());
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("project-tile");

        projectTitleLabel.getStyleClass().add("project-tile-title-label");
        projectSubtitleLabel.getStyleClass().add("project-tile-subtitle-label");
        projectComposerLabel.getStyleClass().add("project-tile-composer-label");

        projectCoverContainer.setAlignment(Pos.CENTER);
        projectCoverView.setWidth(240);
        projectCoverView.setHeight(320);
        projectCoverView.setEffect(createDropShadow());

        projectDataBox.getStyleClass().add("project-data-box");
    }

    @Override
    public void setupLayout() {
        projectCoverContainer.getChildren().add(projectCoverView);

        projectDataBox.getChildren().addAll(
            createSpacer(SpaceFactory.Direction.VERTICAL),
            projectTitleLabel,
            projectSubtitleLabel,
            projectComposerLabel
        );

        this.getChildren().addAll(
            projectCoverContainer,
            projectDataBox
        );
    }

    @Override
    public void setupEventListeners() {
        coverProperty.addListener((_, oldImg, newImg) -> {
            if (newImg == null) return;

            newImg.progressProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.doubleValue() >= 1.0) {
                    Platform.runLater(() ->
                            projectCoverView.setFill(new ImagePattern(newImg))
                    );
                }
            });

            if (newImg.getProgress() >= 1.0) {
                Platform.runLater(() ->
                        projectCoverView.setFill(new ImagePattern(newImg))
                );
            }

            newImg.errorProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    Platform.runLater(() ->
                            projectCoverView.setFill(Color.GREEN)
                    );
                }
            });
        });
        System.out.println("URL: " + cardResponse.getCover());
        coverProperty.set(new Image(cardResponse.getCover(), true));
    }

    @Override
    public void setupEventHandlers() {
        this.setOnMouseClicked(e -> handleOpen());
    }

    private void handleOpen() {
        CompletableFuture
                .supplyAsync(() -> {
                    String url = StorageApiService.getInstance().getProjectFileUrl(cardResponse.getId());
                    return downloadToTemp(url);
                })
                .thenAcceptAsync(tempFile ->
                                EventBus.getInstance().publish(new OpenCloudProjectRequestedEvent(tempFile.toPath())),
                        Platform::runLater
                )
                .exceptionally(ex -> {
                    System.err.println("Failed to open project: " + ex.getMessage());
                    return null;
                });
    }

    private File downloadToTemp(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = ApiConfig.getHttpClient().newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Download failed: " + response.code());
                }

                File temp = File.createTempFile("muse_score_", ".musicxml");
                temp.deleteOnExit();

                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(temp)) {
                    fos.write(response.body().bytes());
                }

                return temp;
            }
        } catch (Exception e) {
            throw new RuntimeException("downloadToTemp failed", e);
        }
    }

    private Effect createDropShadow() {
        final DropShadow shadow = new DropShadow();

        shadow.setColor(Color.rgb(0, 0, 0, 0.18));
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(14);
        shadow.setSpread(0.4);
        shadow.setOffsetX(0);
        shadow.setOffsetY(3);

        return shadow;
    }
}
