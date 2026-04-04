package com.muse.editor.ui.view;

import com.muse.editor.core.EventBus;
import com.muse.editor.model.event.ChangeProjectPreviewEvent;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.Viewable;
import com.muse.editor.ui.util.ButtonFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
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
    private Canvas sheetPreview;
    private GraphicsContext graphicsContext;

    private GridPane dataForm;
    private GridPane templateForm;

    public NewProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        titleInput = new TextField();
        composerInput = new TextField();
        albumInput = new TextField();

        createProjectBtn = ButtonFactory.createButton("Create", "create-btn", "Create new project", "create-btn");
        sheetPreview = new Canvas();
        graphicsContext = sheetPreview.getGraphicsContext2D();

        dataForm = new GridPane();
        templateForm = new GridPane();
    }

    @Override
    public void setupComponents() {
        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.CHECK_CIRCLE, 16, Color.rgb(5, 5, 5));

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

        final Label categoryLabel = new Label("Category");
        final Label templateLabel = new Label("Template");

        categoryLabel.getStyleClass().add("template-label");
        templateLabel.getStyleClass().add("template-label");

        final ListView<String> categoryList = new ListView<>();
        final ListView<String> templatesList = new ListView<>();

        categoryList.getItems().addAll("General", "Choral", "Solo", "Band", "Orchestral");
        templatesList.getItems().addAll("Treble Clef", "Bass Clef", "Grand Staff");

        templateForm.add(categoryLabel, 0, 0);
        templateForm.add(templateLabel, 1, 0);
        templateForm.add(categoryList, 0, 1);
        templateForm.add(templatesList, 1, 1);

        getChildren().addAll(
                dataForm,
                templateForm,
                sheetPreview,
                createProjectBtn
        );
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(ChangeProjectPreviewEvent.class, event -> redrawPreview());
    }

    @Override
    public void setupEventHandlers() {
        titleInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        composerInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
        albumInput.setOnKeyTyped(keyEvent -> EventBus.getInstance().publish(new ChangeProjectPreviewEvent()));
    }

    private void redrawPreview() {
        graphicsContext.clearRect(0, 0, sheetPreview.getWidth(), sheetPreview.getHeight());
        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillRoundRect(0,0 ,300, 450, 20, 30);
        graphicsContext.setStroke(Color.rgb(5, 5, 5));
        graphicsContext.strokeRect(0, 0, 300, 450);

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
