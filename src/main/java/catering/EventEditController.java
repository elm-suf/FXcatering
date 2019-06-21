package catering;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.grasp_controllers.Recipe;
import catering.businesslogic.grasp_controllers.Shift;
import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.managers.CateringAppManager;
import catering.businesslogic.receivers.CatEventReceiver;
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
    ObservableList<Task> tasks;

    @FXML
    private TableColumn<Task, Integer> task_index;
    @FXML
    private TableColumn<Task, String> task_recipe;
    SimpleObjectProperty<Task> obsSelectedTas;

    List<Task> allTasks;
    String shift;
    String user;
    Task selectedTask;
    @FXML
    private TableColumn<Task, String> task_is_assigned;
    @FXML
    private Button add_task_btn;

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

    private Recipe selectedRecipe;


    @FXML
    void initialize() {
        loadRecipes();
        initData();
        initView();
        initTaskList();
    }

    private void showDetailedView(Task selectedTask, boolean isNew) {
        detail_task.setVisible(true);
        recipe_combo.setVisible(isNew);
        recipe_txf.setVisible(!isNew);

        if (isNew) {
            System.out.println("isNew");

            recipe_combo.setOnAction(e -> {
                int selected = recipe_combo.getSelectionModel().getSelectedIndex();
                System.out.println(recipe_combo.getSelectionModel().getSelectedIndex());
                if (selected > 0
                ) {
                    this.selectedRecipe = recipes.get(selected);
                }
            });

            assign_task_btn.setOnAction(e -> addTask());

        } else {
            assign_task_btn.setOnMouseClicked(e -> assignTask(selectedTask, shift_combo.getSelectionModel().getSelectedItem(),
                    cook_combo.getSelectionModel().getSelectedItem(), quantity_txf.getText(), duration_txf.getText(),
                    difficulty_combo.getSelectionModel().getSelectedItem())
            );

            //================================================================\\
            int shiftindex = shifts.indexOf(selectedTask.getShift());
            int cookIndex;
            if (selectedTask.getShift() != null) {
                users = FXCollections.observableList(CateringAppManager.userManager.getUsersInShift(selectedTask.getShift()));
                cook_combo.setItems(users);
                System.out.println("users not null");
                cookIndex = users.indexOf(selectedTask.getCook());
                System.out.println("index " + cookIndex);
                cook_combo.getSelectionModel().select(cookIndex);
            } else {
                System.out.println("else");
                cook_combo.getSelectionModel().select(selectedTask.getCook());

                recipe_txf.setText(selectedTask.getRecipe().toString());
                difficulty_combo.getSelectionModel().select(selectedTask.getDifficulty());
                duration_txf.setText(String.valueOf(selectedTask.getDurationMinutes()));
                quantity_txf.setText(String.valueOf(selectedTask.getQuantity()));
            }
            shift_combo.getSelectionModel().select(shiftindex);
            //==================================================================\\

        }
        shift_combo.setOnAction(e -> {
            if (shift_combo.getSelectionModel().getSelectedItem() != null) {
                users = FXCollections.observableList(CateringAppManager.userManager.getUsersInShift(shift_combo.getSelectionModel().getSelectedItem()));
                cook_combo.setItems(users);
            }
        });


        //todo wire the save button
    }

    private void addTask() {

        System.out.println("Adding Task : " + selectedRecipe.getName());
        try {
            CateringAppManager.eventManager.addTask(selectedRecipe);
        } catch (AssignTaskException e) {
            //todo catch
            e.printStackTrace();
        }
    }

    private void assignTask(Task task, Shift shift, User cook, String quantity, String duration, String difficulty) {
        System.out.println(task + " shift: " + shift + " cook: " + cook + " quantity: " + quantity + "dur: " + duration + " diff: " + difficulty);
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
        tasks = FXCollections.observableList(allTasks);
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

    private void increasePosition() {
        if (selectedTask != null) {
            int currentIndex = selectedTask.getIndex();
            CateringAppManager.eventManager.sortTask(selectedTask, ++currentIndex);
        }
    }

    private void decreasePositionIfNotZero() {
        if (selectedTask != null) {
            int currentIndex = selectedTask.getIndex();
            CateringAppManager.eventManager.sortTask(selectedTask, --currentIndex);
        }
    }

    void refreshTable() {
        tasks.clear();
        tasks = FXCollections.observableList(CateringAppManager.eventManager.getAllTasks());
        task_list.setItems(tasks);
    }

    private void initView() {
//        assign_task_btn.setOnAction(e -> showDetailedView(new Task(), true));
        add_task_btn.setOnAction(e -> showAddTaskView(new Task()));

        position_up_btn.setOnAction(e -> increasePosition());
        position_down_btn.setOnAction(e -> decreasePositionIfNotZero());
        detail_task.setVisible(false);

        //todo check set selected task


        event_edit_back_btn.setOnMouseClicked(e -> goBack());
//        task_is_assigned.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isAssigned()));
        task_is_completed.setCellValueFactory(tc -> new SimpleBooleanProperty(tc.getValue().isCompleted()));
        task_index.setCellValueFactory(tc -> new SimpleIntegerProperty(tc.getValue().getIndex()).asObject());
        task_recipe.setCellValueFactory(tc -> new SimpleObjectProperty<>(tc.getValue().getRecipe().toString()));

        task_is_assigned.setCellValueFactory(tc -> {
            if (tc.getValue().getCook() != null)
                return new SimpleObjectProperty<>(tc.getValue().getCook().getName());
            else
                return new SimpleObjectProperty<>("Unassigned");
        });

        task_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        task_list.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            selectedTask = task_list.getSelectionModel().getSelectedItem();
            if (selectedTask != null)
                showDetailedView(selectedTask, false);
        });
    }

    private void showAddTaskView(Task task) {
//        detail_task.setVisible(true);

        System.out.println("showAddTaskView();");
        showDetailedView(task, true);

    }

    private void initData() {
        difficulty_combo.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList(
                "Facile", "Medio", "Difficile"
        ))));

        shifts = FXCollections.observableList(CateringAppManager.shiftManager.getShifts());
        shift_combo.setItems(shifts);

        recipes = FXCollections.observableList(CateringAppManager.recipeManager.getRecipes());
        recipe_combo.setItems(recipes);


        CateringAppManager.eventManager.addReceiver(new CatEventReceiver() {
            @Override
            public void notifyTaskAdded(Task task) {

            }

            @Override
            public void notifyTaskRemoved(Task task) {

            }

            @Override
            public void notifyTaskSorted(Task task) {
                selectedTask = task;
                refreshTable();
                task_list.getSelectionModel().select(task);
            }

            @Override
            public void notifyTaskAssigned(Task task) {
                selectedTask = task;
                refreshTable();
                task_list.getSelectionModel().select(task);
//                showDetailedView(task, false);
            }


            @Override
            public void notifyTaskAssignmentDeleted(Task task, User cook) {

            }
        });
    }
}
