package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Attributes;
import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.gui.manager.LayoutManager;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Staffable;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MeasureComponent extends Presentable<Pane> {
    private static final Set<Character> stepSet   = Set.of('C', 'D', 'E', 'F', 'G', 'A');
    private static final Set<Integer>   octaveSet = Set.of(4, 5);

    private DoubleProperty          measureWidth;
    private ObjectProperty<Measure> measureProperty;
    private List<Staffable<?>>      staffComponents;

    public MeasureComponent() {
        super(new Pane());
    }

    @Override
    protected void initComponents() {
        measureWidth    = new SimpleDoubleProperty(MusicMetrics.BASE_MEASURE_WIDTH);
        measureProperty = new SimpleObjectProperty<>(null);

        staffComponents = List.of(
                new LineComponent(3, 'A'),
                new SpaceComponent(3, 'B'),
                new LineComponent(4, 'C'),
                new SpaceComponent(4, 'D'),
                new LineComponent(4, 'E'),
                new SpaceComponent(4, 'F'),
                new LineComponent(4, 'G'),
                new SpaceComponent(4, 'A'),
                new LineComponent(4, 'B'),
                new SpaceComponent(5, 'C'),
                new LineComponent(5, 'D'),
                new SpaceComponent(5, 'E'),
                new LineComponent(5, 'F'),
                new SpaceComponent(5, 'G'),
                new LineComponent(5, 'A'),
                new SpaceComponent(5, 'B')
        );
    }

    @Override
    protected void setupComponents() {

    }

    @Override
    protected void setupStyle() {
        root.setMinHeight(computeMinHeight());
        root.prefWidthProperty().bind(measureWidth);
        root.setPadding(new Insets(0, 10, 0, 10));
        root.setStyle("-fx-background-color: transparent");
    }

    @Override
    protected void setupLayout() {
        double y       = 0;

        staffComponents = staffComponents.reversed();

        for (Staffable<?> component : staffComponents) {
            component.bindWidth(measureWidth);
            component.setPosition(y);
            if (component instanceof SpaceComponent)
                y += MusicMetrics.BLANK_SPACE_HEIGHT;
            else if (component instanceof LineComponent)
                y += MusicMetrics.LINE_HEIGHT;

        }

        staffComponents.forEach(staffable -> {
            root.getChildren().add(staffable.getRoot());
        });

        root.getChildren().addAll(
                buildBarLine(),
                buildRightBarLine()
        );
    }

    @Override
    protected void setupEventListeners() {
        measureProperty.addListener((observableValue, measure, t1) -> {
            if (t1 != null && t1.getAttributes() != null) {
                this.getRoot().getChildren().add(buildClef());
                this.getRoot().getChildren().add(buildMetre());
            }
            if (t1 != null && !t1.getNotes().isEmpty())
                this.getRoot().getChildren().addAll(buildNotes());
        });
    }

    @Override
    protected void setupEventHandlers() {

    }

    public void assignMeasure(Measure measure) {
        this.measureProperty.set(measure);

        LayoutManager.getInstance().register(this);
    }

    public ObjectProperty<Measure> measureProperty() {
        return measureProperty;
    }

    public DoubleProperty measureWidthProperty() {
        return measureWidth;
    }

    private Canvas buildClef() {
        if (measureProperty.get() == null)                 return null;
        if (measureProperty.get().getAttributes() == null) return null;

        final Attributes attributes = measureProperty.get().getAttributes();
        final ClefComponent clefComponent = new ClefComponent(
                attributes.getClefs().getFirst().getSign(),
                attributes.getClefs().getFirst().getLine()
        );

        if (attributes.getClefs().getFirst().getLine() == 2)  {
            clefComponent.getRoot().setLayoutY(staffComponents.getFirst().getY());
            clefComponent.getRoot().setLayoutX(5);
        }

        expand(MusicMetrics.CLEF_CANVAS_WIDTH);

        return clefComponent.getRoot();
    }

    private Canvas buildMetre() {
        if (measureProperty.get() == null)                 return null;
        if (measureProperty.get().getAttributes() == null) return null;

        final Attributes attributes = measureProperty.get().getAttributes();
        final MetreComponent metreComponent = new MetreComponent(
                attributes.getBeats(),
                attributes.getBeatType()
        );

        metreComponent.getRoot().setLayoutY(staffComponents.get(2).getY());
        metreComponent.getRoot().setLayoutX(35);

        expand(MusicMetrics.METRE_CANVAS_WIDTH);

        return metreComponent.getRoot();
    }

    private List<Canvas> buildNotes() {
        if (measureProperty.get() == null) return List.of();
        if (
            measureProperty.get().getNotes() == null
            ||
            measureProperty.get().getNotes().isEmpty()
        ) return List.of();

        final List<Canvas> notes = new ArrayList<>();

        double xOffset = measureProperty.get().getAttributes() == null
                ? MusicMetrics.NOTE_CANVAS_WIDTH / 2
                : MusicMetrics.CLEF_CANVAS_WIDTH + 30;

        xOffset += measureWidth.get() / (measureProperty.get().getNotes().size() + 2);

        for (Note note : measureProperty.get().getNotes()) {
            Staffable<?> staffable = evaluateStaffPlace(note);

            NoteComponent noteComponent = new NoteComponent(note);
            noteComponent.getRoot().setLayoutY(staffable.getY());
            noteComponent.getRoot().setLayoutX(xOffset);

            notes.add(noteComponent.getRoot());
            xOffset += MusicMetrics.NOTE_CANVAS_WIDTH + 4;
        }

        expand(notes.size() * MusicMetrics.NOTE_CANVAS_WIDTH);
        return notes;
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

    private Staffable<?> evaluateStaffPlace(final Note note) {
        if (note.isRest()) {
            return switch (note.getType()) {
                case Whole -> staffComponents.get(5);
                case Half -> staffComponents.get(6);
                case Quarter -> null;
                case Eighth -> null;
                case Semiquaver -> null;
                case null -> throw new IllegalArgumentException();
            };
        }

        char noteStep   = note.getStep();
        int  noteOctave = note.getOctave();

        for (Staffable<?> component : staffComponents) {
            if (component.getStep() == noteStep && component.getOctave() == noteOctave) {
                return component;
            }
        }

        throw new IllegalArgumentException("No staff place for: " + noteStep + noteOctave);
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

    private void expand(double amount) {
        measureWidth.set(measureWidth.get() + amount);
    }
}
