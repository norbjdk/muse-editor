package com.muse.editor.develop.ui.util;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

/**
 * ButtonFactory for creating button with specific requirements and for adding icon to button
 * Object with no instance possibility, just static methods
 *
 * @see <a href="https://github.com/norbjdk/muse-editor">GitHub</a>
 * @author norbjdk
 */

public class ButtonFactory {

    private ButtonFactory() {}

    public static Button createButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        return button;
    }

    public static Button createButton(String text, String id, String tooltip) {
        Button button = createButton(text, id);
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String style) {
        final Button button = new Button(text);
        button.setId(id);
        button.setTooltip(new Tooltip(tooltip));
        button.getStyleClass().add(style);
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String style, Font font) {
        final Button button = createButton(text, id, tooltip, style);
        button.setFont(font);

        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String [] classStyles) {
        Button button = new Button(text);
        button.setId(id);
        button.setTooltip(new Tooltip(tooltip));
        button.getStyleClass().addAll(Arrays.asList(classStyles));
        return button;
    }

    public static Button createButton(String text, String id, String tooltip, String [] classStyles, Font font) {
        Button button = createButton(text, id, tooltip, classStyles);
        button.setFont(font);
        return button;
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(10);
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(display);
        button.setGraphicTextGap(10);
    }

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display, int textGap) {
        FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);
        button.setGraphic(icon);
        button.setContentDisplay(display);
        button.setGraphicTextGap(textGap);
    }
}
