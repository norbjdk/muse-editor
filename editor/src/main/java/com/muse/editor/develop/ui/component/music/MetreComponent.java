package com.muse.editor.develop.ui.component.music;

import com.muse.editor.develop.ui.util.FontFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MetreComponent extends Canvas {
    public MetreComponent(int beats, int beatType) {
        super(30, 60);
        GraphicsContext gc = getGraphicsContext2D();

        Font bravura = FontFactory.getBravura(48);
        gc.setFont(bravura);
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);

        gc.fillText(getDigitGlyph(beats), 0, 20);
        gc.fillText(getDigitGlyph(beatType), 0, 44);
    }

    private String getDigitGlyph(int digit) {
        return switch (digit) {
            case 0 -> "\uE080";
            case 1 -> "\uE081";
            case 2 -> "\uE082";
            case 3 -> "\uE083";
            case 4 -> "\uE084";
            case 5 -> "\uE085";
            case 6 -> "\uE086";
            case 7 -> "\uE087";
            case 8 -> "\uE088";
            case 9 -> "\uE089";
            default -> throw new IllegalArgumentException();
        };
    }
}
