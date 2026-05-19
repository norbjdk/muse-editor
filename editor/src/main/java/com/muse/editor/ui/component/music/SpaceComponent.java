package com.muse.editor.ui.component.music;

import com.muse.editor.ui.model.Presentable;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpaceComponent extends Rectangle implements StaffComponent {
    private final int  octave;
    private final char step;

    public SpaceComponent(int octave, char step) {
        super(0, 0, MusicMetrics.BASE_MEASURE_WIDTH, MusicMetrics.BLANK_SPACE_HEIGHT);

        this.octave = octave;
        this.step = step;

        this.setFill(Color.TRANSPARENT);
        this.setStroke(null);
        this.setCursor(Cursor.HAND);

        this.setOnMouseEntered(mouseEvent -> this.setFill(Color.color(0.2, 0.5, 1.0, 0.15)));
        this.setOnMouseExited(mouseEvent -> this.setFill(Color.TRANSPARENT));
    }

    public void setPosition(double y) {
        this.setY(y);
    }

    public void bindWidth(DoubleProperty width) {
        this.widthProperty().bind(width);
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
}
