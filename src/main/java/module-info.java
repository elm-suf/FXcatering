module catering {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;

    opens catering to javafx.fxml;
    exports catering;
}