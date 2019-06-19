package catering;

import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.managers.CateringAppManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.List;

public class EventEditController {

    ObservableList<Task> observableTasks;
    List<Task> tasks;
    @FXML
    private JFXListView<Task> tasks_listview;
    @FXML
    private JFXButton add_task_btn;
    @FXML
    private JFXButton delete_task_btn;
    @FXML
    private JFXButton assign_task_btn;
    @FXML
    private JFXButton delete_assignment_btn;

    @FXML
    public void initialize() {

        this.initList();

    }

    private void initList() {
        tasks = CateringAppManager.eventManager.getAllTasks();
        observableTasks = FXCollections.observableList(tasks);
        tasks_listview.setItems(observableTasks);
    }

}
