package com.muse.editor.ui.component.music;

import com.muse.editor.ui.model.Presentable;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class BlankSpace extends Rectangle implements Presentable {
    private final double y;
    private final double index;

    private Consumer<Integer> onSpaceClicked;

    public BlankSpace(double y, int index) {
        this.y = y;
        this.index = index;

        present();
    }

    public void bindWidth(DoubleProperty width) {
        this.widthProperty().bind(width);
    }

    public void setOnSpaceClicked(Consumer<Integer> handler) {
        this.onSpaceClicked = handler;
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        this.setFill(Color.TRANSPARENT);
        this.setStroke(null);
        this.setCursor(Cursor.HAND);
    }

    @Override
    public void setupLayout() {

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {
        this.setOnMouseClicked(e -> handleSpaceClicked());
        this.setOnMouseEntered(e -> this.setFill(Color.color(0.2, 0.5, 1.0, 0.15)));
        this.setOnMouseExited(e -> this.setFill(Color.TRANSPARENT));
    }

    private void handleSpaceClicked() {
        if (onSpaceClicked != null) {
            onSpaceClicked.accept(Integer.parseInt(String.valueOf(index)));
        }
    }
}
