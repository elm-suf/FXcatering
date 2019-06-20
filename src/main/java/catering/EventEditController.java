package catering;

import catering.businesslogic.grasp_controllers.Recipe;
import catering.businesslogic.grasp_controllers.Shift;
import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.managers.CateringAppManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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


    //    Detail Task FXML
    @FXML
    private TextField recipe_txf;

    @FXML
    private ComboBox<Recipe> recipe_combo;

    @FXML
    private ComboBox<Shift> shift_combo;
    private ObservableList<Shift> shifts;

    @FXML
    private ComboBox<User> cook_combo;

    @FXML
    private TextField quantity_txf;

    @FXML
    private ComboBox<?> duration_combo;

    @FXML
    private TextField difficulty_txf;

    @FXML
    private Button position_down_btn;

    @FXML
    private Button position_up_btn;



    @FXML
    void initialize() {
        initTaskList();
        detail_task.setVisible(false);
        event_edit_back_btn.setOnMouseClicked(e -> goBack());
        task_is_assigned.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isAssigned()));
        task_is_completed.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isCompleted()));
        task_index.setCellValueFactory(tc -> new SimpleIntegerProperty(tc.getValue().getIndex()).asObject());
        task_recipe.setCellValueFactory(tc -> new SimpleObjectProperty<>(tc.getValue().getRecipe().toString()));

        task_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        task_list.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            selectedTask = task_list.getSelectionModel().getSelectedItem();
            //todo
            showDetailedView(selectedTask, false);
        });

        cestino.setOnMouseClicked(e -> addTask());
    }

    private void addTask() {
//        try {
        this.showDetailedView(new Task(), true);
//            CateringAppManager.eventManager.addTask(selectedTask.getRecipe());
//        } catch (AssignTaskException e) {
//            e.printStackTrace();
//        }
    }

    private void showDetailedView(Task selectedTask, boolean isNew) {
        detail_task.setVisible(true);
        if (isNew) {
            recipe_combo.setVisible(true);
            recipe_txf.setVisible(false);
            difficulty_txf.setText("");
            quantity_txf.setText("");

        } else {
            recipe_combo.setVisible(false);
            recipe_txf.setVisible(true);
            System.out.println("Selectd : " + selectedTask);
            recipe_txf.setText(selectedTask.getRecipe().toString());

        }

        shifts = FXCollections.observableList(CateringAppManager.shiftManager.getShifts());

        shift_combo.setItems(shifts);

        //todo load cook in cook_combo
        //todo load shifts in shift_combo
        //todo setItems duration_combo
        //todo wire the save button
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
