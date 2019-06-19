module catering {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.jfoenix;

    opens catering to javafx.fxml;
    exports catering;
}