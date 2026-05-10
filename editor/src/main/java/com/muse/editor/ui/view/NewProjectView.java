package com.muse.editor.ui.view;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.edit.Instrument;
import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.core.model.score.Attributes;
import com.muse.editor.core.user.UserService;
import com.muse.editor.model.dto.internal.ViewRequest;
import com.muse.editor.model.event.ChangeProjectPreviewEvent;
import com.muse.editor.model.event.ChangeViewRequestedEvent;
import com.muse.editor.model.event.CreateProjectRequestedEvent;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.ViewName;
import com.muse.editor.ui.model.Viewable;

import com.muse.editor.ui.util.ButtonFactory;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;
import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;


public class NewProjectView extends VBox implements Presentable, Viewable {
    private final static int PREVIEW_WIDTH        = 300;
    private final static int PREVIEW_HEIGHT       = 450;
    private final static List<Instrument> instrumentsList = List.of(
            new Instrument(Instrument.Name.Violin),
            new Instrument(Instrument.Name.Viola),
            new Instrument(Instrument.Name.Cello)
    );
    private final static List<String> keysList = List.of(
            "C-dur", "G-dur", "D-dur", "A-dur", "E-dur",
            "H-dur", "Fis-dur", "Cis-dur", "F-dur", "B-dur",
            "Es-dur", "As-dur", "Des-dur", "Ges-dur", "Ces-dur"
    );
    private final static PseudoClass ACTIVE_PSEUDO = PseudoClass.getPseudoClass("active");

    private FlowPane contentContainer;

    private Label viewHeaderLabel;
    private Label instrumentsLabel;

    private List<Label> metaDataLabels;
    private List<Label> additionalDataLabels;
    private List<Node>  additionalDataNodes;

    private TextField titleInput;
    private TextField composerInput;
    private TextField subtitleInput;
    private TextField copyrightInput;

    private List<TextField> metaDataInputs;

    private Button createProjectBtn;
    private Button cancelProjectBtn;

    private GridPane metaDataForm;
    private GridPane additionalDataForm;
    private GridPane instrumentsForm;

    private VBox                  instrumentsBox;
    private HBox                  projectButtonsContainer;

    private Canvas           sheetPreview;
    private GraphicsContext  graphicsContext;

    private ComboBox<String> keyBox;
    private HBox             metricBox;
    private TextField        beatsInput;
    private TextField        beatTypeInput;
    private TextField        tempoInput;
    private TextField        measuresInput;

    public NewProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        contentContainer = new FlowPane();

        viewHeaderLabel  = new Label("Create new composition");
        instrumentsLabel = new Label("Choose Instruments");

        titleInput     = new TextField();
        composerInput  = new TextField();
        subtitleInput  = new TextField();
        copyrightInput = new TextField();

        createProjectBtn = ButtonFactory.createButton("Create", "create-btn", "Create new project", "new-project-btn");
        cancelProjectBtn = ButtonFactory.createButton("Cancel", "cancel-btn", "Cancel creation", "new-project-btn");

        metaDataForm       = new GridPane();
        additionalDataForm = new GridPane();
        instrumentsForm    = new GridPane();

        instrumentsBox = new VBox();
        projectButtonsContainer = new HBox();

        sheetPreview    = new Canvas();
        graphicsContext = sheetPreview.getGraphicsContext2D();

        keyBox        = new ComboBox<>();
        metricBox     = new HBox();
        beatsInput    = new TextField();
        beatTypeInput = new TextField();
        tempoInput    = new TextField();
        measuresInput = new TextField();

        metaDataLabels = List.of(
                new Label("Title"),
                new Label("Subtitle"),
                new Label("Composer"),
                new Label("Copyright")
        );

        additionalDataLabels = List.of(
                new Label("Key"),
                new Label("Metric"),
                new Label("Tempo"),
                new Label("Measures")
        );

        additionalDataNodes = List.of(
                keyBox,
                metricBox,
                tempoInput,
                measuresInput
        );

        metaDataInputs = List.of(
                titleInput,
                subtitleInput,
                composerInput,
                copyrightInput
        );

        instrumentsList.forEach(instrument -> {
            final Button btn = ButtonFactory.createButton(instrument.getName().getName(), "instrument-btn", instrument.getName().getName(), "instrument-btn");

            btn.setOnAction(actionEvent -> {
                handleInstrumentButtonClicked(btn);
            });

            instrumentsBox.getChildren().add(btn);
        });
    }

    @Override
    public void setupComponents() {
        titleInput.setPromptText("New composition...");
        subtitleInput.setPromptText("Unknown");
        composerInput.setPromptText(UserService.getInstance().getCurrentUser() != null ? UserService.getInstance().getCurrentUser().getUsername() : "Guest");
        copyrightInput.setPromptText("...");

        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.CHECK_CIRCLE, 15, Color.rgb(204, 197, 185));
        ButtonFactory.addIcon(cancelProjectBtn, FontAwesomeSolid.MINUS_CIRCLE, 15, Color.rgb(204, 197, 185));

        sheetPreview.setWidth(PREVIEW_WIDTH);
        sheetPreview.setHeight(PREVIEW_HEIGHT);

        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillRoundRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, 20, 30);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRoundRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, 20, 30);

        keyBox.getItems().addAll(keysList);
        keyBox.getSelectionModel().selectFirst();

        beatsInput.setText("4");
        beatTypeInput.setText("4");
        tempoInput.setText("120");
        measuresInput.setText("15");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("new-project-view");

        contentContainer.getStyleClass().add("content-container");

        projectButtonsContainer.getStyleClass().add("project-buttons-container");

        viewHeaderLabel.getStyleClass().add("new-project-page-header");

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

        metaDataForm.getStyleClass().add("project-form");
        additionalDataForm.getStyleClass().add("project-form");
        instrumentsForm.getStyleClass().add("project-form");

        keyBox.getStyleClass().add("key-box");
        instrumentsBox.getStyleClass().add("instruments-box");

        addReflection(sheetPreview);
    }

    @Override
    public void setupLayout() {
        metricBox.getChildren().addAll(
                beatsInput, beatTypeInput
        );

        projectButtonsContainer.getChildren().addAll(
                cancelProjectBtn, createProjectBtn
        );

        for (int box = 0; box < metaDataLabels.size(); box++) {
            metaDataForm.add(
                    new VBox(10, metaDataLabels.get(box), metaDataInputs.get(box)),
                    0,
                    box);
        }

        for (int box = 0; box < additionalDataLabels.size(); box++) {
            additionalDataForm.add(
                    new VBox(10, additionalDataLabels.get(box), additionalDataNodes.get(box)),
                    0,
                    box
            );
        }

        instrumentsForm.add(instrumentsLabel, 0, 0);
        instrumentsForm.add(instrumentsBox, 0, 1);

        contentContainer.getChildren().addAll(
                metaDataForm,
                additionalDataForm,
                instrumentsForm,
                sheetPreview,
                projectButtonsContainer
        );

        this.getChildren().addAll(
                viewHeaderLabel,
                createSpacer(SpaceFactory.Direction.VERTICAL),
                contentContainer,
                createSpacer(SpaceFactory.Direction.VERTICAL),
                new HBox(createSpacer(SpaceFactory.Direction.HORIZONTAL), projectButtonsContainer),
                createSpacer(SpaceFactory.Direction.VERTICAL)
        );
    }

    @Override
    public void setupEventListeners() {
        titleInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        subtitleInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        composerInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        createProjectBtn.setOnAction(actionEvent -> handleCreateProjectBtnClicked());
        cancelProjectBtn.setOnAction(actionEvent -> handleCancelProjectBtnClicked());
    }

    @Override
    public void setupEventHandlers() {
        EventBus.getInstance().subscribe(ChangeProjectPreviewEvent.class, event -> handleRedrawPreview());
    }

    private void handleRedrawPreview() {
        graphicsContext.clearRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.fillRoundRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, 20, 30);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRoundRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, 20, 30);
        graphicsContext.setFill(Color.BLACK);

        final String title    = titleInput.getText();
        final String subtitle = subtitleInput.getText();
        final String composer = composerInput.getText();

        if (!title.isEmpty()) {
            graphicsContext.setFont(Font.font("Arial Black", 16));
            graphicsContext.fillText(title, (double) PREVIEW_WIDTH / 2, 60);
        }

        if (!subtitle.isEmpty()) {
            graphicsContext.setFont(Font.font("Roboto Light", 13));
            graphicsContext.fillText(subtitle, (double) PREVIEW_WIDTH / 2, 80);
        }

        if (!composer.isEmpty()) {
            graphicsContext.setFont(Font.font("Times New Roman", 14));
            graphicsContext.fillText(composer, PREVIEW_WIDTH - (composer.length() + 30), 100);
        }
    }

    private void handleInstrumentButtonClicked(Button target) {
        boolean isCurrentlyActive = target.getPseudoClassStates().contains(ACTIVE_PSEUDO);
        target.pseudoClassStateChanged(ACTIVE_PSEUDO, !isCurrentlyActive);
    }

    private void handleCreateProjectBtnClicked() {
        final NewProjectRequest request = new NewProjectRequest();

        final String title    = titleInput.getText();
        final String subtitle = subtitleInput.getText();
        final String composer = composerInput.getText();
        final int    beats    = Integer.parseInt(beatsInput.getText());
        final int    beatType = Integer.parseInt(beatTypeInput.getText());
        final int    tempo    = Integer.parseInt(tempoInput.getText());
        final int    measures = Integer.parseInt(measuresInput.getText());

        request.setTitle(title);
        request.setSubtitle(subtitle);
        request.setComposer(composer);
        request.setBeats(beats);
        request.setBeatType(beatType);
        request.setTempo(tempo);
        request.setMeasures(measures);

        for (Node button : instrumentsBox.getChildren())
            if (button.getPseudoClassStates().contains(ACTIVE_PSEUDO)) {
                request.getInstruments().add(((Button) button).getText());
            }

        EventBus.getInstance().publish(new CreateProjectRequestedEvent(request));
    }

    private void handleCancelProjectBtnClicked() {
        titleInput.setText("");
        subtitleInput.setText("");
        composerInput.setText("");

        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.HOME)));
    }

    private void addReflection(Node target) {
        final Reflection reflection = new Reflection();

        reflection.setFraction(0.7);
        reflection.setTopOffset(0);
        reflection.setTopOpacity(0.3);
        reflection.setBottomOpacity(0);

        target.setEffect(reflection);
    }
}
