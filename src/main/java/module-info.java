module catering {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.jfoenix;

    opens catering to javafx.fxml;
    exports catering;
}