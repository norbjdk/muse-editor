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


    opens com.muse.editor.app to javafx.fxml;
    exports com.muse.editor.app;

    opens com.muse.editor.core.model.dto to com.fasterxml.jackson.databind;
    opens com.muse.editor.core.user to com.fasterxml.jackson.databind;
}