module com.norbjdk.museeditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.norbjdk.museeditor to javafx.fxml;
    exports com.norbjdk.museeditor;
}