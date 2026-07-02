package com.muse.editor.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.util.Debug;
import javafx.beans.property.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class AppConfig {
    private AppConfig() {}

    private static final StringProperty serverUrl            = new SimpleStringProperty();
    private static final StringProperty websocketUrl         = new SimpleStringProperty();
    private static final StringProperty saveLocation         = new SimpleStringProperty();
    private static final StringProperty saveShortcut         = new SimpleStringProperty();
    private static final StringProperty inputModeShortcut    = new SimpleStringProperty();
    private static final StringProperty closeProjectShortcut = new SimpleStringProperty();

    private static final BooleanProperty isAutoSaveEnabled = new SimpleBooleanProperty(true);

    private static final IntegerProperty serverMonitorDelay  = new SimpleIntegerProperty();
    private static final IntegerProperty loginWindowWidth    = new SimpleIntegerProperty();
    private static final IntegerProperty loginWindowHeight   = new SimpleIntegerProperty();
    private static final IntegerProperty mainWindowMinWidth  = new SimpleIntegerProperty();
    private static final IntegerProperty mainWindowMinHeight = new SimpleIntegerProperty();

    public static void load() {
        final ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = AppConfig.class.getResourceAsStream("/com/muse/editor/config/config.json")) {
            Objects.requireNonNull(is, "Configuration file not found");

            final JsonNode node = mapper.readTree(is);

            serverUrl.set(node.get("serverUrl").asText());
            websocketUrl.set(node.get("websocketUrl").asText());
            saveLocation.set(node.get("saveLocation").asText());
            saveShortcut.set(node.get("shortcuts").get("save").asText());
            inputModeShortcut.set(node.get("shortcuts").get("inputMode").asText());
            closeProjectShortcut.set(node.get("shortcuts").get("closeProject").asText());
            isAutoSaveEnabled.set(node.get("isAutoSaveEnabled").asBoolean());
            serverMonitorDelay.set(node.get("serverMonitorDelay").asInt());
            loginWindowWidth.set(node.get("loginWindowWidth").asInt());
            loginWindowHeight.set(node.get("loginWindowHeight").asInt());
            mainWindowMinWidth.set(node.get("mainWindowMinWidth").asInt());
            mainWindowMinHeight.set(node.get("mainWindowMinHeight").asInt());

            Debug.pass("App configuration successfully loaded.");

        } catch (IOException e) {
            Debug.fail("Configuration file not found");
            throw new RuntimeException(e);
        }
    }

    public static StringProperty serverUrlProperty() {
        return serverUrl;
    }

    public static StringProperty websocketUrlProperty() {
        return websocketUrl;
    }

    public static StringProperty saveLocationProperty() {
        return saveLocation;
    }

    public static StringProperty saveShortcutProperty() {
        return saveShortcut;
    }

    public static StringProperty inputModeShortcutProperty() {
        return inputModeShortcut;
    }

    public static StringProperty closeProjectShortcutProperty() {
        return closeProjectShortcut;
    }

    public static BooleanProperty isAutoSaveEnabledProperty() {
        return isAutoSaveEnabled;
    }

    public static int getServerMonitorDelay() {
        return serverMonitorDelay.get();
    }

    public static int getLoginWindowWidth() {
        return loginWindowWidth.get();
    }

    public static int getLoginWindowHeight() {
        return loginWindowHeight.get();
    }

    public static int getMainWindowMinWidth() {
        return mainWindowMinWidth.get();
    }

    public static int getMainWindowMinHeight() {
        return mainWindowMinHeight.get();
    }
}
