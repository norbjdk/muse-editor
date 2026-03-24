module com.norbjdk.museeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires nu.xom;
    requires java.xml;


    opens com.norbjdk.museeditor.app to javafx.fxml;
    exports com.norbjdk.museeditor.app;
}