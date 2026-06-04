package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.gui.model.Measurable;
import com.muse.editor.redevelop.gui.util.FontFactory;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class MetreComponent extends Measurable<Canvas> {
    private final int beats;
    private final int beatType;

    public MetreComponent(int beats, int beatType) {
        super(new Canvas());

        this.beats    = beats;
        this.beatType = beatType;

        root.setWidth(MusicMetrics.METRE_CANVAS_WIDTH);
        root.setHeight(MusicMetrics.METRE_CANVAS_HEIGHT);

        draw();
    }

    @Override
    protected void draw() {
        graphicsContext.setFont(FontFactory.getBravura(MusicMetrics.METRE_FONT_SIZE));
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        graphicsContext.fillText(getDigitGlyph(beats), 0, 20);
        graphicsContext.fillText(getDigitGlyph(beatType), 0, 44);
    }

    @Override
    protected void highlight() {

    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {

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
