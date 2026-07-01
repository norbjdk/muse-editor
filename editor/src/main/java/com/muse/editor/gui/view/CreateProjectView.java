package com.muse.editor.gui.view;

import com.muse.editor.app.ClientService;
import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.core.model.message.InvitationResponse;
import com.muse.editor.core.user.UserManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.core.model.dto.NewProjectRequest;
import com.muse.editor.event.project.CollaboratorAnsweredEvent;
import com.muse.editor.event.project.CollaboratorInvitedEvent;
import com.muse.editor.event.project.CreateProjectEvent;
import com.muse.editor.event.view.ChangeViewEvent;
import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.core.model.music.ScorePart;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.model.Viewable;
import com.muse.editor.gui.util.SpaceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Objects;

public class CreateProjectView extends Presentable<ScrollPane> implements Viewable {
    private final static int PREVIEW_WIDTH  = 356;
    private final static int PREVIEW_HEIGHT = 531;

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

    private ObservableList<InvitationMessage>  collaboratorsInvited;
    private ObservableList<InvitationResponse> collaboratorsAccepted;

    private VBox contentContainer;
    private VBox instrumentsBox;
    private VBox resultsBox;
    private VBox invitationsBox;
    private VBox membersBox;
    private HBox inviteBox;
    private HBox metricBox;
    private HBox projectButtonsContainer;
    private HBox projectButtonsWrapper;

    private TilePane columnsContainer;

    private Label pageHeaderLabel;
    private Label projectInfoLabel;
    private Label instrumentsLabel;
    private Label scorePreviewLabel;
    private Label collaboratorsLabel;
    private Label resultsLabel;
    private Label invitationsLabel;
    private Label membersLabel;

    private Button createProjectBtn;
    private Button cancelProjectBtn;
    private Button inviteCollaboratorFriend;

    private TextField workTitleInput;
    private TextField creatorInput;
    private TextField collaboratorInput;
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

    private Pane            previewCanvasContainer;
    private Canvas          sheetPreview;
    private GraphicsContext graphicsContext;

    public CreateProjectView() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        collaboratorsInvited  = FXCollections.observableArrayList();
        collaboratorsAccepted = FXCollections.observableArrayList();

        contentContainer        = new VBox();
        instrumentsBox          = new VBox();
        resultsBox              = new VBox();
        invitationsBox          = new VBox();
        inviteBox               = new HBox();
        membersBox              = new VBox();
        metricBox               = new HBox();
        projectButtonsContainer = new HBox();
        projectButtonsWrapper   = new HBox();

        columnsContainer = new TilePane();

        pageHeaderLabel    = new Label("Create new composition");
        projectInfoLabel   = new Label("Project Info");
        instrumentsLabel   = new Label("Instruments");
        scorePreviewLabel  = new Label("Score Preview");
        collaboratorsLabel = new Label("Collaborators");
        resultsLabel       = new Label("Search results");
        invitationsLabel   = new Label("Pending invitations ("  + collaboratorsInvited.size() + ")");
        membersLabel       = new Label("Members (" + (collaboratorsAccepted.size() + 1) + ")");

        createProjectBtn         = ButtonFactory.createButton("Create", "create-btn", "Create new project", "new-project-btn");
        cancelProjectBtn         = ButtonFactory.createButton("Cancel", "cancel-btn", "Cancel creation", "new-project-btn");
        inviteCollaboratorFriend = ButtonFactory.createButton("Invite", "invite-btn", "Invite entered artist", "invite-btn");

        workTitleInput    = new TextField();
        creatorInput      = new TextField();
        collaboratorInput = new TextField();
        beatsInput        = new TextField();
        beatTypeInput     = new TextField();
        tempoInput        = new TextField();
        measuresInput     = new TextField();

        keyBox = new ComboBox<>();

        metaDataForm       = new GridPane();
        additionalDataForm = new GridPane();
        instrumentsForm    = new GridPane();
        previewForm        = new GridPane();

        sheetPreview           = new Canvas();
        graphicsContext        = sheetPreview.getGraphicsContext2D();
        previewCanvasContainer = new Pane(sheetPreview);

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

        ButtonFactory.addIcon(inviteCollaboratorFriend, FontAwesomeSolid.USER_PLUS, 15, Color.rgb(240, 238, 235));

        collaboratorInput.setMaxWidth(Double.MAX_VALUE);

        sheetPreview.widthProperty().bind(previewCanvasContainer.widthProperty());
        sheetPreview.heightProperty().bind(previewCanvasContainer.heightProperty());

        sheetPreview.widthProperty().addListener((obs, oldV, newV) -> handleRedrawPreview());
        sheetPreview.heightProperty().addListener((obs, oldV, newV) -> handleRedrawPreview());

        graphicsContext.clearRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        graphicsContext.setFill(Color.rgb(250, 250, 250));
        graphicsContext.fillRoundRect(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT,16, 16);
        graphicsContext.setFill(Color.BLACK);

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

        resultsLabel.getStyleClass().add("form-label");
        invitationsLabel.getStyleClass().add("form-label");
        membersLabel.getStyleClass().add("form-label");

        metaDataInputs.forEach(input -> {
            input.getStyleClass().add("form-input");
            input.setStyle("-fx-min-width: 250;");
        });

        additionalDataNodes.forEach(node -> node.setStyle("-fx-max-width: 150"));

        collaboratorInput.getStyleClass().add("collaborators-input");
        HBox.setHgrow(collaboratorInput, Priority.ALWAYS);

        beatsInput.getStyleClass().add("form-input");
        beatTypeInput.getStyleClass().add("form-input");
        tempoInput.getStyleClass().add("form-input");
        measuresInput.getStyleClass().add("form-input");

        columnsContainer.getStyleClass().add("columns-container");

        metaDataForm.getStyleClass().add("form");
        additionalDataForm.getStyleClass().add("form");
        instrumentsForm.getStyleClass().add("form");
        previewForm.getStyleClass().add("preview-form");

        inviteBox.getStyleClass().add("invite-box");
        resultsBox.getStyleClass().add("form");
        invitationsBox.getStyleClass().add("form");
        membersBox.getStyleClass().add("members-box");

        projectButtonsContainer.getStyleClass().add("buttons-container");
        projectButtonsWrapper.getStyleClass().add("buttons-wrapper");

        keyBox.getStyleClass().add("key-box");
        instrumentsBox.getStyleClass().add("instruments-box");

        addEffect(previewCanvasContainer);
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
        EventBus.getInstance().subscribe(CollaboratorInvitedEvent.class, collaboratorInvitedEvent -> {
            handleCollaboratorInvited(collaboratorInvitedEvent.getInvitationMessage());
        });

        EventBus.getInstance().subscribe(CollaboratorAnsweredEvent.class, collaboratorAnsweredEvent -> {
            handleCollaboratorAccepted(collaboratorAnsweredEvent.getInvitationResponse());
        });
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

        inviteCollaboratorFriend.setOnAction(actionEvent -> handleInviteCollaboratorBtnClicked());
    }

    private void handleInviteCollaboratorBtnClicked() {
        ClientService.getInstance().sendInvitation(collaboratorInput.getText());
    }

    private void handleInstrumentButtonClicked(Button target) {
        boolean isActive = target.getPseudoClassStates().contains(ACTIVE_PSEUDO);
        target.pseudoClassStateChanged(ACTIVE_PSEUDO, !isActive);
    }

    private void handleCollaboratorInvited(InvitationMessage invitationMessage) {
        collaboratorsInvited.add(invitationMessage);
    }

    private void handleCollaboratorAccepted(InvitationResponse response) {
        if (!response.isAccepted()) return;

        collaboratorsAccepted.add(response);
        membersBox.getChildren().add(memberCard(response.getResponder(), false));
    }

    private VBox previewColumn() {
        final VBox previewColumn = new VBox(20);
        previewColumn.getStyleClass().add("preview-column");

        ColumnConstraints col = new ColumnConstraints();
        col.setFillWidth(true);
        col.setHgrow(Priority.ALWAYS);
        previewForm.getColumnConstraints().add(col);

        RowConstraints row = new RowConstraints();
        row.setFillHeight(true);
        row.setVgrow(Priority.ALWAYS);
        previewForm.getRowConstraints().add(row);

        previewForm.add(previewCanvasContainer, 0, 0);
        GridPane.setHgrow(previewCanvasContainer, Priority.ALWAYS);
        GridPane.setVgrow(previewCanvasContainer, Priority.ALWAYS);

        VBox.setVgrow(previewForm, Priority.ALWAYS);

        previewColumn.getChildren().addAll(
                scorePreviewLabel,
                previewForm
        );

        return previewColumn;
    }

    private VBox collaboratorsColumn() {
        final VBox collaboratorsColumn = new VBox(20);

        collaboratorsColumn.getStyleClass().add("collaborators-column");

        inviteBox.getChildren().addAll(
                collaboratorInput, inviteCollaboratorFriend
        );
        resultsBox.getChildren().add(resultsLabel);
        invitationsBox.getChildren().add(invitationsLabel);
        membersBox.getChildren().add(membersLabel);

        UserManager.getInstance().currentUserProperty().addListener((observableValue, user, t1) -> {
            if (t1 != null) {
                membersBox.getChildren().add(memberCard(
                                UserManager.getInstance().currentUserProperty().get().getUsername(),
                                true
                        )
                );
            }
        });

        collaboratorsColumn.getChildren().addAll(
                collaboratorsLabel,
                inviteBox,
                invitationsBox,
                membersBox
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
                new HBox(10,
                        additionalDataForm,
                        instrumentsForm)
        );

        return projectInfoColumn;
    }

    private void handleRedrawPreview() {
        final double width  = sheetPreview.getWidth();
        final double height = sheetPreview.getHeight();

        graphicsContext.clearRect(0, 0, width, height);
        graphicsContext.setFill(Color.rgb(250, 250, 250));
        graphicsContext.fillRoundRect(0, 0, width, height, 16, 16);
        graphicsContext.setFill(Color.BLACK);

        final String workTitle = workTitleInput.getText();
        final String creator   = creatorInput.getText();

        if (!workTitle.isEmpty()) {
            graphicsContext.setFont(Font.font("Arial Black", 16));
            graphicsContext.fillText(workTitle, width / 2, 60);
        }

        if (!creator.isEmpty()) {
            graphicsContext.setFont(Font.font("Times New Roman", 14));
            graphicsContext.fillText(creator, width - (creator.length() + 30), 100);
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
        root.setVvalue(0.0);

        EventBus.getInstance().publish(new ChangeViewEvent(Name.HOME));
    }

    private void addEffect(Node target) {
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(Color.rgb(0, 0, 0, 0.13));
        shadow.setRadius(12);
        shadow.setSpread(0.35);
        shadow.setOffsetX(0);
        shadow.setOffsetY(2);

        target.setEffect(shadow);
    }

    private HBox memberCard(String username, boolean isOwner) {
        final HBox card = new HBox();
        HBox.setHgrow(card, Priority.ALWAYS);

        final Circle pictureView = new Circle();
        final Label  usernameLabel = new Label(username);
        final Label  identificationLabel = new Label("@" + username);
        final Label  roleLabel = new Label(isOwner ? "Owner" : "Editor");
        final Button removeButton = new Button();

        ButtonFactory.addIcon(removeButton, FontAwesomeSolid.USER_MINUS, 15, Color.rgb(50, 50, 50));

        usernameLabel.getStyleClass().add("form-label");
        identificationLabel.getStyleClass().add("card-id-label");
        roleLabel.getStyleClass().add("role-label");
        removeButton.setStyle("-fx-background-color: transparent");

        if (isOwner) {
            FontIcon icon = new FontIcon(FontAwesomeSolid.AWARD);
            icon.setIconSize(15);
            icon.setIconColor(Color.rgb(5, 5, 5));

            usernameLabel.setGraphic(icon);
            usernameLabel.setContentDisplay(ContentDisplay.RIGHT);
            usernameLabel.setGraphicTextGap(13);
        }

        card.getChildren().addAll(
                pictureView,
                new VBox(5, usernameLabel,identificationLabel),
                SpaceFactory.createSpacer(SpaceFactory.Direction.HORIZONTAL),
                roleLabel
        );

//        if (!isOwner) card.getChildren().add(removeButton);

        return card;
    }
}
