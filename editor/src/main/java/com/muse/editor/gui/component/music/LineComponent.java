package com.muse.editor.gui.component.music;

import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.AddValueEvent;
import com.muse.editor.gui.model.Staffable;
import com.muse.editor.gui.util.MusicMetrics;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Map;

public class LineComponent extends Staffable<Pane> {
    private static final Map<Integer, Character> drawExcluding = Map.of(
            3, 'A', 4, 'C', 5, 'A'
    );

    private boolean excluded;

    private Line      line;
    private Rectangle hitArea;

    public LineComponent(int octave, char step) {
        super(new Pane(), octave, step);
    }

    @Override
    protected void initComponents() {
        line    = new Line();
        hitArea = new Rectangle();
    }

    @Override
    protected void setupComponents() {
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(0);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MusicMetrics.LINE_HEIGHT);
        line.setMouseTransparent(true);

        final Character excludedStep = drawExcluding.get(octave);
        if (excludedStep != null && excludedStep == step) {
            excluded = true;
        }

        hitArea.setX(0);
        hitArea.setY(0);
        hitArea.setHeight(MusicMetrics.BLANK_SPACE_HEIGHT);
        hitArea.setWidth(0);

        hitArea.setFill(Color.TRANSPARENT);
        hitArea.setStroke(null);
        hitArea.setCursor(Cursor.HAND);
    }

    @Override
    protected void setupLayout() {
        if (excluded) {
            root.getChildren().add(hitArea);
        } else {
            root.getChildren().addAll(hitArea, line);
        }
    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {
        hitArea.setOnMouseEntered(mouseEvent -> hitArea.setFill(Color.color(1.0, 0.4, 0.0, 0.2)));
        hitArea.setOnMouseExited(mouseEvent -> hitArea.setFill(Color.TRANSPARENT));
        hitArea.setOnMouseClicked(mouseEvent -> EventBus.getInstance().publish(new AddValueEvent(octave, step)));
    }

    @Override
    public void setPosition(double y) {
        this.y = y;
        root.setLayoutY(y);

        line.setStartY(0);
        line.setEndY(0);
        hitArea.setY(-MusicMetrics.BLANK_SPACE_HEIGHT / 2.0);
    }

    @Override
    public void bindWidth(DoubleProperty width) {
        line.endXProperty().bind(width);
        hitArea.widthProperty().bind(width);
    }
}
