package com.norbjdk.museeditor.ui.component;

import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.util.SpaceFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

import static com.norbjdk.museeditor.ui.util.SpaceFactory.createSpacer;

public class MenuBar extends HBox implements Presentable {

    private final BooleanProperty isServerDown = new SimpleBooleanProperty(true);

    private Label serverStatusLabel;
    private Label statusLabel;
    private HBox statusContainer;
    private javafx.scene.control.MenuBar menuBar;

    public MenuBar() {
        present();
    }

    @Override
    public void initComponents() {
        menuBar = new javafx.scene.control.MenuBar();
        serverStatusLabel = new Label();
        statusLabel = new Label();
        statusContainer = new HBox(2);
    }

    @Override
    public void setupComponents() {
        serverStatusLabel.setText("Server status:");
        statusLabel.setText("down");

        final FontIcon serverIcon = new FontIcon(FontAwesomeSolid.GLOBE);
        serverIcon.setIconSize(14);
        serverIcon.setIconColor(Color.rgb(5, 5, 5));

        serverStatusLabel.setGraphic(serverIcon);
        serverStatusLabel.setContentDisplay(ContentDisplay.LEFT);
        serverStatusLabel.setGraphicTextGap(4);

        statusContainer.getChildren().addAll(
                serverStatusLabel,
                statusLabel
        );

        final Menu fileMenu = new Menu("File");
        final Menu editMenu = new Menu("Edit");
        final Menu viewMenu = new Menu("View");
        final Menu addMenu = new Menu("Add");
        final Menu formatMenu = new Menu("Format");
        final Menu toolsMenu = new Menu("Tools");
        final Menu helpMenu = new Menu("Help");

        menuBar.getMenus().addAll(
                fileMenu,
                editMenu,
                viewMenu,
                addMenu,
                formatMenu,
                toolsMenu,
                helpMenu
        );
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("menu-bar");

        statusContainer.getStyleClass().add("status-container");
    }

    @Override
    public void setupLayout() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(5);
        this.getChildren().addAll(
                menuBar,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                statusContainer
        );
    }

    @Override
    public void setupEventListeners() {
        isServerDown.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                statusLabel.setText("running");
            } else {
                statusLabel.setText("down");
            }
        });
    }

    @Override
    public void setupEventHandlers() {

    }
}
