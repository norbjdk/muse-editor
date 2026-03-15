package com.norbjdk.museeditor.ui.component;

import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.util.ButtonFactory;
import com.norbjdk.museeditor.ui.util.FontFactory;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Objects;

public class ToolBox extends VBox implements Presentable {
    private Accordion toolAccordion;

    private TitledPane clefs;
    private TitledPane dynamics;
    private TitledPane tempo;
    private TitledPane metre;

    private Button gClefBtn;
    private Button fClefBtn;
    private Button cClefBtn;

    private Button pppBtn;
    private Button ppBtn;
    private Button pBtn;
    private Button fffBtn;
    private Button ffBtn;
    private Button fBtn;
    private Button mpBtn;
    private Button mfBtn;

    private final Font font;

    public ToolBox() {
        font = FontFactory.getBravura(32);

        present();
    }

    @Override
    public void initComponents() {
        toolAccordion = new Accordion();
        clefs = new TitledPane();
        dynamics = new TitledPane();
        tempo = new TitledPane();
        metre = new TitledPane();

        gClefBtn = ButtonFactory.createButton(FontFactory.getGClef(), "gClef-btn", "Treble Clef", "tool-bar-btn", font);
        fClefBtn = ButtonFactory.createButton(FontFactory.getFClef(), "fClef-btn", "Bass Clef", "tool-bar-btn", font);
        cClefBtn = ButtonFactory.createButton(FontFactory.getGClef(), "cClef-btn", "Alto Clef", "tool-bar-btn", font);

        pBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.P), "p", "P", "tool-bar-btn", font);
        fBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.F), "f", "F", "tool-bar-btn", font);
        mpBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.MP), "mp", "MP", "tool-bar-btn", font);
        mfBtn = ButtonFactory.createButton(FontFactory.getDynamic(FontFactory.Dynamic.MF), "mf", "MF", "tool-bar-btn", font);
    }

    @Override
    public void setupComponents() {
        clefs.setText("Clefs");
        dynamics.setText("Dynamics");
        tempo.setText("Tempo");
        metre.setText("Metre");

        final GridPane clefsContainer = new GridPane(8, 8);
        final GridPane dynamicsContainer = new GridPane(8, 8);
        final GridPane tempoContainer = new GridPane();
        final GridPane metreContainer = new GridPane();

        clefsContainer.add(gClefBtn, 0, 0);
        clefsContainer.add(fClefBtn, 1, 0);
        clefsContainer.add(cClefBtn, 0, 1);

        dynamicsContainer.add(pBtn, 0, 0);
        dynamicsContainer.add(fBtn, 1, 0);
        dynamicsContainer.add(mpBtn, 0, 1);
        dynamicsContainer.add(mfBtn, 1, 1);

        clefs.setCollapsible(true);
        dynamics.setCollapsible(true);
        tempo.setCollapsible(true);
        metre.setCollapsible(true);

        clefs.setContent(clefsContainer);
        dynamics.setContent(dynamicsContainer);

        toolAccordion.setExpandedPane(clefs);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("tool-box");
    }

    @Override
    public void setupLayout() {
        toolAccordion.getPanes().addAll(
            clefs,
            dynamics,
            tempo,
            metre
        );

        this.getChildren().add(toolAccordion);
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
