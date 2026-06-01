package com.muse.editor.ui.component;

import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import com.muse.editor.ui.util.FontFactory;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Objects;

public class ToolBox extends VBox implements Presentable {
    private TitledPane clefsPane;
    private TitledPane dynamicsPane;
    private TitledPane metresPane;

    private TilePane clefButtonsContainer;
    private TilePane dynamicButtonsContainer;
    private TilePane metreButtonsContainer;

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

    private final Font font;

    public ToolBox() {
        font = FontFactory.getBravura(32);

        present();
    }

    @Override
    public void initComponents() {
        clefsPane    = new TitledPane();
        dynamicsPane = new TitledPane();
        metresPane   = new TitledPane();

        clefButtonsContainer    = new TilePane();
        dynamicButtonsContainer = new TilePane();
        metreButtonsContainer   = new TilePane();

        GClefBtn = ButtonFactory.createButton(FontFactory.getGClef(), "gClef-btn", "Treble Clef", "tool-bar-btn", font);
        FClefBtn = ButtonFactory.createButton(FontFactory.getFClef(), "fClef-btn", "Bass Clef", "tool-bar-btn", font);
        CClefBtn = ButtonFactory.createButton(FontFactory.getCClef(), "cClef-btn", "Alto Clef", "tool-bar-btn", font);

        pBtn  = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.P), "p", "P", "tool-bar-btn", font);
        fBtn  = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.F), "f", "F", "tool-bar-btn", font);
        mpBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.MP), "mp", "MP", "tool-bar-btn", font);
        mfBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.MF), "mf", "MF", "tool-bar-btn", font);

        twoFourBtn     = ButtonFactory.createButton(FontFactory.getTimeSignature(FontFactory.Metre.TWO_FOUR), "2/4", "2/4", "tool-bar-btn", font);
        threeFourBtn   = ButtonFactory.createButton(FontFactory.getTimeSignature(FontFactory.Metre.THREE_FOUR), "3/4f", "3/4", "tool-bar-btn", font);
        fourFourBtn    = ButtonFactory.createButton(FontFactory.getTimeSignature(FontFactory.Metre.FOUR_FOUR), "4/4", "4/4", "tool-bar-btn", font);
        sixEightBtn    = ButtonFactory.createButton(FontFactory.getTimeSignature(FontFactory.Metre.SIX_EIGHT), "6/8", "6/8", "tool-bar-btn", font);
        twelveEightBtn = ButtonFactory.createButton(FontFactory.getTimeSignature(FontFactory.Metre.TWELVE_EIGHT), "12/8", "12/8", "tool-bar-btn", font);
    }

    @Override
    public void setupComponents() {
        clefsPane.setText("Clefs");
        dynamicsPane.setText("Dynamics");
        metresPane.setText("Metres");

        metresPane.setExpanded(false);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("tool-box");

        clefsPane.getStyleClass().add("expandable-tools-container");
        dynamicsPane.getStyleClass().add("expandable-tools-container");
        metresPane.getStyleClass().add("expandable-tools-container");

        clefButtonsContainer.getStyleClass().add("tool-buttons-container");
        dynamicButtonsContainer.getStyleClass().add("tool-buttons-container");
        metreButtonsContainer.getStyleClass().add("tool-buttons-container");
    }

    @Override
    public void setupLayout() {
        clefButtonsContainer.getChildren().addAll(
                GClefBtn,
                FClefBtn,
                CClefBtn
        );

        dynamicButtonsContainer.getChildren().addAll(
                pBtn,
                fBtn,
                mpBtn,
                mfBtn
        );

        metreButtonsContainer.getChildren().addAll(
                twoFourBtn,
                threeFourBtn,
                fourFourBtn,
                sixEightBtn,
                twelveEightBtn
        );

        clefsPane.setContent(clefButtonsContainer);
        dynamicsPane.setContent(dynamicButtonsContainer);
        metresPane.setContent(metreButtonsContainer);

        this.getChildren().addAll(
                clefsPane,
                dynamicsPane,
                metresPane
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
