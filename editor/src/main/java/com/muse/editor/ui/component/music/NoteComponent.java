package com.muse.editor.ui.component.music;

import com.muse.editor.core.model.score.Note;
import com.muse.editor.ui.util.FontFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class NoteComponent extends Canvas {
    private final Note note;

    public NoteComponent(Note note) {
        super(30, 60);
        this.note = note;

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFont(FontFactory.getBravura(48));
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(getNoteGlyph(), 15, 32);
    }

    public Note getNote() {
        return note;
    }

    private String getNoteGlyph() {
        if (note.isRest()) return getRestGlyph();
        return getNoteHeadGlyph();
    }

    private String getNoteHeadGlyph() {
        return switch (note.getType()) {
            case "whole"   -> FontFactory.getWholeNote();
            case "half"    -> FontFactory.getHalfNote();
            case "quarter" -> FontFactory.getQuarterNote();
            case "eighth"  -> FontFactory.getEighthNote();
            case "16th"    -> FontFactory.getSixteenthNote();
            case "32nd"    -> FontFactory.getThirtySecondNote();
            case "64th"    -> FontFactory.getSixtyFourthNote();
            default        -> throw new IllegalArgumentException("Unknown note type: " + note.getType());
        };
    }

    private String getRestGlyph() {
        return switch (note.getType()) {
            case "whole"   -> FontFactory.getWholeRest();
            case "half"    -> FontFactory.getHalfRest();
            case "quarter" -> FontFactory.getQuarterRest();
            case "eighth"  -> FontFactory.getEighthRest();
            case "16th"    -> FontFactory.getSixteenthRest();
            case "32nd"    -> FontFactory.getThirtySecondRest();
            case "64th"    -> FontFactory.getSixtyFourthRest();
            default        -> throw new IllegalArgumentException("Unknown rest type: " + note.getType());
        };
    }
}
