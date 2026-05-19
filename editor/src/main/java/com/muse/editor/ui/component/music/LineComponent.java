package com.muse.editor.ui.component.music;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Map;

public class LineComponent extends Pane implements StaffComponent {
    private static final Map<Integer, Character> drawExcluding = Map.of(
            3, 'A', 4, 'C', 5, 'A'
    );

    private final int  octave;
    private final char step;

    private double y;

    private Line      line;
    private Rectangle hitArea;

    public LineComponent(int octave, char step) {
        this.octave = octave;
        this.step   = step;

        line    = new Line();
        hitArea = new Rectangle();

        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(0);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MusicMetrics.LINE_HEIGHT);
        line.setMouseTransparent(true);

        final Character excludedStep = drawExcluding.get(octave);
        if (excludedStep != null && excludedStep == step) {
            line.setVisible(false);
        }

        hitArea.setX(0);
        hitArea.setY(0);
        hitArea.setHeight(MusicMetrics.BLANK_SPACE_HEIGHT);
        hitArea.setWidth(0);

        hitArea.setFill(Color.TRANSPARENT);
        hitArea.setStroke(null);
        hitArea.setCursor(Cursor.HAND);

        this.setStyle("-fx-background-color: transparent");
        this.setPickOnBounds(false);

        hitArea.setOnMouseEntered(mouseEvent -> hitArea.setFill(Color.color(1.0, 0.4, 0.0, 0.2)));
        hitArea.setOnMouseExited(mouseEvent -> hitArea.setFill(Color.TRANSPARENT));

        this.getChildren().addAll(
                hitArea,
                line
        );
    }

    public void setPosition(double y) {
        this.y = y;

        line.setStartY(y);
        line.setEndY(y);

        hitArea.setY(y - MusicMetrics.BLANK_SPACE_HEIGHT / 2.0);
    }

    public void bindWidth(DoubleProperty width) {
        line.endXProperty().bind(width);
        hitArea.widthProperty().bind(width);
    }

    @Override
    public String toString() {
        return "Step: " + step + "\nOctave: " + octave;
    }

    @Override
    public int getOctave() {
        return octave;
    }

    @Override
    public char getStep() {
        return step;
    }

    public double getY() {
        return y;
    }
}
