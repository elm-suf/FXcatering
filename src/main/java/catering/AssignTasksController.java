package catering;

import catering.businesslogic.grasp_controllers.CatEvent;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.managers.CateringAppManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AssignTasksController implements Initializable {

    @FXML
    private Label title_label;

    @FXML
    private JFXButton assign_btn;

    @FXML
    private JFXListView<CatEvent> events_listview;

    List<CatEvent> events;
    private User currentUser;
    private CatEvent selectedEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentUser = CateringAppManager.userManager.getCurrentUser();
        title_label.setText("Bentornato " + currentUser.toString());
        //todo fare chiamata a datamanger per ottenere la lista di eventi

        events_listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.initList();

        events_listview.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            selectedEvent = events_listview.getSelectionModel().getSelectedItem();
            assign_btn.setDisable(selectedEvent == null);
        });
        assign_btn.setOnMouseClicked((ev) -> {
            System.out.println(selectedEvent);
        });
    }

    private void initList() {
        events = CateringAppManager.eventManager.getAllEvents(currentUser);
        ObservableList<CatEvent> catEvents = FXCollections.observableList(events);
        events_listview.setItems(catEvents);
    }
}
