package catering;

import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.managers.CateringAppManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

/**
 * @noinspection Duplicates
 */
public class EventEditController {

    AnchorPane newAnchorPane;
    @FXML
    private AnchorPane edit_root_pane;
    @FXML
    private AnchorPane all_tasks_pane;
    @FXML
    private TableView<Task> task_list;

    @FXML
    private TableColumn<Task, Integer> task_index;
    @FXML
    private TableColumn<Task, String> task_recipe;
    @FXML
    private TableColumn<Task, Boolean> task_is_assigned;
    List<Task> allTasks;
    Task selectedTask;

    @FXML
    private Button cestino;

    @FXML
    private Button event_edit_back_btn;
    @FXML
    private TableColumn<Task, Boolean> task_is_completed;
    @FXML
    private AnchorPane detail_task;

    @FXML
    void initialize() {
        initTaskList();
        event_edit_back_btn.setOnMouseClicked(e -> goBack());
        task_recipe.setCellValueFactory(new PropertyValueFactory<>("recipe"));
        task_is_assigned.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isAssigned()));
        task_is_completed.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isCompleted()));
        task_index.setCellValueFactory(new PropertyValueFactory<>("index"));

        task_list.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            selectedTask = task_list.getSelectionModel().getSelectedItem();
        });


    }

    private void initTaskList() {
        this.allTasks = CateringAppManager.eventManager.getAllTasks();
        ObservableList<Task> tasks = FXCollections.observableList(allTasks);
        task_list.setItems(tasks);
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("assign_tasks_main.fxml"));
            newAnchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        edit_root_pane.getChildren().setAll(newAnchorPane);
    }


}
