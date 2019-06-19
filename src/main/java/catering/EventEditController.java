package catering;

import catering.businesslogic.grasp_controllers.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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

    @FXML
    private AnchorPane detail_task;

    @FXML
    private Button cestino;

    @FXML
    private Button event_edit_back_btn;
    @FXML
    private TableColumn<Task, Boolean> task_is_completed;

    @FXML
    void initialize() {
        event_edit_back_btn.setOnMouseClicked(e -> goBack());
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
