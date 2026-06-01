package com.muse.editor.develop.ui.component.music;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.core.edit.EditManager;
import com.muse.editor.develop.core.model.score.Note;
import com.muse.editor.develop.model.event.edit.InputModeOff;
import com.muse.editor.develop.ui.util.FontFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

public class NoteComponent extends Canvas {
    private final EditManager editManager = EditManager.getInstance();
    private final Note note;

    public NoteComponent(Note note) {
        super(MusicMetrics.NOTE_CANVAS_WIDTH, MusicMetrics.NOTE_CANVAS_HEIGHT);
        this.note = note;

        double noteHeadYPosition = 32.0;

        Translate shiftToNoteHead = new Translate();
        shiftToNoteHead.setY(-noteHeadYPosition);
        this.getTransforms().add(shiftToNoteHead);

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFont(FontFactory.getBravura(MusicMetrics.NOTE_FONT_SIZE));
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);

        gc.fillText(getNoteGlyph(), 15, noteHeadYPosition);

        if (note.getOctave() == 4 && note.getStep() == 'C') {
            gc.fillRect(3, MusicMetrics.NOTE_CANVAS_HEIGHT / 2 + 3, 23, 1);
        }

        this.setOnMouseClicked(mouseEvent -> {
            System.out.println(note.getType());
            editManager.switchMode();
        });

        EventBus.getInstance().subscribe(InputModeOff.class, event -> {

        });
    }

    public void handleInputOff() {

    }

    public void handleInputOn() {

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
        if (note.getType() == null || note.getType().isEmpty()) {
            return FontFactory.getWholeRest();
        }
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
