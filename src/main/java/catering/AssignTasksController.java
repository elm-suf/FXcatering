package catering;

import catering.businesslogic.CatEvent;
import catering.businesslogic.CateringAppManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class AssignTasksController implements Initializable {

    @FXML
    private Label title_label;

    @FXML
    private JFXButton assign_btn;

    @FXML
    private JFXListView<CatEvent> events_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        title_label.setText("Bentornato " + CateringAppManager.userManager.getCurrentUser().toString());
        //todo fare chiamata a datamanger per ottenere la lista di eventi
        events_listview.setItems(FXCollections.observableList(
                new ArrayList<>(Arrays.asList(  new CatEvent(1, "evento 1"),
                                                new CatEvent(2, "evento 2"),
                                                new CatEvent(3, "evento 3")))));
    }
}
