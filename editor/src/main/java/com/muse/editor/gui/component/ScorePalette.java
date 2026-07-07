package com.muse.editor.gui.component;

import com.muse.editor.core.model.music.Note;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.SetNoteInputEvent;
import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.gui.util.FontFactory;
import com.muse.editor.gui.model.Presentable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class ScorePalette extends Presentable<VBox> {
    private ObjectProperty<Button> activeButton;

    private TitledPane notesPane;
    private TitledPane restsPane;
    private TitledPane clefsPane;
    private TitledPane dynamicsPane;
    private TitledPane metresPane;
    private TitledPane accidentalsPane;

    private TilePane notesContainer;
    private TilePane restsContainer;
    private TilePane clefsContainer;
    private TilePane dynamicsContainer;
    private TilePane metresContainer;
    private TilePane accidentalsContainer;

    private Button wholeNoteBtn;
    private Button halfNoteBtn;
    private Button quarterNoteBtn;
    private Button quaverNoteBtn;
    private Button semiquaverNoteBtn;

    private Button wholeRestBtn;
    private Button halfRestBtn;
    private Button quarterRestBtn;
    private Button quaverRestBtn;
    private Button semiquaverRestBtn;

    private Button sharpBtn;
    private Button flatBtn;
    private Button naturalBtn;

    private Button GClefBtn;
    private Button FClefBtn;
    private Button CClefBtn;

    private Button pBtn;
    private Button fBtn;
    private Button mpBtn;
    private Button mfBtn;

    private Button twoFourBtn;
    private Button threeFourBtn;
    private Button fourFourBtn;
    private Button sixEightBtn;
    private Button twelveEightBtn;

    public ScorePalette() {
        super(new VBox());
    }

    @Override
    protected void initComponents() {
        activeButton = new SimpleObjectProperty<>(null);

        notesPane       = new TitledPane();
        restsPane       = new TitledPane();
        clefsPane       = new TitledPane();
        dynamicsPane    = new TitledPane();
        metresPane      = new TitledPane();
        accidentalsPane = new TitledPane();

        notesContainer       = new TilePane();
        restsContainer       = new TilePane();
        clefsContainer       = new TilePane();
        dynamicsContainer    = new TilePane();
        metresContainer      = new TilePane();
        accidentalsContainer = new TilePane();

        wholeNoteBtn      = ButtonFactory.createNoteButton(FontFactory.getWholeNote(),      "whole-note-btn",      "note-btn");
        halfNoteBtn       = ButtonFactory.createNoteButton(FontFactory.getHalfNote(),       "half-note-btn",       "note-btn");
        quarterNoteBtn    = ButtonFactory.createNoteButton(FontFactory.getQuarterNote(),    "quarter-note-btn",    "note-btn");
        quaverNoteBtn     = ButtonFactory.createNoteButton(FontFactory.getEighthNote(),     "quaver-note-btn",     "note-btn");
        semiquaverNoteBtn = ButtonFactory.createNoteButton(FontFactory.getSixteenthNote(),  "semiquaver-note-btn", "note-btn");

        wholeRestBtn      = ButtonFactory.createNoteButton(FontFactory.getWholeRest(),      "whole-rest-btn",      "note-btn");
        halfRestBtn       = ButtonFactory.createNoteButton(FontFactory.getHalfRest(),       "half-rest-btn",       "note-btn");
        quarterRestBtn    = ButtonFactory.createNoteButton(FontFactory.getQuarterRest(),    "quarter-rest-btn",    "note-btn");
        quaverRestBtn     = ButtonFactory.createNoteButton(FontFactory.getEighthRest(),     "quaver-rest-btn",     "note-btn");
        semiquaverRestBtn = ButtonFactory.createNoteButton(FontFactory.getSixteenthRest(),  "semiquaver-rest-btn", "note-btn");

        sharpBtn   = ButtonFactory.createSmallMusicButton(FontFactory.getSharp(),   "sharp-btn",   "accidental-btn");
        flatBtn    = ButtonFactory.createSmallMusicButton(FontFactory.getFlat(),    "flat-btn",    "accidental-btn");
        naturalBtn = ButtonFactory.createSmallMusicButton(FontFactory.getNatural(), "natural-btn", "accidental-btn");

        GClefBtn = ButtonFactory.createClefButton(FontFactory.getGClef(), "g-clef-btn", "clef-btn");
        FClefBtn = ButtonFactory.createClefButton(FontFactory.getFClef(), "f-clef-btn", "clef-btn");
        CClefBtn = ButtonFactory.createClefButton(FontFactory.getCClef(), "c-clef-btn", "clef-btn");

        pBtn  = ButtonFactory.createSmallMusicButton(FontFactory.getDynamicP(),  "dynamic-p-btn",  "dynamic-btn");
        fBtn  = ButtonFactory.createSmallMusicButton(FontFactory.getDynamicF(),  "dynamic-f-btn",  "dynamic-btn");
        mpBtn = ButtonFactory.createSmallMusicButton(FontFactory.getDynamicMp(), "dynamic-mp-btn", "dynamic-btn");
        mfBtn = ButtonFactory.createSmallMusicButton(FontFactory.getDynamicMf(), "dynamic-mf-btn", "dynamic-btn");

        twoFourBtn      = ButtonFactory.createTimeSignatureButton(2,  4, "time-2-4-btn",   "metre-btn");
        threeFourBtn    = ButtonFactory.createTimeSignatureButton(3,  4, "time-3-4-btn",   "metre-btn");
        fourFourBtn     = ButtonFactory.createTimeSignatureButton(4,  4, "time-4-4-btn",   "metre-btn");
        sixEightBtn     = ButtonFactory.createTimeSignatureButton(6,  8, "time-6-8-btn",   "metre-btn");
        twelveEightBtn  = ButtonFactory.createTimeSignatureButton(12, 8, "time-12-8-btn",  "metre-btn");
    }

    @Override
    protected void setupComponents() {
        notesPane.setText("Notes");
        restsPane.setText("Rests");
        clefsPane.setText("Clefs");
        accidentalsPane.setText("Accidentals");
        dynamicsPane.setText("Dynamics");
        metresPane.setText("Time signatures");

        notesPane.setContent(notesContainer);
        restsPane.setContent(restsContainer);
        clefsPane.setContent(clefsContainer);
        accidentalsPane.setContent(accidentalsContainer);
        dynamicsPane.setContent(dynamicsContainer);
        metresPane.setContent(metresContainer);

        notesContainer.getChildren().addAll(
                wholeNoteBtn, halfNoteBtn, quarterNoteBtn,
                quaverNoteBtn, semiquaverNoteBtn
        );

        restsContainer.getChildren().addAll(
                wholeRestBtn, halfRestBtn, quarterRestBtn,
                quaverRestBtn, semiquaverRestBtn
        );

        clefsContainer.getChildren().addAll(
                GClefBtn, FClefBtn, CClefBtn
        );

        accidentalsContainer.getChildren().addAll(
                sharpBtn, flatBtn, naturalBtn
        );

        dynamicsContainer.getChildren().addAll(
                pBtn, mpBtn, mfBtn, fBtn
        );

        metresContainer.getChildren().addAll(
                twoFourBtn, threeFourBtn, fourFourBtn,
                sixEightBtn, twelveEightBtn
        );
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components/score-palette.css")).toExternalForm());
        root.getStyleClass().add("score-palette");

        for (TilePane tp : new TilePane[]{
                notesContainer, restsContainer, clefsContainer,
                accidentalsContainer, dynamicsContainer, metresContainer}) {
            tp.getStyleClass().add("palette-tile");
            tp.setHgap(4);
            tp.setVgap(4);
            tp.setPadding(new Insets(6));
        }
    }

    @Override
    protected void setupLayout() {
        root.getChildren().addAll(
                notesPane,
                restsPane,
                clefsPane,
//                accidentalsPane,
                dynamicsPane
//                metresPane
        );
    }

    @Override
    protected void setupEventListeners() {
        activeButton.addListener((observableValue, button, t1) -> {
            if (t1 != null) {
                if (button != null) {
                    button.setStyle("-fx-background-color: #f3f3f3");
                }
                t1.setStyle("-fx-background-color: #efefa8");
            }

        });
    }

    @Override
    protected void setupEventHandlers() {
        wholeNoteBtn.setOnAction(actionEvent -> {
            activeButton.set(wholeNoteBtn);
            EventBus.getInstance().publish(new SetNoteInputEvent(Note.Type.Whole, false));
        });
        halfNoteBtn.setOnAction(actionEvent -> {
            activeButton.set(halfNoteBtn);
            EventBus.getInstance().publish(new SetNoteInputEvent(Note.Type.Half, false));
        });
        quarterNoteBtn.setOnAction(actionEvent -> {
            activeButton.set(quarterNoteBtn);
            EventBus.getInstance().publish(new SetNoteInputEvent(Note.Type.Quarter, false));
        });
        quaverNoteBtn.setOnAction(actionEvent -> {
            activeButton.set(quaverNoteBtn);
            EventBus.getInstance().publish(new SetNoteInputEvent(Note.Type.Eighth, false));
        });
        semiquaverNoteBtn.setOnAction(actionEvent -> {
            activeButton.set(semiquaverNoteBtn);
            EventBus.getInstance().publish(new SetNoteInputEvent(Note.Type.Semiquaver, false));
        });
    }
}