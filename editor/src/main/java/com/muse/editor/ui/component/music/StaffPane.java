package com.muse.editor.ui.component.music;

import com.muse.editor.ui.model.Presentable;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StaffPane extends Pane implements Presentable {

    private static final int LINES_NUMBER = 5;
    private static final double SPACE_HEIGHT = 10.0;
    private static final double LINE_HEIGHT = 0.7;
    private static final double MIN_STAFF_WIDTH = 160.0;
    private static final int SLOT_COUNT = 11;

    private static final double STAFF_TOP_Y = SPACE_HEIGHT;
    private static final double STAFF_BOTTOM_Y = 5 * SPACE_HEIGHT + 4 * LINE_HEIGHT;

    private static final double BARLINE_STROKE = 1;

    private final List<Rectangle> spaces = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();

    private Consumer<Integer> onSlotClicked;

    public StaffPane() {
        present();
    }

    @Override
    public void initComponents() {
        spaces.clear();
        lines.clear();
    }

    @Override
    public void setupComponents() {
        double y = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (i % 2 == 0) {
                spaces.add(buildSpace(y, i));
                y += SPACE_HEIGHT;
            } else {
                lines.add(buildLine(y, i));
                y += LINE_HEIGHT;
            }
        }
    }

    @Override
    public void setupStyle() {
        this.setStyle("-fx-background-color: transparent;");
        this.setPrefWidth(MIN_STAFF_WIDTH);
        this.setPrefHeight(computeTotalHeight());
    }

    @Override
    public void setupLayout() {
        getChildren().addAll(spaces);
        getChildren().addAll(lines);
        getChildren().add(buildBarline(0));
        getChildren().add(buildBarline(MIN_STAFF_WIDTH));
    }

    @Override
    public void setupEventListeners() {}

    @Override
    public void setupEventHandlers() {}

    private Rectangle buildSpace(double y, int slotIndex) {
        Rectangle rect = new Rectangle(0, y, MIN_STAFF_WIDTH, SPACE_HEIGHT);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(null);
        rect.setCursor(Cursor.HAND);
        rect.setOnMouseClicked(e -> fireSlotClicked(slotIndex));
        rect.setOnMouseEntered(e -> rect.setFill(Color.color(0.2, 0.5, 1.0, 0.15)));
        rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
        return rect;
    }

    private Line buildLine(double y, int slotIndex) {
        Rectangle hitArea = new Rectangle(0, y - SPACE_HEIGHT / 2.0, MIN_STAFF_WIDTH, SPACE_HEIGHT);
        hitArea.setFill(Color.TRANSPARENT);
        hitArea.setStroke(null);
        hitArea.setCursor(Cursor.HAND);
        hitArea.setOnMouseClicked(e -> fireSlotClicked(slotIndex));
        hitArea.setOnMouseEntered(e -> hitArea.setFill(Color.color(1.0, 0.4, 0.0, 0.2)));
        hitArea.setOnMouseExited(e -> hitArea.setFill(Color.TRANSPARENT));
        getChildren().add(hitArea);

        Line line = new Line(0, y, MIN_STAFF_WIDTH, y);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(LINE_HEIGHT);
        line.setMouseTransparent(true);
        return line;
    }

    private Line buildBarline(double x) {
        Line barline = new Line(x, STAFF_TOP_Y, x, STAFF_BOTTOM_Y);
        barline.setStroke(Color.BLACK);
        barline.setStrokeWidth(BARLINE_STROKE);
        barline.setMouseTransparent(true);
        return barline;
    }

    private void fireSlotClicked(int slotIndex) {
        if (onSlotClicked != null) {
            onSlotClicked.accept(slotIndex);
        }
    }

    private double computeTotalHeight() {
        return (LINES_NUMBER + 1) * SPACE_HEIGHT + LINES_NUMBER * LINE_HEIGHT;
    }
}