package com.muse.editor.ui.view;

import com.muse.editor.core.EventBus;
import com.muse.editor.model.dto.internal.ViewRequest;
import com.muse.editor.model.event.*;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.ViewName;
import com.muse.editor.ui.model.Viewable;
import com.muse.editor.ui.util.ButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewProjectView extends FlowPane implements Presentable, Viewable {

    private TextField titleInput;
    private TextField composerInput;
    private TextField albumInput;

    private Button createProjectBtn;
    private Button cancelProjectBtn;
    private Canvas sheetPreview;
    private GraphicsContext graphicsContext;

    private GridPane dataForm;
    private GridPane templateForm;

    private ListView<String> instrumentsListView;
    private ListView<String> selectedListView;

    public NewProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        titleInput = new TextField();
        composerInput = new TextField();
        albumInput = new TextField();

        createProjectBtn = ButtonFactory.createButton("Create", "create-btn", "Create new project", "create-btn");
        cancelProjectBtn = ButtonFactory.createButton("Cancel", "cancel-btn", "Cancel new project", "create-btn");
        sheetPreview = new Canvas();
        graphicsContext = sheetPreview.getGraphicsContext2D();

        dataForm = new GridPane();
        templateForm = new GridPane();

        instrumentsListView = new ListView<>();
        selectedListView = new ListView<>();
    }

    @Override
    public void setupComponents() {
        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.CHECK_CIRCLE, 16, Color.rgb(204, 197, 185));
        ButtonFactory.addIcon(cancelProjectBtn, FontAwesomeSolid.MINUS_CIRCLE, 16, Color.rgb(204, 197, 185));

        sheetPreview.setWidth(300);
        sheetPreview.setHeight(450);

        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillRoundRect(0,0 ,300, 450, 20, 30);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRect(0, 0, 300, 450);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("new-project-view");

        dataForm.getStyleClass().add("data-form");
        templateForm.getStyleClass().add("template-form");

        dataForm.setEffect(createReflection());
        templateForm.setEffect(createReflection());
        sheetPreview.setEffect(createReflection());


    }

    @Override
    public void setupLayout() {
        final ArrayList<VBox> fieldBoxes = new ArrayList<>(List.of(
                new VBox(10, new Label("Title"), titleInput),
                new VBox(10, new Label("Album"), albumInput),
                new VBox(10, new Label("Composer"), composerInput)
        ));

        fieldBoxes.forEach( vbox -> {
            vbox.getChildren().getFirst().getStyleClass().add("data-label");
            vbox.getChildren().getLast().getStyleClass().add("data-input");
        });

        for (int vbox = 0; vbox < fieldBoxes.size(); vbox++) {
            dataForm.add(fieldBoxes.get(vbox), 0, vbox);
        }

        final Label instrumentsLabel = new Label("Choose instruments");
        final Label templateLabel = new Label("");

        instrumentsLabel.getStyleClass().add("template-label");
        templateLabel.getStyleClass().add("template-label");

        instrumentsListView.getItems().addAll("Piano", "Violin", "Guitar", "Drums", "Flutes");
        selectedListView.getItems().addAll("");

        instrumentsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) EventBus.getInstance().publish(new AddInstrumentToProjectEvent(newValue));
        });

        selectedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) EventBus.getInstance().publish(new RemoveInstrumentFromProjectEvent(newValue));
        });

        templateForm.add(instrumentsLabel, 0, 0);
        templateForm.add(templateLabel, 1, 0);
        templateForm.add(instrumentsListView, 0, 1);
        templateForm.add(selectedListView, 1, 1);

        final VBox buttonsContainer = new VBox(10, createProjectBtn, cancelProjectBtn);
        buttonsContainer.setAlignment(Pos.CENTER);

        getChildren().addAll(
                dataForm,
                templateForm,
                sheetPreview,
                buttonsContainer
        );
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(ChangeProjectPreviewEvent.class, event -> redrawPreview());
        EventBus.getInstance().subscribe(AddInstrumentToProjectEvent.class, event -> handleInstrumentSelected(event.getName()));
        EventBus.getInstance().subscribe(RemoveInstrumentFromProjectEvent.class, event -> handleInstrumentRemoved(event.getName()));
        EventBus.getInstance().subscribe(NewProjectCancelledEvent.class, event -> handleCancelledProject());
    }

    @Override
    public void setupEventHandlers() {
        titleInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        composerInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        albumInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        cancelProjectBtn.setOnAction(actionEvent -> handleCancelButton());
    }

    private void redrawPreview() {
        graphicsContext.clearRect(0, 0, sheetPreview.getWidth(), sheetPreview.getHeight());
        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillRoundRect(0,0 ,300, 450, 20, 30);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRect(0, 0, 300, 450);
        graphicsContext.setFill(Color.BLACK);

        final String title = titleInput.getText();
        final String album = albumInput.getText();
        final String author = composerInput.getText();

        if (!title.isEmpty()) {
            graphicsContext.setFont(Font.font("Arial Black", 16));
            graphicsContext.fillText(title, sheetPreview.getWidth() / 2, 60);
        }

        if (!album.isEmpty()) {
            graphicsContext.setFont(Font.font("Roboto Light", 13));
            graphicsContext.fillText(album, sheetPreview.getWidth() / 2, 80);
        }

        if (!author.isEmpty()) {
            graphicsContext.setFont(Font.font("Times New Roman", 14));
            graphicsContext.fillText(author, sheetPreview.getWidth() - 28, 100);
        }
    }

    private Reflection createReflection() {
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);
        reflection.setTopOffset(0.0);
        reflection.setTopOpacity(0.3);
        reflection.setBottomOpacity(0.0);

        return reflection;
    }

    private void handleInstrumentSelected(String instrument) {
        selectedListView.getItems().add(instrument);
        instrumentsListView.getItems().remove(instrument);
    }

    private void handleInstrumentRemoved(String instrument) {
        selectedListView.getItems().remove(instrument);
        instrumentsListView.getItems().add(instrument);
    }

    private void handleCancelledProject() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.HOME)));
    }

    private void handleCancelButton() {
        EventBus.getInstance().publish(new NewProjectCancelledEvent());
    }

    public TextField getTitleInput() {
        return titleInput;
    }

    public TextField getComposerInput() {
        return composerInput;
    }

    public TextField getAlbumInput() {
        return albumInput;
    }

    public Button getCreateProjectBtn() {
        return createProjectBtn;
    }

    public Canvas getSheetPreview() {
        return sheetPreview;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
}
