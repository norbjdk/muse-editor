package com.muse.editor.ui.view;

import com.muse.editor.core.collection.CollectionApiService;
import com.muse.editor.model.dto.external.ProjectCardResponse;
import com.muse.editor.ui.component.ProjectTile;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.Viewable;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class CollectionView extends ScrollPane implements Presentable, Viewable {
    private VBox contentContainer;
    private HBox infoSection;

    private TilePane cloudProjectsContainer;

    private Label pageHeaderLabel;
    private Label infoTitleLabel;
    private Label infoDescLabel;

    private List<ProjectTile> projectTiles = new ArrayList<>();

    public CollectionView() {
        present();
    }

    @Override
    public void initComponents() {
        contentContainer = new VBox();
        infoSection      = new HBox();

        cloudProjectsContainer = new TilePane();

        pageHeaderLabel = new Label();
        infoTitleLabel  = new Label();
        infoDescLabel   = new Label();


//        final ProjectCardResponse response1 = new ProjectCardResponse();
//        response1.setTitle("Arcade");
//        response1.setSubtitle("Small Town Boy");
//        response1.setComposer("Duncan Laurence");
//        response1.setCover("https://pianocoda.com/wp-content/uploads/Russian-Peasant-Girl.png");
//        final ProjectTile projectTile1 = new ProjectTile(response1);
//
//        final ProjectCardResponse response2 = new ProjectCardResponse();
//        response2.setTitle("Another Love");
//        response2.setSubtitle("Long Way Down");
//        response2.setComposer("Tom Odell");
//        response2.setCover("https://pianocoda.com/wp-content/uploads/Russian-Peasant-Girl.png");
//        final ProjectTile projectTile2 = new ProjectTile(response2);
//
//        final ProjectCardResponse response3 = new ProjectCardResponse();
//        response3.setTitle("Zombie");
//        response3.setSubtitle("No Need to Argue");
//        response3.setComposer("The Cranberries");
//        response3.setCover("https://pianocoda.com/wp-content/uploads/Russian-Peasant-Girl.png");
//        final ProjectTile projectTile3 = new ProjectTile(response3);
//
//        projectTiles.add(projectTile1);
//        projectTiles.add(projectTile2);
//        projectTiles.add(projectTile3);
    }

    @Override
    public void setupComponents() {
        pageHeaderLabel.setText("Collection");
        infoTitleLabel.setText("Your cloud projects in one place");
        infoTitleLabel.setText("""
                Here, you can see and choose any project that u contribute in.
                
                Pick desired one and start editing right now!""");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("collection-scroll");

        contentContainer.getStyleClass().add("collection-content");

        pageHeaderLabel.getStyleClass().add("collection-page-header");
        pageHeaderLabel.setMaxWidth(Double.MAX_VALUE);
        pageHeaderLabel.setAlignment(Pos.CENTER_LEFT);

        cloudProjectsContainer.getStyleClass().add("collection-projects-container");
    }

    @Override
    public void setupLayout() {
        this.setContent(contentContainer);

        cloudProjectsContainer.getChildren().addAll(projectTiles);
        if (projectTiles.size() % 2 != 0) {
            cloudProjectsContainer.getChildren().add(
                createSpacer(SpaceFactory.Direction.HORIZONTAL)
            );
        }

        contentContainer.getChildren().addAll(
                pageHeaderLabel,
                cloudProjectsContainer
        );
    }

    @Override
    public void setupEventListeners() {
        loadProjects();
    }

    private void loadProjects() {
        CompletableFuture
                .supplyAsync(() -> CollectionApiService.getInstance().getMyProjects())
                .thenAcceptAsync(this::onProjectsLoaded, Platform::runLater)
                .exceptionally(ex -> {
                    Platform.runLater(() -> onLoadFailed(ex));
                    return null;
                });
    }

    private void onProjectsLoaded(List<ProjectCardResponse> projects) {
        cloudProjectsContainer.getChildren().clear();

        projectTiles = projects.stream()
                .map(ProjectTile::new)
                .collect(Collectors.toList());

        cloudProjectsContainer.getChildren().addAll(projectTiles);

        if (projectTiles.size() % 2 != 0) {
            cloudProjectsContainer.getChildren().add(
                    createSpacer(SpaceFactory.Direction.HORIZONTAL)
            );
        }
    }

    private void onLoadFailed(Throwable ex) {
        System.err.println("Failed to load projects: " + ex.getMessage());
    }

    @Override
    public void setupEventHandlers() {

    }
}
