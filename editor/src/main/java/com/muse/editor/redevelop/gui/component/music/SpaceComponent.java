package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.AddValueEvent;
import com.muse.editor.redevelop.gui.model.Staffable;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpaceComponent extends Staffable<Pane> {

    private Rectangle hitArea;

    public SpaceComponent(int octave, char step) {
        super(new Pane(), octave, step);
    }
    @Override
    protected void initComponents() {
        hitArea = new Rectangle();
    }

    @Override
    protected void setupComponents() {
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
        root.getChildren().addAll(
                hitArea
        );
    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {
        hitArea.setOnMouseEntered(mouseEvent -> hitArea.setFill(Color.color(0.2, 0.5, 1.0, 0.15)));
        hitArea.setOnMouseExited(mouseEvent -> hitArea.setFill(Color.TRANSPARENT));
        hitArea.setOnMouseClicked(mouseEvent -> EventBus.getInstance().publish(new AddValueEvent(octave, step)));
    }

    @Override
    public void setPosition(double y) {
        this.y = y;
        root.setLayoutY(y);
        hitArea.setY(0);
    }

    @Override
    public void bindWidth(DoubleProperty width) {
        hitArea.widthProperty().bind(width);
    }
}
