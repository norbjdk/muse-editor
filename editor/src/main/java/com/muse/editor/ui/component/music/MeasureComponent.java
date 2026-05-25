package com.muse.editor.ui.component.music;

import com.muse.editor.core.model.score.Attributes;
import com.muse.editor.core.model.score.Measure;
import com.muse.editor.core.model.score.Note;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MeasureComponent extends Pane {
    private static final Set<Character> stepSet   = Set.of('C', 'D', 'E', 'F', 'G', 'A');
    private static final Set<Integer>   octaveSet = Set.of(4, 5);

    private final DoubleProperty measureWidth = new SimpleDoubleProperty(MusicMetrics.BASE_MEASURE_WIDTH);

    private final List<SpaceComponent> spaceComponents;
    private final List<LineComponent>  lineComponents;
    private final List<StaffComponent> staffComponents;

    private boolean isMeasureAssigned = false;
    private Measure measure;
    private final ObjectProperty<Measure> measureProperty = new SimpleObjectProperty<>(measure);

    public MeasureComponent() {
        lineComponents = List.of(
                new LineComponent(5, 'A'),
                new LineComponent(5, 'F'),
                new LineComponent(5, 'D'),
                new LineComponent(4, 'B'),
                new LineComponent(4, 'G'),
                new LineComponent(4, 'E'),
                new LineComponent(4, 'C'),
                new LineComponent(3, 'A')

        );

        spaceComponents = List.of(
                new SpaceComponent(5, 'B'),
                new SpaceComponent(5, 'G'),
                new SpaceComponent(5, 'E'),
                new SpaceComponent(5, 'C'),
                new SpaceComponent(4, 'A'),
                new SpaceComponent(4, 'F'),
                new SpaceComponent(4, 'D'),
                new SpaceComponent(3, 'B')
        );

        staffComponents = new ArrayList<>();
        staffComponents.addAll(lineComponents);
        staffComponents.addAll(spaceComponents);

        double y = 0;
        int spaceIdx = 0;
        int lineIdx = 0;

        for (int i = 0; i < MusicMetrics.TOTAL_STAFF_ELEMENTS; i++) {
            if (i % 2 == 0) {
                SpaceComponent space = spaceComponents.get(spaceIdx++);
                space.bindWidth(measureWidth);
                space.setPosition(y);
                y += MusicMetrics.BLANK_SPACE_HEIGHT;
            } else {
                LineComponent line = lineComponents.get(lineIdx++);
                line.bindWidth(measureWidth);
                line.setPosition(y);
                y += MusicMetrics.LINE_HEIGHT;
            }
        }

        this.setMinHeight(computeMinHeight());
        this.prefWidthProperty().bind(measureWidth);
        this.setPadding(new Insets(0, 10, 0, 10));
        this.setStyle("-fx-background-color: transparent");

        this.getChildren().addAll(lineComponents);
        this.getChildren().addAll(spaceComponents);
        this.getChildren().addAll(
                buildBarLine(),
                buildRightBarLine()
        );

        measureProperty.addListener((obs, oldMeasure, newMeasure) -> {
            if (newMeasure != null && newMeasure.getAttributes() != null) {
                this.getChildren().add(buildClefComponent());
                this.getChildren().add(buildMetreComponent());
            }
            if (newMeasure != null && !newMeasure.getNotes().isEmpty() ) {
                this.getChildren().addAll(buildNotes());
            }
            this.recomputeWidth();
        });
    }

    public void assignMeasure(final Measure measure) {
        if (measure == null && !isMeasureAssigned) return;

        this.measure = measure;
        this.measureProperty.set(measure);
        this.isMeasureAssigned = true;

        LayoutManager.getInstance().register(this);
    }

    public Measure getMeasureProperty() {
        return measureProperty.get();
    }

    public Measure getMeasure() {
        return measure;
    }

    public DoubleProperty measureWidthProperty() {
        return measureWidth;
    }

    private ClefComponent buildClefComponent() {
        if (measure == null)                 return null;
        if (measure.getAttributes() == null) return null;

        final Attributes    attributes    = measure.getAttributes();
        final ClefComponent clefComponent = new ClefComponent(attributes.getFifths());

        if (attributes.getLine() == 2) {
            clefComponent.setLayoutY(lineComponents.getFirst().getY());
            clefComponent.setLayoutX(5);
        }

        return clefComponent;
    }

    private MetreComponent buildMetreComponent() {
        if (measure == null)                 return null;
        if (measure.getAttributes() == null) return null;

        final Attributes     attributes     = measure.getAttributes();
        final MetreComponent metreComponent = new MetreComponent(
                attributes.getBeats(),
                attributes.getBeatType()
        );

        metreComponent.setLayoutY(lineComponents.getFirst().getY());
        metreComponent.setLayoutX(35);

        return metreComponent;
    }

    private List<NoteComponent> buildNotes() {
        if (
            measure == null
        ) return List.of();

        if (
            measure.getNotes() == null
            ||
            measure.getNotes().isEmpty()
        ) return List.of();

        final List<NoteComponent> noteComponents = new ArrayList<>();
        double xOffset = measure.getAttributes() == null ? 0 : MusicMetrics.CLEF_CANVAS_WIDTH + 30;

        for (Note note : measure.getNotes()) {
            StaffComponent staffComponent = evaluateStaffPlace(note);

            NoteComponent noteComponent = new NoteComponent(note);
            noteComponent.setLayoutY(staffComponent.getY() + 5);
            noteComponent.setLayoutX(xOffset);


            noteComponents.add(noteComponent);
            xOffset += MusicMetrics.NOTE_CANVAS_WIDTH + 5;
        }

        return noteComponents;
    }

    private StaffComponent evaluateStaffPlace(final Note note) {
        if (note.isRest()) {
            return lineComponents.get(3);
        }

        char noteStep   = note.getStep();
        int  noteOctave = note.getOctave();
        
        for (StaffComponent component : staffComponents) {
            if (component.getStep() == noteStep && component.getOctave() == noteOctave) {
                return component;
            }
        }

        throw new IllegalArgumentException("No staff place for: " + noteStep + noteOctave);
    }

    private Line buildBarLine() {
        final Line barline = new Line(
                0,
                MusicMetrics.MEASURE_TOP_Y,
                0,
                MusicMetrics.MEASURE_BOTTOM_Y
        );

        barline.setStroke(Color.BLACK);
        barline.setStrokeWidth(MusicMetrics.BAR_LINE_STROKE);
        barline.setMouseTransparent(true);

        return barline;
    }

    private Line buildRightBarLine() {
        final Line barline = new Line();

        barline.startXProperty().bind(measureWidth);
        barline.endXProperty().bind(measureWidth);
        barline.setStartY(MusicMetrics.MEASURE_TOP_Y);
        barline.setEndY(MusicMetrics.MEASURE_BOTTOM_Y);
        barline.setStroke(Color.BLACK);
        barline.setStrokeWidth(MusicMetrics.BAR_LINE_STROKE);
        barline.setMouseTransparent(true);

        return barline;
    }

    private void recomputeWidth() {
        double maxX = 0;
        for (var child : this.getChildren()) {
            double rightEdge = child.getLayoutX() + child.getBoundsInLocal().getWidth();
            maxX = Math.max(maxX, rightEdge);
        }

        final double required = Math.max(MusicMetrics.BASE_MEASURE_WIDTH, maxX + 20);

        measureWidth.set(required);
    }

    private double computeMinHeight() {
        return
            MusicMetrics.TOTAL_LINES_NUMBER
            *
            MusicMetrics.LINE_HEIGHT
            +
            MusicMetrics.TOTAL_BLANK_SPACES_NUMBER
            *
            MusicMetrics.BLANK_SPACE_HEIGHT;
    }
}
