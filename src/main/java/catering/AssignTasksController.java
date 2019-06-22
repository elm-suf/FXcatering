package catering;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.grasp_controllers.CatEvent;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.managers.CateringAppManager;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AssignTasksController implements Initializable {

    @FXML
    JFXListView<CatEvent> jfx_list;

    @FXML
    private Label title_label;

    @FXML
    private AnchorPane root_pane;


    ObservableList<CatEvent> catEvents;

    private AnchorPane editEvent;
    private EventEditController eventEditController;

    List<CatEvent> events;
    private User currentUser;
    private CatEvent selectedEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        initView();
    }

    private void initData() {
        this.currentUser = CateringAppManager.userManager.getCurrentUser();

        events = CateringAppManager.eventManager.getAllEvents(currentUser);
        catEvents = FXCollections.observableList(events);
    }

    private void initView() {
        title_label.setText("Bentornato " + currentUser.toString());

        jfx_list.setFixedCellSize(52.0);
        jfx_list.setItems(catEvents);
        jfx_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        jfx_list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number value, Number newVal) {
                if (newVal.intValue() >= 0)
                    openEditEvent(events.get(newVal.intValue()));
            }
        });
    }

    private void openEditEvent(CatEvent prop) {
        if (jfx_list.getSelectionModel().getSelectedIndex() >= 0) {
            try {
                CateringAppManager.eventManager.selectEvent(prop);
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
        }
    }
}
