package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.gui.model.Measurable;
import com.muse.editor.redevelop.gui.util.FontFactory;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ClefComponent extends Measurable<Canvas> {
    private final char sign;
    private final int  line;

    public ClefComponent(char sign, int line) {
        super(new Canvas());

        this.sign = sign;
        this.line = line;

        root.setWidth(MusicMetrics.CLEF_CANVAS_WIDTH);
        root.setHeight(MusicMetrics.CLEF_CANVAS_HEIGHT);

        draw();
    }

    @Override
    protected void draw() {
        graphicsContext.setFont(FontFactory.getBravura(MusicMetrics.CLEF_FONT_SIZE));
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(extractClef(), 15, 43);
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

    private String extractClef() {
        return switch (sign){
            case 'G' -> FontFactory.getGClef();
            case 'F' -> FontFactory.getFClef();
            default -> throw new IllegalStateException("Unexpected value: " + sign);
        };
    }
}
