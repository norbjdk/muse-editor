package com.muse.editor.redevelop.gui.util;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

public final class ButtonFactory {

    private static final double NOTE_W  = 28, NOTE_H  = 52, NOTE_SIZE  = 32, NOTE_X  = 4,  NOTE_Y  = 38;

    private static final double CLEF_W  = 28, CLEF_H  = 52, CLEF_SIZE  = 32, CLEF_X  = 4,  CLEF_Y  = 38;

    private static final double SMALL_W = 28, SMALL_H = 52, SMALL_SIZE = 32, SMALL_X = 4,  SMALL_Y = 38;

    private static final double METRE_W = 28, METRE_H = 44, METRE_SIZE = 16;

    private ButtonFactory() {}

    public static Button createButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        return button;
    }

    public static Button createButton(String text, String id, String tooltip) {
        Button button = createButton(text, id);
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String style) {
        final Button button = new Button(text);
        button.setId(id);
        button.setTooltip(new Tooltip(tooltip));
        button.getStyleClass().add(style);
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String style, Font font) {
        final Button button = createButton(text, id, tooltip, style);
        button.setFont(font);
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String[] classStyles) {
        Button button = new Button(text);
        button.setId(id);
        button.setTooltip(new Tooltip(tooltip));
        button.getStyleClass().addAll(Arrays.asList(classStyles));
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String[] classStyles, Font font) {
        Button button = createButton(text, id, tooltip, classStyles);
        button.setFont(font);
        return button;
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(10);
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(display);
        button.setGraphicTextGap(10);
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display, int textGap) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(display);
        button.setGraphicTextGap(textGap);
    }

    public static Button createNoteButton(String glyph, String id, String style) {
        return createMusicButton(glyph, id, style, NOTE_W, NOTE_H, NOTE_SIZE, NOTE_X, NOTE_Y);
    }

    public static Button createClefButton(String glyph, String id, String style) {
        return createMusicButton(glyph, id, style, CLEF_W, CLEF_H, CLEF_SIZE, CLEF_X, CLEF_Y);
    }

    public static Button createSmallMusicButton(String glyph, String id, String style) {
        return createMusicButton(glyph, id, style, SMALL_W, SMALL_H, SMALL_SIZE, SMALL_X, SMALL_Y);
    }

    public static Button createMusicButton(String glyph, String id, String style) {
        return createNoteButton(glyph, id, style);
    }

    private static Button createMusicButton(
            String glyph, String id, String style,
            double cw, double ch, double fontSize, double x, double y) {

        final Canvas canvas       = new Canvas(cw, ch);
        final GraphicsContext gc  = canvas.getGraphicsContext2D();

        gc.setFont(FontFactory.getBravura(fontSize));
        gc.setFill(Color.BLACK);
        gc.setTextBaseline(VPos.BASELINE);
        gc.fillText(glyph, x, y);

        final Button button = new Button();
        button.setId(id);
        button.getStyleClass().add(style);
        button.setGraphic(canvas);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return button;
    }

    public static Button createTimeSignatureButton(int numerator, int denominator, String id, String style) {
        final Canvas canvas      = new Canvas(METRE_W, METRE_H);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFont(FontFactory.getBravura(METRE_SIZE));
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);

        final double cx = METRE_W / 2.0;
        gc.fillText(String.valueOf(numerator),   cx, 2);
        gc.fillText(String.valueOf(denominator), cx, METRE_H / 2.0 + 1);

        final Button button = new Button();
        button.setId(id);
        button.getStyleClass().add(style);
        button.setGraphic(canvas);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return button;
    }
}