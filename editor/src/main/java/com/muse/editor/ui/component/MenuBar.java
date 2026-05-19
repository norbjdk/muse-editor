package com.muse.editor.ui.component;

import com.muse.editor.app.AppManager;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class MenuBar extends HBox implements Presentable {

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

        final MenuItem newFile = new MenuItem("New");
        final MenuItem openFile = new MenuItem("Open");
        final MenuItem recentProjects = new MenuItem("Recent Projects");
        final MenuItem closeProject = new MenuItem("Close project");
        final MenuItem saveFile = new MenuItem("Save");
        final MenuItem saveAsFile = new MenuItem("Save as");
        final MenuItem exportFile = new MenuItem("Export");
        final MenuItem printFile = new MenuItem("Print");
        final MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(
                newFile,
                openFile,
                recentProjects,
                closeProject,
                saveFile,
                saveAsFile,
                exportFile,
                printFile,
                exit
        );

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
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
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
        AppManager.getInstance().isConnected().addListener((obs, oldV, newV) -> {
            Platform.runLater(() -> {
                if (newV) {
                    statusLabel.setText("running");
                } else {
                    statusLabel.setText("down");
                }
            });
        });
    }

    @Override
    public void setupEventHandlers() {

    }
}
