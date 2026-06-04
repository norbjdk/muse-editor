package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.gui.model.Measurable;
import com.muse.editor.redevelop.gui.util.FontFactory;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

public class NoteComponent extends Measurable<Canvas> {
    private final Note note;

    public NoteComponent(Note note) {
        super(new Canvas());

        this.note = note;

        root.setWidth(MusicMetrics.NOTE_CANVAS_WIDTH);
        root.setHeight(MusicMetrics.NOTE_CANVAS_HEIGHT);

        draw();
    }

    @Override
    protected void draw() {
        final double noteHeadYPosition = 32;
        final Translate shiftToNoteHead = new Translate();

        shiftToNoteHead.setY(-noteHeadYPosition);
        root.getTransforms().add(shiftToNoteHead);

        graphicsContext.setFont(FontFactory.getBravura(MusicMetrics.NOTE_FONT_SIZE));
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(getNoteGlyph(), 15, noteHeadYPosition);

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

    private String getNoteGlyph() {
        if (note.isRest()) return getRestGlyph();
        return getNoteHeadGlyph();
    }

    private String getNoteHeadGlyph() {
        return switch (note.getType()) {
            case Whole      -> FontFactory.getWholeNote();
            case Half       -> FontFactory.getHalfNote();
            case Quarter    -> FontFactory.getQuarterNote();
            case Eighth     -> FontFactory.getEighthNote();
            case Semiquaver -> FontFactory.getSixteenthNote();
            case null       -> throw new IllegalArgumentException("Unknown note type: " + note.getType());
        };
    }

    private String getRestGlyph() {
        if (note.getType() == null || note.getType().getValue().isEmpty()) {
            return FontFactory.getWholeRest();
        }
        return switch (note.getType()) {
            case Whole      -> FontFactory.getWholeRest();
            case Half       -> FontFactory.getHalfRest();
            case Quarter    -> FontFactory.getQuarterRest();
            case Eighth     -> FontFactory.getEighthRest();
            case Semiquaver -> FontFactory.getSixteenthRest();
            case null       -> throw new IllegalArgumentException("Unknown rest type: " + note.getType());
        };
    }
}
