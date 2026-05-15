package com.muse.editor.ui.component.music;

import com.muse.editor.ui.model.Presentable;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StaffPane extends Pane implements Presentable {
    private final double         BASE_WIDTH = 220;
    private final DoubleProperty currentStaffWidth = new SimpleDoubleProperty(BASE_WIDTH);

    private static final int LINES_NUMBER = 5;
    private static final double SPACE_HEIGHT = 10.0;
    private static final double LINE_HEIGHT = 0.7;
    private static final int SLOT_COUNT = 12;

    private static final double STAFF_TOP_Y = SPACE_HEIGHT;
    private static final double STAFF_BOTTOM_Y = 5 * SPACE_HEIGHT + 4 * LINE_HEIGHT;

    private static final double BARLINE_STROKE = 1;

    private final List<BlankSpace> blankSpaces = new ArrayList<>();
    private final List<Rectangle> spaces = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();

    private Consumer<Integer> onSlotClicked;

    public StaffPane() {
        present();
    }

    @Override
    public void initComponents() {
        blankSpaces.clear();
        spaces.clear();
        lines.clear();
    }

    @Override
    public void setupComponents() {
        double y = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (i % 2 == 0) {
                final BlankSpace blankSpace = new BlankSpace(y, i);
                blankSpace.bindWidth(currentStaffWidth);
                blankSpaces.add(blankSpace);
//                spaces.add(buildSpace(y, i));

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
        this.setPrefWidth(currentStaffWidth.get());
        this.setPrefHeight(computeTotalHeight());
        this.setPadding(new Insets(0, 10, 0, 10));
    }

    @Override
    public void setupLayout() {
        getChildren().clear();

        getChildren().addAll(blankSpaces);
        getChildren().addAll(lines);
        getChildren().add(buildBarline(0));
        getChildren().add(buildBarline(currentStaffWidth.get()));
    }

    @Override
    public void setupEventListeners() {
    }

    @Override
    public void setupEventHandlers() {
        currentStaffWidth.addListener((obs, oldV, newV) -> {
            setupLayout();
        });
    }

    public DoubleProperty getCurrentStaffWidth() {
        return currentStaffWidth;
    }

    public void setOnSlotClicked(Consumer<Integer> handler) {
        this.onSlotClicked = handler;
    }

    private Rectangle buildSpace(double y, int slotIndex) {
        Rectangle rect = new Rectangle(0, y, 0, SPACE_HEIGHT);
        rect.widthProperty().bind(currentStaffWidth);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(null);
        rect.setCursor(Cursor.HAND);
        rect.setOnMouseClicked(e -> fireSlotClicked(slotIndex));
        rect.setOnMouseEntered(e -> rect.setFill(Color.color(0.2, 0.5, 1.0, 0.15)));
        rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
        return rect;
    }

    private Line buildLine(double y, int slotIndex) {
        Rectangle hitArea = new Rectangle(0, y - SPACE_HEIGHT / 2.0, currentStaffWidth.get(), SPACE_HEIGHT);
        hitArea.setFill(Color.TRANSPARENT);
        hitArea.setStroke(null);
        hitArea.setCursor(Cursor.HAND);
        hitArea.setOnMouseClicked(e -> fireSlotClicked(slotIndex));
        hitArea.setOnMouseEntered(e -> hitArea.setFill(Color.color(1.0, 0.4, 0.0, 0.2)));
        hitArea.setOnMouseExited(e -> hitArea.setFill(Color.TRANSPARENT));
        getChildren().add(hitArea);

        Line line = new Line(0, y, 0, y);
        line.endXProperty().bind(currentStaffWidth);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(LINE_HEIGHT);
        line.setMouseTransparent(true);

        if (slotIndex == SLOT_COUNT -1) {
            line.setVisible(false);
        }

        return line;
    }

    public void setStaffWidth(double width) {
        currentStaffWidth.set(width);
        this.setPrefWidth(width);
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

    public double getBASE_WIDTH() {
        return BASE_WIDTH;
    }

    private double computeTotalHeight() {
        return (LINES_NUMBER + 1) * SPACE_HEIGHT + LINES_NUMBER * LINE_HEIGHT;
    }
}