package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.gui.manager.LayoutManager;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Staffable;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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
