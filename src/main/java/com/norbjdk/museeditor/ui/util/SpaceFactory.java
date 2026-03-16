package com.norbjdk.museeditor.ui.util;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * SpaceFactory for creating space element that resize itself
 * Object with no instance possibility, just static methods
 *
 * @see <a href="https://github.com/norbjdk/muse-editor">GitHub</a>
 * @author norbjdk
 */

public class SpaceFactory {

    /**
     * Direction to define if for VBox or for HBox spacer is needed
     */

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }
    private SpaceFactory() {}

    /**
     *
     * @param direction for the element grow.
     * @return grown spacing element that can be used in container
     */

    public static Region createSpacer(Direction direction) {
        final Region region = new Region();

        switch (direction) {
            case VERTICAL -> VBox.setVgrow(region, Priority.ALWAYS);
            case HORIZONTAL -> HBox.setHgrow(region, Priority.ALWAYS);
        }

        return region;
    }
}
