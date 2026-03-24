package com.norbjdk.museeditor.ui.component.music;

import com.norbjdk.museeditor.core.model.score.Attributes;
import com.norbjdk.museeditor.core.model.score.Measure;
import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.util.FontFactory;
import com.norbjdk.museeditor.ui.util.SheetMetrics;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Objects;

public class MeasurePane extends Group implements Presentable {

    private final Measure measure;
    private final boolean showTimeSig;
    private final double width;

    private final double staffY = SheetMetrics.STAFF_MARGIN_V;

    public MeasurePane(Measure measure, boolean showTimeSig, double width) {
        this.measure = measure;
        this.showTimeSig = showTimeSig;
        this.width = width;

        present();
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("measure-pane");
    }

    @Override
    public void setupLayout() {
        double cursorX = 0;

        buildStaffLines();
        buildBarLine(cursorX);
        buildBarLine(width);
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }

    private void buildStaffLines() {
        for (int i = 0; i < SheetMetrics.LINE_COUNT; i++) {
            double y = staffY + (SheetMetrics.LINE_COUNT - 1 - i) * SheetMetrics.STAFF_SPACE;

            final Line line = new Line(0, y, width, y);
            line.setStrokeWidth(SheetMetrics.LINE_THICKNESS);
            line.setStroke(Color.BLACK);

            getChildren().add(line);
        }
    }

    private void buildBarLine(double x) {
        double top = staffY;
        double bottom = staffY + SheetMetrics.STAFF_HEIGHT;

        final Line line = new Line(x, staffY, x, staffY + SheetMetrics.STAFF_HEIGHT);
        line.setStrokeWidth(SheetMetrics.BAR_LINE_THICKNESS);
        line.setStroke(Color.BLACK);

        getChildren().add(line);
    }

    private double buildTimeSignature(double x, Attributes attributes) {
        final double mid = staffY + SheetMetrics.STAFF_HEIGHT / 2;

        final Text beatsText = new Text(String.valueOf(attributes.getBeats()));

        beatsText.setFont(FontFactory.getBravura((int) SheetMetrics.TIME_SIG_FONT_SIZE));
        beatsText.setFill(Color.BLACK);
        beatsText.setX(x + SheetMetrics.STAFF_SPACE * 0.3);
        beatsText.setY(mid);

        final Text beatTypeText = new Text(String.valueOf(attributes.getBeatType()));

        beatTypeText.setFont(FontFactory.getBravura((int) SheetMetrics.TIME_SIG_FONT_SIZE));
        beatTypeText.setFill(Color.BLACK);
        beatTypeText.setX(x + SheetMetrics.STAFF_SPACE * 0.3);
        beatTypeText.setY(staffY + SheetMetrics.STAFF_HEIGHT + SheetMetrics.STAFF_SPACE * 0.2);

        getChildren().addAll(beatsText, beatTypeText);
        return x + SheetMetrics.TIME_SIG_WIDTH;
    }
}
