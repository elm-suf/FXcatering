package catering;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.grasp_controllers.CatEvent;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.managers.CateringAppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AssignTasksController implements Initializable {

    @FXML
    private Label title_label;

    @FXML
    private AnchorPane root_pane;

    @FXML
    private Button assign_btn;

    @FXML
    private ListView<CatEvent> events_listview;

    private AnchorPane editEvent;
    private EventEditController eventEditController;

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
            try {
                CateringAppManager.eventManager.selectEvent(selectedEvent);
            } catch (AssignTaskException e) {
                e.printStackTrace();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("event_edit.fxml"));
                editEvent = loader.load();
                eventEditController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
            root_pane.getChildren().setAll(editEvent);
        });
    }

    private void initList() {
        events = CateringAppManager.eventManager.getAllEvents(currentUser);
        ObservableList<CatEvent> catEvents = FXCollections.observableList(events);
        events_listview.setItems(catEvents);
    }
}
