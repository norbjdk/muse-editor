package com.muse.editor.gui.view;

import com.muse.editor.core.collection.CollectionService;
import com.muse.editor.core.model.dto.ProjectResponse;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.DownloadProjectEvent;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.model.Viewable;
import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.gui.util.SpaceFactory;
import com.muse.editor.util.Debug;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

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

        scoreCard.setId(String.valueOf(projectResponse.getId()));
        scoreCard.getStyleClass().add("score-card");

        final Label title   = new Label(projectResponse.getTitle());
        final Label creator = new Label(projectResponse.getComposer());

        title.getStyleClass().add("score-title-label");
        creator.getStyleClass().add("score-creator-label");

        final StackPane scoreImageContainer = new StackPane();
        final Rectangle scoreImageView      = new Rectangle();

        scoreImageView.setWidth(200);
        scoreImageView.setHeight(280);
        scoreImageContainer.getChildren().add(scoreImageView);
        scoreImageView.setFill(Color.WHITESMOKE);
        scoreImageView.setStroke(Color.BLACK);
        scoreImageView.setStrokeWidth(1);

        scoreCard.getChildren().add(scoreImageContainer);
        scoreCard.getChildren().add(title);
        scoreCard.getChildren().add(creator);

        scoreCard.setOnMouseClicked(event -> {
            EventBus.getInstance().publish(new DownloadProjectEvent(projectResponse.getId()));
        });

        return scoreCard;
    }
}
