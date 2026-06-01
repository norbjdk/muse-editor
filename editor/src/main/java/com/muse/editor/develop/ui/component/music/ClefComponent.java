package com.muse.editor.develop.ui.component.music;

import com.muse.editor.develop.ui.util.FontFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ClefComponent extends Canvas {
    private final int value;

    public ClefComponent(int value) {
        super(MusicMetrics.CLEF_CANVAS_WIDTH, MusicMetrics.CLEF_CANVAS_HEIGHT);
        this.value = value;

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFont(FontFactory.getBravura(MusicMetrics.CLEF_FONT_SIZE));
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(extractClef(value), 15, 43);
    }

    public int getValue() {
        return value;
    }

    private String extractClef(int fifths) {
        return switch (fifths) {
            case 0 -> FontFactory.getGClef();
            default -> throw new IllegalStateException("Unexpected value: " + fifths);
        };
    }
}
