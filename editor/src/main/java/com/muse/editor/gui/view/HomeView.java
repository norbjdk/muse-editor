package com.muse.editor.gui.view;

import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.gui.util.SpaceFactory;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.model.Viewable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

import static com.muse.editor.gui.util.SpaceFactory.createSpacer;

public class HomeView extends Presentable<ScrollPane> implements Viewable {
    private VBox contentContainer;
    private HBox heroSection;
    private HBox  infoSection;

    private StackPane heroImageContainer;
    private Rectangle heroImageView;
    private Label heroHeaderLabel;
    private Label     heroParagraphLabel;
    private Button createProjectBtn;

    private Label pageHeaderLabel;

    private Label overviewTitleLabel;
    private Label overviewDescLabel;
    private Label authorTitleLabel;
    private Label authorNameLabel;
    private Label authorGithubLabel;

    private Label     instructionTitleLabel;
    private Label     instructionStep1Label;
    private Label     instructionStep2Label;
    private Label     instructionStep3Label;
    private Button    openProjectBtn;

    public HomeView() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        contentContainer    = new VBox();
        heroSection         = new HBox();
        infoSection         = new HBox();

        heroImageContainer  = new StackPane();
        heroImageView       = new Rectangle();
        heroHeaderLabel     = new Label("Compose without limits");
        heroParagraphLabel  = new Label(
                "Create beautiful music sheets alone or together with your band. " +
                        "MUSE gives you all the tools you need to write, edit and share your music."
        );
        createProjectBtn    = ButtonFactory.createButton("Create Project", "home-btn-primary", "Start a new score", "home-btn-primary");

        pageHeaderLabel     = new Label("Welcome to MUSE");

        overviewTitleLabel  = new Label("Overview");
        overviewDescLabel   = new Label("""
                MUSE is an open-source music notation editor built as a diploma thesis project.

                Scores are stored in the industry-standard .musicxml format, keeping your work \
                portable and compatible with other notation tools.

                Stack: JavaFX · Spring Boot · React · PostgreSQL · TailwindCSS

                Released under the MIT License."""
        );
        authorTitleLabel    = new Label("Author");
        authorNameLabel     = new Label("Norbert Dominkiewicz");
        authorGithubLabel   = new Label("@norbjdk — github.com/norbjdk");

        instructionTitleLabel  = new Label("How to start");
        instructionStep1Label  = new Label("1. Click \"Create Project\" to start a new score.");
        instructionStep2Label  = new Label("2. Fill in the project details and choose a template.");
        instructionStep3Label  = new Label("3. Use the editor to compose, edit and export your music.");
        openProjectBtn         = ButtonFactory.createButton("Open Project", "home-btn-secondary", "Open an existing score", "home-btn-secondary");
    }

    @Override
    protected void setupComponents() {
        root.setFitToWidth(true);

        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setFillWidth(true);

        pageHeaderLabel.setMaxWidth(Double.MAX_VALUE);
        pageHeaderLabel.setAlignment(Pos.CENTER_LEFT);

        final Image heroImage = new Image(
                Objects.requireNonNull(getClass().getResource(
                        "/com/muse/editor/assets/images/logo.png")).toExternalForm()
        );
        heroImageView.setWidth(280);
        heroImageView.setHeight(280);
        heroImageView.setArcWidth(24);
        heroImageView.setArcHeight(24);
        heroImageView.setFill(new ImagePattern(heroImage));
        heroImageContainer.getChildren().add(heroImageView);

        heroParagraphLabel.setWrapText(true);
        overviewDescLabel.setWrapText(true);
        instructionStep3Label.setWrapText(true);
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/shared.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/home.css")).toExternalForm()
        );

        root.getStyleClass().add("home-scroll");

        contentContainer.getStyleClass().add("home-content");
        pageHeaderLabel.getStyleClass().add("home-page-header");

        heroSection.getStyleClass().add("home-hero-section");
        heroImageContainer.getStyleClass().add("home-hero-image-container");
        heroHeaderLabel.getStyleClass().add("home-hero-header");
        heroParagraphLabel.getStyleClass().add("home-hero-paragraph");
        createProjectBtn.getStyleClass().setAll("home-btn-primary");

        infoSection.getStyleClass().add("home-info-section");
    }

    @Override
    protected void setupLayout() {
        root.setContent(contentContainer);

        heroSection.getChildren().addAll(
                buildHeroImageColumn(),
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                buildHeroTextColumn()
        );

        infoSection.getChildren().addAll(
                buildAboutColumn(),
                buildInstructionColumn()
        );

        contentContainer.getChildren().addAll(
                pageHeaderLabel,
                heroSection,
                infoSection
        );
    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {

    }

    private StackPane buildHeroImageColumn() {
        heroImageContainer.getStyleClass().add("home-hero-image-wrapper");
        return heroImageContainer;
    }

    private VBox buildHeroTextColumn() {
        final VBox column = new VBox();
        column.getStyleClass().add("home-hero-text");
        column.getChildren().addAll(
                heroHeaderLabel,
                heroParagraphLabel,
                createProjectBtn
        );
        return column;
    }

    private VBox buildAboutColumn() {
        final VBox card = new VBox();
        card.getStyleClass().add("home-card");

        final Label sectionDivider = new Label();
        sectionDivider.getStyleClass().add("home-card-divider");

        overviewTitleLabel.getStyleClass().add("home-card-title");
        overviewDescLabel.getStyleClass().add("home-card-body");
        authorTitleLabel.getStyleClass().add("home-card-subtitle");
        authorNameLabel.getStyleClass().add("home-card-name");
        authorGithubLabel.getStyleClass().add("home-card-link");

        card.getChildren().addAll(
                overviewTitleLabel,
                overviewDescLabel,
                authorTitleLabel,
                authorNameLabel,
                authorGithubLabel
        );
        return card;
    }

    private VBox buildInstructionColumn() {
        final VBox card = new VBox();
        card.getStyleClass().add("home-card");

        instructionTitleLabel.getStyleClass().add("home-card-title");
        instructionStep1Label.getStyleClass().addAll("home-card-body", "home-step");
        instructionStep2Label.getStyleClass().addAll("home-card-body", "home-step");
        instructionStep3Label.getStyleClass().addAll("home-card-body", "home-step");
        instructionStep3Label.setWrapText(true);
        openProjectBtn.getStyleClass().setAll("home-btn-secondary");

        card.getChildren().addAll(
                instructionTitleLabel,
                instructionStep1Label,
                instructionStep2Label,
                instructionStep3Label,
                openProjectBtn
        );
        return card;
    }
}
