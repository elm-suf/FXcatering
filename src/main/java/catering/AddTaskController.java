package catering;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.grasp_controllers.Recipe;
import catering.businesslogic.managers.CateringAppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    @FXML
    public Button commit_addtask_btn;

    @FXML
    private AnchorPane add_task_root;

    @FXML
    private ComboBox<Recipe> combo_recepies;
    private ObservableList<Recipe> allRecipes;

    public AddTaskController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("intialize");
        if (allRecipes == null)
            loadRecipes();

        initView();


    }

    private void initView() {
        commit_addtask_btn.setOnAction(e -> {
            System.out.println("ID= " + combo_recepies.getSelectionModel().getSelectedItem().getId());
            try {
                int index = combo_recepies.getSelectionModel().getSelectedIndex();

                CateringAppManager.eventManager.addTask(allRecipes.get(index));
            } catch (AssignTaskException ex) {
                System.out.println(ex.toString());
                ex.printStackTrace();
            }
        });
    }

    private void loadRecipes() {
        allRecipes = FXCollections.observableList(CateringAppManager.recipeManager.getRecipes());
        combo_recepies.setItems(allRecipes);
    }
}
