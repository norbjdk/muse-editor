package com.muse.editor.redevelop.gui.view;

import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.event.project.CreateProjectEvent;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.util.ButtonFactory;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Viewable;
import com.muse.editor.redevelop.gui.util.SpaceFactory;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Objects;

public class CreateProjectView extends Presentable<ScrollPane> implements Viewable {
    private final static int PREVIEW_WIDTH  = 350;
    private final static int PREVIEW_HEIGHT = 525;

    private final static List<ScorePart.Name> instrumentsList = List.of(
        ScorePart.Name.Piano,
        ScorePart.Name.Violin,
        ScorePart.Name.Viola,
        ScorePart.Name.Cello,
        ScorePart.Name.Flute,
        ScorePart.Name.Trumpet
    );
    private final static List<String> keysList = List.of(
            "C-dur", "G-dur", "D-dur", "A-dur", "E-dur",
            "H-dur", "Fis-dur", "Cis-dur", "F-dur", "B-dur",
            "Es-dur", "As-dur", "Des-dur", "Ges-dur", "Ces-dur"
    );

    private final static PseudoClass ACTIVE_PSEUDO = PseudoClass.getPseudoClass("active");

    private VBox contentContainer;
    private VBox instrumentsBox;
    private HBox metricBox;
    private HBox projectButtonsContainer;
    private HBox projectButtonsWrapper;

    private TilePane columnsContainer;

    private Label pageHeaderLabel;
    private Label projectInfoLabel;
    private Label instrumentsLabel;
    private Label scorePreviewLabel;
    private Label collaboratorsLabel;
    private Label inviteLabel;
    private Label membersLabel;

    private Button createProjectBtn;
    private Button cancelProjectBtn;

    private TextField workTitleInput;
    private TextField creatorInput;
    private ComboBox<String> keyBox;
    private TextField        beatsInput;
    private TextField        beatTypeInput;
    private TextField        tempoInput;
    private TextField        measuresInput;

    private GridPane metaDataForm;
    private GridPane additionalDataForm;
    private GridPane instrumentsForm;
    private GridPane previewForm;

    private List<Label> metaDataLabels;
    private List<Label> additionalDataLabels;
    private List<Label> instrumentsLabels;

    private List<Node>      additionalDataNodes;
    private List<TextField> metaDataInputs;

    private Canvas          sheetPreview;
    private GraphicsContext graphicsContext;

    public CreateProjectView() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        contentContainer        = new VBox();
        instrumentsBox          = new VBox();
        metricBox               = new HBox();
        projectButtonsContainer = new HBox();
        projectButtonsWrapper   = new HBox();

        columnsContainer = new TilePane();

        pageHeaderLabel    = new Label("Create new composition");
        projectInfoLabel   = new Label("Project Info");
        instrumentsLabel   = new Label("Instruments");
        scorePreviewLabel  = new Label("Score Preview");
        collaboratorsLabel = new Label("Collaborators");
        inviteLabel        = new Label("Invite by email");
        membersLabel       = new Label("Team Members");

        createProjectBtn = ButtonFactory.createButton("Create", "create-btn", "Create new project", "new-project-btn");
        cancelProjectBtn = ButtonFactory.createButton("Cancel", "cancel-btn", "Cancel creation", "new-project-btn");

        workTitleInput = new TextField();
        creatorInput   = new TextField();
        beatsInput     = new TextField();
        beatTypeInput  = new TextField();
        tempoInput     = new TextField();
        measuresInput  = new TextField();

        keyBox = new ComboBox<>();

        metaDataForm       = new GridPane();
        additionalDataForm = new GridPane();
        instrumentsForm    = new GridPane();
        previewForm        = new GridPane();

        sheetPreview    = new Canvas();
        graphicsContext = sheetPreview.getGraphicsContext2D();

        metaDataLabels = List.of(
                new Label("Work Title"),
                new Label("Creator")
        );

        additionalDataLabels = List.of(
                new Label("Key"),
                new Label("Metric"),
                new Label("Tempo"),
                new Label("Measures")
        );

        metaDataInputs = List.of(
                workTitleInput,
                creatorInput
        );

        additionalDataNodes = List.of(
                keyBox,
                metricBox,
                tempoInput,
                measuresInput
        );

        instrumentsList.forEach(instrument -> {
            final Button btn = ButtonFactory.createButton(instrument.getValue(), "instrument-btn", instrument.getValue(), "instrument-btn");

            btn.setOnAction(actionEvent -> {
                handleInstrumentButtonClicked(btn);
            });

            instrumentsBox.getChildren().add(btn);
        });
    }

    @Override
    protected void setupComponents() {
        root.setFitToWidth(true);

        HBox.setHgrow(projectButtonsWrapper, Priority.ALWAYS);

        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setFillWidth(true);

        pageHeaderLabel.setMaxWidth(Double.MAX_VALUE);
        pageHeaderLabel.setAlignment(Pos.CENTER_LEFT);

        sheetPreview.setWidth(PREVIEW_WIDTH);
        sheetPreview.setHeight(PREVIEW_HEIGHT);

        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        keyBox.getItems().addAll(keysList);
        keyBox.getSelectionModel().selectFirst();
        keyBox.setDisable(true);

        beatsInput.setText("4");
        beatsInput.setDisable(true);
        beatTypeInput.setText("4");
        beatTypeInput.setDisable(true);
        tempoInput.setText("120");
        tempoInput.setDisable(true);
        measuresInput.setText("15");
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/shared.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/create-project.css")).toExternalForm()
        );

        root.getStyleClass().add("new-project-scroll");

        contentContainer.getStyleClass().add("content");
        pageHeaderLabel.getStyleClass().add("header");

        projectInfoLabel.getStyleClass().add("project-info-label");
        scorePreviewLabel.getStyleClass().add("preview-column-label");
        collaboratorsLabel.getStyleClass().add("collaborators-column-label");

        metaDataLabels.forEach(label -> label.getStyleClass().add("form-label"));
        additionalDataLabels.forEach(label -> label.getStyleClass().add("form-label"));
        instrumentsLabel.getStyleClass().add("form-label");

        metaDataInputs.forEach(input -> {
            input.getStyleClass().add("form-input");
            input.setStyle("-fx-min-width: 250;");
        });

        additionalDataNodes.forEach(node -> node.setStyle("-fx-max-width: 150"));

        beatsInput.getStyleClass().add("form-input");
        beatTypeInput.getStyleClass().add("form-input");
        tempoInput.getStyleClass().add("form-input");
        measuresInput.getStyleClass().add("form-input");

        columnsContainer.getStyleClass().add("columns-container");

        metaDataForm.getStyleClass().add("form");
        additionalDataForm.getStyleClass().add("form");
        instrumentsForm.getStyleClass().add("form");
        previewForm.getStyleClass().add("form");

        projectButtonsContainer.getStyleClass().add("buttons-container");
        projectButtonsWrapper.getStyleClass().add("buttons-wrapper");

        keyBox.getStyleClass().add("key-box");
        instrumentsBox.getStyleClass().add("instruments-box");

        addEffect(sheetPreview);
    }

    @Override
    protected void setupLayout() {
        root.setContent(contentContainer);

        metricBox.getChildren().addAll(
                beatsInput, beatTypeInput
        );

        projectButtonsWrapper.getChildren().addAll(
                cancelProjectBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.HORIZONTAL),
                createProjectBtn
        );

        projectButtonsContainer.getChildren().add(projectButtonsWrapper);

        columnsContainer.getChildren().addAll(
                projectInfoColumn(),
                previewColumn(),
                collaboratorsColumn()
        );

        contentContainer.getChildren().addAll(
                pageHeaderLabel,
                columnsContainer,
                projectButtonsContainer
        );
    }

    @Override
    protected void setupEventListeners() {
    }

    @Override
    protected void setupEventHandlers() {
        workTitleInput.setOnKeyTyped(keyEvent -> {
            handleRedrawPreview();
        });
        creatorInput.setOnKeyTyped(keyEvent -> {
            handleRedrawPreview();
        });

        createProjectBtn.setOnAction(actionEvent -> handleCreateProjectBtnClicked());
        cancelProjectBtn.setOnAction(actionEvent -> handleCancelProjectBtnClicked());

    }

    private void handleInstrumentButtonClicked(Button target) {
        boolean isActive = target.getPseudoClassStates().contains(ACTIVE_PSEUDO);
        target.pseudoClassStateChanged(ACTIVE_PSEUDO, !isActive);
    }

    private VBox previewColumn() {
        final VBox previewColumn = new VBox(20);

        previewColumn.getStyleClass().add("preview-column");

        previewForm.add(sheetPreview, 0,  0);

        previewColumn.getChildren().addAll(
                scorePreviewLabel,
                previewForm
        );

        return previewColumn;
    }

    private VBox collaboratorsColumn() {
        final VBox collaboratorsColumn = new VBox(20);

        collaboratorsColumn.getStyleClass().add("collaborators-column");

        collaboratorsColumn.getChildren().addAll(
                collaboratorsLabel
        );

        return collaboratorsColumn;
    }

    private VBox projectInfoColumn() {
        final VBox projectInfoColumn = new VBox(20);

        projectInfoColumn.getStyleClass().add("project-info-column");

        for (int box =  0; box < metaDataLabels.size(); box++)
            metaDataForm.add(
                    new VBox(
                            10, metaDataLabels.get(box), metaDataInputs.get(box)
                    ), 0, box
            );

        for (int box = 0; box < additionalDataLabels.size(); box++)
            additionalDataForm.add(
                    new VBox(
                            10, additionalDataLabels.get(box), additionalDataNodes.get(box)
                    ), 0, box
            );

        instrumentsForm.add(instrumentsLabel, 0, 0);
        instrumentsForm.add(instrumentsBox, 0, 1);

        projectInfoColumn.getChildren().addAll(
                projectInfoLabel,
                metaDataForm,
                additionalDataForm,
                instrumentsForm
        );

        return projectInfoColumn;
    }

    private void handleRedrawPreview() {
        graphicsContext.clearRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.fillRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setFill(Color.BLACK);

        final String workTitle    = workTitleInput.getText();
        final String creator = creatorInput.getText();

        if (!workTitle.isEmpty()) {
            graphicsContext.setFont(Font.font("Arial Black", 16));
            graphicsContext.fillText(workTitle, (double) PREVIEW_WIDTH / 2, 60);
        }

        if (!creator.isEmpty()) {
            graphicsContext.setFont(Font.font("Times New Roman", 14));
            graphicsContext.fillText(creator, PREVIEW_WIDTH - (creator.length() + 30), 100);
        }
    }

    private void handleCreateProjectBtnClicked() {
        final NewProjectRequest request = new NewProjectRequest();

        final String workTitle = workTitleInput.getText();
        final String creator   = creatorInput.getText();
        final int    beats     = Integer.parseInt(beatsInput.getText());
        final int    beatType  = Integer.parseInt(beatTypeInput.getText());
        final int    tempo     = Integer.parseInt(tempoInput.getText());
        final int    measures  = Integer.parseInt(measuresInput.getText());

        request.setWorkTitle(workTitle);
        request.setCreator(creator);
        request.setBeats(beats);
        request.setBeatType(beatType);
        request.setTempo(tempo);
        request.setMeasures(measures);

        for (Node button : instrumentsBox.getChildren())
            if (button.getPseudoClassStates().contains(ACTIVE_PSEUDO)) {
                request.getInstruments().add(((Button) button).getText());
            }

        EventBus.getInstance().publish(new CreateProjectEvent(request));
    }

    private void handleCancelProjectBtnClicked() {
        workTitleInput.setText("");
        creatorInput.setText("");

        EventBus.getInstance().publish(new ChangeViewEvent(Name.HOME));
    }

    private void addEffect(Node target) {
        DropShadow outerShadow = new DropShadow();
        outerShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        outerShadow.setRadius(25);
        outerShadow.setOffsetX(0);
        outerShadow.setOffsetY(10);
        outerShadow.setSpread(0.1);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.rgb(255, 255, 255, 0.6));
        innerShadow.setRadius(5);
        innerShadow.setOffsetX(-2);
        innerShadow.setOffsetY(-2);
        innerShadow.setChoke(0.8);

        target.setEffect(new Blend(
                BlendMode.SRC_OVER,
                outerShadow,
                innerShadow
        ));
    }
}
