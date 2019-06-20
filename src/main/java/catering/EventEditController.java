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
import java.util.ArrayList;
import java.util.Arrays;
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
    Button assign_task_btn;

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
    private ObservableList<Recipe> recipes;

    @FXML
    private ComboBox<Shift> shift_combo;
    private ObservableList<Shift> shifts;

    @FXML
    private ComboBox<User> cook_combo;
    private ObservableList<User> users;

    @FXML
    private TextField quantity_txf;

    @FXML
    private TextField duration_txf; //difficulty_combo

    @FXML
    private ComboBox<String> difficulty_combo; //difficulty_combo

    @FXML
    private Button position_down_btn;

    @FXML
    private Button position_up_btn;

    @FXML
    private Label position_lbl;


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
//            difficulty_combo.setText("");
            duration_txf.setText("");
            quantity_txf.setText("");
            loadRecipes();

        } else {
            recipe_combo.setVisible(false);
            recipe_txf.setVisible(true);
            System.out.println("Selectd : " + selectedTask);
            recipe_txf.setText(selectedTask.getRecipe().toString());
            position_lbl.setText("Posizione :" + selectedTask.getIndex());
        }

        difficulty_combo.setItems(FXCollections.observableList(new ArrayList<String>(Arrays.asList(
                "Facile", "Medio", "Difficile"
        ))));

        shifts = FXCollections.observableList(CateringAppManager.shiftManager.getShifts());
        shift_combo.setItems(shifts);

        shift_combo.setOnAction(e -> {
            users = FXCollections.observableList(CateringAppManager.userManager.getUsersInShift(shift_combo.getSelectionModel().getSelectedItem()));
            System.out.println(users);
            cook_combo.setItems(users);
        });


        //todo wire the save button
        Task task = selectedTask;//recipe_combo.getSelectionModel().getSelectedItem()
        Shift shift = shift_combo.getSelectionModel().getSelectedItem();
        User cook = cook_combo.getSelectionModel().getSelectedItem();

        String quantity = quantity_txf.getText();
        String difficulty = difficulty_combo.getSelectionModel().getSelectedItem();
        String duration = duration_txf.getText();

        assign_task_btn.setOnMouseClicked(e -> assignTask(task, shift, cook, quantity, duration, difficulty));
    }

    private void assignTask(Task task, Shift shift, User cook, String quantity, String duration, String difficulty) {
        CateringAppManager.eventManager.assignTask(task, shift, cook, quantity, duration, difficulty);
    }

    private void loadRecipes() {
        List<Recipe> allRec = CateringAppManager.recipeManager.getRecipes();
//        allRec.removeIf(recipe -> !recipe.isDish());
        recipes = FXCollections.observableList(allRec);
        recipe_combo.setItems(recipes);
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
