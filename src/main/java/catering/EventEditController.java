package catering;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EventEditController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane root_pane;

    @FXML
    private TableView<?> task_list;

    @FXML
    private TableColumn<?, ?> task_index;

    @FXML
    private TableColumn<?, ?> task_recipe;

    @FXML
    private TableColumn<?, ?> task_is_assigned;

    @FXML
    private TableColumn<?, ?> task_is_completed;

    @FXML
    private AnchorPane detail_task;

    @FXML
    private Button cestino;

    @FXML
    private Button event_edit_back_btn;

    @FXML
    void deleteMail(ActionEvent event) {

    }

    @FXML
    void initialize() {
    }
}
