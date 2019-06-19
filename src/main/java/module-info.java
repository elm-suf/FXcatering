module catering {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens catering to javafx.fxml;
    exports catering;
}