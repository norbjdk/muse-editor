package com.muse.editor.ui.component.old_music;

import com.muse.editor.core.model.score.Attributes;
import com.muse.editor.core.model.score.Measure;
import com.muse.editor.core.model.score.Note;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.FontFactory;
import com.muse.editor.ui.util.SheetMetrics;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
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
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("measure-pane");
    }

    @Override
    public void setupLayout() {
        double cursorX = SheetMetrics.MEASURE_PADDING_L;

        buildStaffLines();
        buildBarLine(0);

        final Attributes attrs = measure.getAttributes();

        if (attrs != null) {
            cursorX = buildClef(cursorX, attrs);
            if (showTimeSig) {
                cursorX = buildTimeSignature(cursorX, attrs);
            }
        }

        if (measure.getNotes() != null) {
            for (var note : measure.getNotes()) {
                if (note != null) {
                    cursorX = buildNote(cursorX, note, attrs);
                    cursorX += SheetMetrics.NOTE_MIN_SPACING;
                }
            }
        }

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

        Font timeFont = Font.font("Arial Black", 22);

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

    private double buildClef(double x, Attributes attributes) {
        final String glif;
        final double yOffset;

        if (attributes.getSign()[0] == 'F') {
            glif = FontFactory.getFClef();
            yOffset = SheetMetrics.STAFF_SPACE;
        } else {
            glif = FontFactory.getGClef();
            yOffset = SheetMetrics.STAFF_SPACE * -1;
        }

        final Text clef = new Text(glif);
        clef.setFont(FontFactory.getBravura((int) SheetMetrics.CLEF_FONT_SIZE));
        clef.setFill(Color.BLACK);
        clef.setX(x);
        clef.setY(staffY + SheetMetrics.STAFF_HEIGHT + yOffset);
        getChildren().add(clef);
        return x + SheetMetrics.CLEF_WIDTH;
    }

    private double buildNote(double x, Note note, Attributes attrs) {
        if (note.isRest()) {
            return buildRest(x, note);
        }

        final double noteY = calcNoteY(note);

        final Ellipse head = new Ellipse(
                x + SheetMetrics.NOTE_HEAD_WIDTH / 2,
                noteY,
                SheetMetrics.NOTE_HEAD_WIDTH / 2,
                SheetMetrics.NOTE_HEAD_HEIGHT / 2
        );
        head.setFill(isFilled(note.getType()) ? Color.BLACK : Color.TRANSPARENT);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(1.2);
        head.setRotate(-15);
        getChildren().add(head);

        drawLedgerLines(x, noteY);

        if (needsStem(note.getType())) {
            boolean stemUp = noteY > staffY + SheetMetrics.STAFF_HEIGHT / 2;
            double stemX = stemUp
                    ? x + SheetMetrics.NOTE_HEAD_WIDTH - 1
                    : x + 1;
            double stemTop = stemUp
                    ? noteY - SheetMetrics.STEM_LENGTH
                    : noteY;
            double stemBot = stemUp
                    ? noteY
                    : noteY + SheetMetrics.STEM_LENGTH;

            final Line stem = new Line(stemX, stemTop, stemX, stemBot);
            stem.setStrokeWidth(SheetMetrics.STEM_THICKNESS);
            stem.setStroke(Color.BLACK);
            getChildren().add(stem);
        }

        return x + SheetMetrics.NOTE_HEAD_WIDTH;
    }

    private double buildRest(double x, Note note) {
        final Text rest = new Text(getRestGlyph(note.getType()));
        rest.setFont(FontFactory.getBravura((int) SheetMetrics.CLEF_FONT_SIZE / 2));
        rest.setFill(Color.BLACK);
        rest.setX(x);
        rest.setY(staffY + SheetMetrics.STAFF_HEIGHT / 2);
        getChildren().add(rest);
        return x + SheetMetrics.NOTE_HEAD_WIDTH;
    }

    private double calcNoteY(Note note) {
        final int[] STEPS = {'C','D','E','F','G','A','B'};
        int stepIndex = 0;
        for (int i = 0; i < STEPS.length; i++) {
            if (STEPS[i] == Character.toUpperCase(note.getStep())) {
                stepIndex = i;
                break;
            }
        }
        int pos = stepIndex + (note.getOctave() - 4) * 7;
        double bottomLineY = staffY + SheetMetrics.STAFF_HEIGHT; // E4

        return bottomLineY - (pos - 2) * (SheetMetrics.STAFF_SPACE / 2.0);
    }

    private void drawLedgerLines(double x, double noteY) {
        double c4Y = staffY + SheetMetrics.STAFF_HEIGHT + SheetMetrics.STAFF_SPACE;
        if (Math.abs(noteY - c4Y) < 2) {
            Line ledger = new Line(
                    x - 3, c4Y,
                    x + SheetMetrics.NOTE_HEAD_WIDTH + 3, c4Y
            );
            ledger.setStrokeWidth(SheetMetrics.LINE_THICKNESS);
            ledger.setStroke(Color.BLACK);
            getChildren().add(ledger);
        }
    }

    private boolean isFilled(String type) {
        return !"whole".equals(type) && !"half".equals(type);
    }

    private boolean needsStem(String type) {
        return !"whole".equals(type);
    }

    private String getRestGlyph(String type) {
        return switch (type == null ? "" : type) {
            case "whole"   -> FontFactory.getWholeRest();
            case "half"    -> FontFactory.getHalfRest();
            case "quarter" -> FontFactory.getQuarterRest();
            case "eighth"  -> FontFactory.getEighthRest();
            default        -> FontFactory.getQuarterRest();
        };
    }
}
