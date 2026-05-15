package com.muse.editor.ui.component.music;

import com.muse.editor.core.model.score.Measure;
import com.muse.editor.core.model.score.Note;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.FontFactory;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MeasurePane extends StackPane implements Presentable {
    private final Measure measure;
    private final boolean drawAttributes;

    private StaffPane staffPane;
    private HBox contentContainer;
    private HBox attributesContainer;
    private HBox notesContainer;
    private ClefComponent clefComponent;
    private MetreComponent metreComponent;

    private List<NoteComponent> noteComponents;

    private ObjectProperty<Integer> notesCounter;

    public MeasurePane(final Measure measure, boolean drawAttributes) {
        this.measure = measure;
        this.drawAttributes = drawAttributes;

        this.notesCounter = new SimpleObjectProperty<>(measure.getNotes().size());

        present();
    }

    @Override
    public void initComponents() {
        staffPane = new StaffPane();

        contentContainer = new HBox();
        attributesContainer = new HBox();
        notesContainer = new HBox();

        if (measure.getAttributes() != null) {
            clefComponent = new ClefComponent(measure.getAttributes().getFifths());
            metreComponent = new MetreComponent(measure.getAttributes().getBeats(), measure.getAttributes().getBeatType());
        }
    }

    @Override
    public void setupComponents() {
        attributesContainer.setMouseTransparent(true);
        notesContainer.setMouseTransparent(true);
    }

    @Override
    public void setupStyle() {
        this.setAlignment(Pos.CENTER);

        int noteWidth = 35;
        int baseNotesCountForWidth = 4;

        if (measure.getNotes().size() > 5) {
            int toAddWidth = (measure.getNotes().size() - baseNotesCountForWidth) * noteWidth;
            this.setPrefWidth(staffPane.getBASE_WIDTH() + toAddWidth);
            staffPane.setStaffWidth(staffPane.getBASE_WIDTH() + toAddWidth);
        }

        notesContainer.setSpacing(10);
        notesContainer.setAlignment(Pos.CENTER);

//        this.setStyle("-fx-background-color: #00bbff");
    }

    @Override
    public void setupLayout() {
        if (measure.getAttributes() != null) {
            attributesContainer.getChildren().addAll(
                    clefComponent,
                    metreComponent
            );
        }
        this.getChildren().add(staffPane);

        if (drawAttributes) contentContainer.getChildren().add(attributesContainer);
        this.contentContainer.getChildren().add(notesContainer);

        this.getChildren().add(contentContainer);

        drawNotes();
    }

    @Override
    public void setupEventListeners() {
        notesCounter.addListener((obs, oldV, newV) -> {
            double baseWidth = 150.0;
            double perNoteWidth = 40.0;
            staffPane.setStaffWidth(baseWidth + (newV * perNoteWidth));
        });
    }

    @Override
    public void setupEventHandlers() {
        staffPane.setOnSlotClicked(slot -> System.out.println("Clicked on: " + slot + " slot"));
    }

    public void drawNotes() {
        Platform.runLater(() -> {
            for (Note note : measure.getNotes()) {
                NoteComponent noteComponent = new NoteComponent(note);

                this.notesContainer.getChildren().add(noteComponent);
            }
        });
    }
}
