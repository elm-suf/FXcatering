package catering;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.grasp_controllers.Recipe;
import catering.businesslogic.managers.CateringAppManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    @FXML
    private JFXComboBox<Recipe> jfx_combo;

    @FXML
    private JFXButton jfx_button;

    @FXML
    public Button commit_addtask_btn;

    @FXML
    private AnchorPane add_task_root;

    private ObservableList<Recipe> allRecipes;

    public AddTaskController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        jfx_combo.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList("facile", "facile", "facile"))));
        System.out.println("intialize");
        if (allRecipes == null)
            loadRecipes();

        initView();
    }

    private void initView() {
        jfx_button.setOnAction(e -> {
            try {
                int index = jfx_combo.getSelectionModel().getSelectedIndex();
                if (index == -1)
                    return;
                CateringAppManager.eventManager.addTask(allRecipes.get(index));
            } catch (AssignTaskException ex) {
                System.out.println(ex.toString());
                ex.printStackTrace();
            }
        });
    }

    private void loadRecipes() {
        allRecipes = FXCollections.observableList(CateringAppManager.recipeManager.getRecipes());
        jfx_combo.setItems(allRecipes);
    }
}
