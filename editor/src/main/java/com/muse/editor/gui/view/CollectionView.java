package com.muse.editor.gui.view;

import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.collection.CollectionService;
import com.muse.editor.core.model.dto.ProjectResponse;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.DownloadProjectEvent;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.model.Viewable;
import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.gui.util.SpaceFactory;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.muse.editor.gui.util.SpaceFactory.createSpacer;

public class CollectionView extends Presentable<ScrollPane> implements Viewable {
    private VBox     contentContainer;
    private TilePane scoresContainer;

    private Label pageHeaderLabel;

    public CollectionView() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        contentContainer = new VBox();
        scoresContainer  = new TilePane();

        pageHeaderLabel = new Label("Your collection");
    }

    @Override
    protected void setupComponents() {
        root.setFitToWidth(true);

        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setFillWidth(true);

        pageHeaderLabel.setMaxWidth(Double.MAX_VALUE);
        pageHeaderLabel.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/shared.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/collection.css")).toExternalForm()
        );

        root.getStyleClass().add("collection-scroll");

        contentContainer.getStyleClass().add("collection-content");
        pageHeaderLabel.getStyleClass().add("collection-page-header");

        scoresContainer.getStyleClass().add("scores-container");
    }

    @Override
    protected void setupLayout() {
        root.setContent(contentContainer);

        contentContainer.getChildren().addAll(
                pageHeaderLabel,
                scoresContainer
        );
    }

    @Override
    protected void setupEventListeners() {
        CollectionService.getInstance().getProjectResponses().addListener((ListChangeListener<? super ProjectResponse>) observable -> {
            while (observable.next()) {
                scoresContainer.getChildren().clear();
                for (ProjectResponse response : CollectionService.getInstance().getProjectResponses()) {
                    scoresContainer.getChildren().add(buildScoreCard(response));
                }
            }
        });
    }

    @Override
    protected void setupEventHandlers() {

    }

    private VBox buildScoreCard(ProjectResponse projectResponse) {
        final VBox scoreCard = new VBox(8);

        Debug.check("Composer", projectResponse.getComposer());
        Debug.check("Cover", projectResponse.getCoverPath() != null ? projectResponse.getCoverPath() : "null");

        scoreCard.setId(String.valueOf(projectResponse.getId()));
        scoreCard.getStyleClass().add("score-card");

        final Label title   = new Label(projectResponse.getTitle());
        final Label creator = new Label(projectResponse.getComposer());

        title.getStyleClass().add("score-title-label");
        creator.getStyleClass().add("score-creator-label");

        final ImageView previewImageView = new ImageView();

        previewImageView.setFitWidth(300);
        previewImageView.setFitHeight(400);
        previewImageView.setPreserveRatio(true);
        previewImageView.setSmooth(true);

        CompletableFuture.supplyAsync(() -> downloadCoverImage(projectResponse.getId()))
                .thenAccept(image -> Platform.runLater(() -> previewImageView.setImage(image)))
                .exceptionally(ex -> {
                    System.err.println("Failed to load cover: " + ex.getMessage());
                    return null;
                });

        VBox imageContainer = new VBox();
        imageContainer.getStyleClass().add("publish-preview-container");
        imageContainer.getChildren().addAll(previewImageView);

        scoreCard.getChildren().add(imageContainer);
        scoreCard.getChildren().add(title);
        scoreCard.getChildren().add(creator);

        scoreCard.setOnMouseClicked(event -> {
            EventBus.getInstance().publish(new DownloadProjectEvent(projectResponse.getId()));
        });

        return scoreCard;
    }

    private Image downloadCoverImage(Long projectId) {
        try {
            final String getUrlEndpoint = ApiConfig.getURL()
                    + "/api/v1/storage/projects/"
                    + projectId
                    + "/shared/cover/get";

            final Request metaRequest = new Request.Builder()
                    .url(getUrlEndpoint)
                    .get()
                    .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                    .build();

            final String coverUrl;

            try (Response metaResponse = ApiConfig.getClient().newCall(metaRequest).execute()) {
                if (!metaResponse.isSuccessful()) {
                    throw new IOException("Failed to fetch cover URL, code: " + metaResponse.code());
                }

                final String body = metaResponse.body().string();
                System.out.println("DEBUG: raw response from /shared/cover/get: " + body);

                final Map<?, ?> parsed = ApiConfig.getObjectMapper().readValue(body, Map.class);
                coverUrl = (String) parsed.get("url");
            }

            if (coverUrl == null) {
                throw new IOException("Cover URL was null in response");
            }

            System.out.println("DEBUG: Wygenerowany URL okładki z serwera: " + coverUrl);

            final Request fileRequest = new Request.Builder()
                    .url(coverUrl)
                    .get()
                    .build();

            try (Response fileResponse = ApiConfig.getClient().newCall(fileRequest).execute()) {
                if (!fileResponse.isSuccessful()) {
                    throw new IOException("Cover download failed: " + fileResponse.code());
                }
                return new Image(fileResponse.body().byteStream());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
