package com.muse.editor.develop.ui.component;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.model.event.AddNoteRequestedEvent;
import com.muse.editor.develop.model.event.AddRestRequestedEvent;
import com.muse.editor.develop.ui.model.Presentable;
import com.muse.editor.develop.ui.util.ButtonFactory;
import com.muse.editor.develop.ui.util.FontFactory;
import com.muse.editor.develop.ui.util.SpaceFactory;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.Objects;

import static com.muse.editor.develop.ui.util.SpaceFactory.createSpacer;

public class ToolBar extends HBox implements Presentable {
    private Button wholeNoteBtn;
    private Button halfNoteBtn;
    private Button quarterNoteBtn;
    private Button quaverNoteBtn;
    private Button semiquaverNoteBtn;
    private Button thirtyTwoNoteBtn;

    private Button wholeRestBtn;
    private Button halfRestBtn;
    private Button quarterRestBtn;
    private Button quaverRestBtn;
    private Button semiquaverRestBtn;
    private Button thirtyTwoRestBtn;

    private Button sharpBtn;
    private Button flatBtn;
    private Button naturalBtn;
    private Button doubleSharpBtn;
    private Button doubleFlatBtn;

    private Button dotBtn;

    private final Font font;

    public ToolBar() {
        font = FontFactory.getBravura(32);

        present();
    }

    @Override
    public void initComponents() {
        wholeNoteBtn = ButtonFactory.createButton(FontFactory.getWholeNote(), "whole-note", "Whole Note", "tool-bar-btn", font);
        halfNoteBtn = ButtonFactory.createButton(FontFactory.getHalfNote(), "half-note", "Half Note", "tool-bar-btn", font);
        quarterNoteBtn = ButtonFactory.createButton(FontFactory.getQuarterNote(), "quarter-note", "Quarter Note", "tool-bar-btn", font);
        quaverNoteBtn = ButtonFactory.createButton(FontFactory.getEighthNote(), "quaver-note", "Quaver Note", "tool-bar-btn", font);
        semiquaverNoteBtn = ButtonFactory.createButton(FontFactory.getSixteenthNote(), "semiquaver-note", "Semiquaver Note", "tool-bar-btn", font);
        thirtyTwoNoteBtn = ButtonFactory.createButton(FontFactory.getThirtySecondNote(), "thirty-two-note", "ThirtyTwo Note", "tool-bar-btn", font);

        wholeRestBtn = ButtonFactory.createButton(FontFactory.getWholeRest(), "whole-rest", "Whole Rest", "tool-bar-btn", font);
        halfRestBtn = ButtonFactory.createButton(FontFactory.getHalfRest(), "half-rest", "Half Rest", "tool-bar-btn", font);
        quarterRestBtn = ButtonFactory.createButton(FontFactory.getQuarterRest(), "quarter-rest", "Quarter Rest", "tool-bar-btn", font);
        quaverRestBtn = ButtonFactory.createButton(FontFactory.getEighthRest(), "eight-n-rest", "Quaver Rest", "tool-bar-btn", font);
        semiquaverRestBtn = ButtonFactory.createButton(FontFactory.getSixteenthRest(), "sixteenth-rest", "Sixteenth Rest", "tool-bar-btn", font);
        thirtyTwoRestBtn = ButtonFactory.createButton(FontFactory.getThirtySecondRest(), "thirty-two-rest", "ThirtyTwo Note", "tool-bar-btn", font);

        sharpBtn = ButtonFactory.createButton(FontFactory.getSharp(), "sharp", "Sharp", "tool-bar-btn", font);
        flatBtn = ButtonFactory.createButton(FontFactory.getFlat(), "flat", "Flat", "tool-bar-btn", font);
        naturalBtn = ButtonFactory.createButton(FontFactory.getNatural(), "natural", "Natural", "tool-bar-btn", font);
        doubleSharpBtn = ButtonFactory.createButton(FontFactory.getDoubleSharp(), "double-sharp", "Double Sharp", "tool-bar-btn", font);
        doubleFlatBtn = ButtonFactory.createButton(FontFactory.getDoubleFlat(), "double-flat", "Double Flat", "tool-bar-btn", font);

        dotBtn = ButtonFactory.createButton(FontFactory.getDot(), "dot", "Dot", "tool-bar-btn", font);
    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("tool-bar");
    }

    @Override
    public void setupLayout() {
        createSpacer(SpaceFactory.Direction.HORIZONTAL);

        this.getChildren().addAll(
                wholeNoteBtn,
                halfNoteBtn,
                quarterNoteBtn,
                quaverNoteBtn,
                semiquaverNoteBtn,
                thirtyTwoNoteBtn
        );

        addSeparator();

        this.getChildren().addAll(
                wholeRestBtn,
                halfRestBtn,
                quarterRestBtn,
                quaverRestBtn,
                semiquaverRestBtn,
                thirtyTwoRestBtn
        );

        addSeparator();

        this.getChildren().addAll(
                sharpBtn,
                flatBtn,
                naturalBtn,
                doubleSharpBtn,
                doubleFlatBtn
        );

        addSeparator();

        this.getChildren().addAll(
                dotBtn
        );

        createSpacer(SpaceFactory.Direction.HORIZONTAL);
    }

    @Override
    public void setupEventListeners() {
//        EventBus.getInstance().subscribe(InputModeOn.class, event -> {
//            wholeNoteBtn.setStyle("-fx-background-color: #fa6637");
//            halfNoteBtn.setStyle("-fx-background-color: #fa6637");
//            quarterNoteBtn.setStyle("-fx-background-color: #fa6637");
//            quaverNoteBtn.setStyle("-fx-background-color: #fa6637");
//        });
//        EventBus.getInstance().subscribe(InputModeOff.class, event -> {
//            wholeNoteBtn.setStyle("-fx-background-color: #F0EDE8;");
//            halfNoteBtn.setStyle("-fx-background-color: #F0EDE8;");
//            quarterNoteBtn.setStyle("-fx-background-color: #F0EDE8;");
//            quaverNoteBtn.setStyle("-fx-background-color: #F0EDE8;");
//        });
    }

    @Override
    public void setupEventHandlers() {
        wholeNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("whole")));
        halfNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("half")));
        quarterNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("quarter")));
        quaverNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("eighth")));
        semiquaverNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("16th")));
        thirtyTwoNoteBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddNoteRequestedEvent("32nd")));

        wholeRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("whole")));
        halfRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("half")));
        quarterRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("quarter")));
        quaverRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("eighth")));
        semiquaverRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("16th")));
        thirtyTwoRestBtn.setOnAction(e ->
                EventBus.getInstance().publish(new AddRestRequestedEvent("32nd")));
    }

    private void addSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.getStyleClass().add("separator");

        this.getChildren().add(separator);
    }
}
