module com.norbjdk.museeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires nu.xom;
    requires java.xml;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;
    requires annotations;
    requires java.desktop;

    opens com.muse.editor.develop.app to javafx.fxml;
    exports com.muse.editor.develop.app;

    opens com.muse.editor.redevelop.app to javafx.fxml;
    exports com.muse.editor.redevelop.app;

    opens com.muse.editor.develop.core.model.dto to com.fasterxml.jackson.databind;
    opens com.muse.editor.develop.core.user to com.fasterxml.jackson.databind;
    exports com.muse.editor.redevelop.app.window;
    opens com.muse.editor.redevelop.app.window to javafx.fxml;
    exports com.muse.editor.redevelop.core.api;
    opens com.muse.editor.redevelop.core.api to javafx.fxml;
}