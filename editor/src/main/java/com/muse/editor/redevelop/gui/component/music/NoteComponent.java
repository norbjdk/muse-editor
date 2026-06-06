package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.edit.CursorModel;
import com.muse.editor.redevelop.core.edit.EditorState;
import com.muse.editor.redevelop.core.model.music.Note;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.editor.NoteSelectedEvent;
import com.muse.editor.redevelop.event.editor.SelectNoteEvent;
import com.muse.editor.redevelop.gui.model.Measurable;
import com.muse.editor.redevelop.gui.util.FontFactory;
import com.muse.editor.redevelop.gui.util.MusicMetrics;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

import java.util.function.Consumer;

public class NoteComponent extends Measurable<Canvas> {
    private final Note note;

    private Consumer<NoteSelectedEvent> noteSelectedListener;

    final double noteHeadYPosition  = 32;
    final Translate shiftToNoteHead = new Translate();

    public NoteComponent(Note note) {
        super(new Canvas());

        this.note = note;

        root.setWidth(MusicMetrics.NOTE_CANVAS_WIDTH);
        root.setHeight(MusicMetrics.NOTE_CANVAS_HEIGHT);

        shiftToNoteHead.setY(-noteHeadYPosition);
        root.getTransforms().add(shiftToNoteHead);
        draw();
    }

    @Override
    protected void draw() {
        graphicsContext.clearRect(0, 0, root.getWidth(), root.getHeight());

        graphicsContext.setFont(FontFactory.getBravura(MusicMetrics.NOTE_FONT_SIZE));
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(getNoteGlyph(), 15, noteHeadYPosition);

    }

    @Override
    protected void highlight() {
        graphicsContext.clearRect(0, 0, root.getWidth(), root.getHeight());

        graphicsContext.setFont(FontFactory.getBravura(MusicMetrics.NOTE_FONT_SIZE));
        graphicsContext.setFill(Color.rgb(239, 239, 168));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(getNoteGlyph(), 15, noteHeadYPosition);
    }

    @Override
    protected void setupEventListeners() {
        noteSelectedListener = event -> {
            if (event.getNoteId() == note.getId()) {
                highlight();
                System.out.println("Selected: " + note.getId() + ", type:" + note.getType().getValue());
            }
            else draw();
        };
        EventBus.getInstance().subscribe(NoteSelectedEvent.class, noteSelectedListener);

        EditorState.getInstance().inputModeProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) draw();
        });
    }

    @Override
    protected void setupEventHandlers() {
        getRoot().setOnMouseClicked(mouseEvent -> {
            EventBus.getInstance().publish(new SelectNoteEvent(note.getId()));
        });
    }

    public void dispose() {
        EventBus.getInstance().unsubscribe(NoteSelectedEvent.class, noteSelectedListener);
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

    public Note getNote() {
        return note;
    }
}
