package catering;

import catering.businesslogic.grasp_controllers.Menu;
import catering.businesslogic.managers.CateringAppManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MenuListController {

    private List<Menu> menus;
    private ObservableList<Menu> observableMenus;
    private MenuEditController menuEditController;
    private Menu selectedMenu;

    @FXML
    private ListView<Menu> menuList;

    @FXML
    private BorderPane mainContainer;

    @FXML
    private BorderPane menuListPane;

    private BorderPane menuEditPane;

    @FXML
    Button editMenuButton;

    @FXML
    Button copyMenuButton;

    @FXML
    Button deleteMenuButton;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuedit.fxml"));
            menuEditPane = loader.load();
            menuEditController = loader.getController();
            menuEditController.listen((publish -> {
                if (publish) {
                    CateringAppManager.menuManager.publish();
                }
                this.resetMenuList();
                mainContainer.setCenter(menuListPane);
            }));

        } catch (IOException exc) {
            exc.printStackTrace();
        }

        menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.resetMenuList();


        menuList.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            selectedMenu = menuList.getSelectionModel().getSelectedItem();
            boolean ownershipOk = (selectedMenu != null) && (selectedMenu.getOwner().equals(CateringAppManager.userManager.getCurrentUser()));
            boolean editable = (selectedMenu != null) && !selectedMenu.isInUse() && !selectedMenu.isPublished();
            copyMenuButton.setDisable(selectedMenu == null);
            editMenuButton.setDisable(!ownershipOk || !editable);
            deleteMenuButton.setDisable(!ownershipOk || !editable);
        });
    }

    private void resetMenuList() {
        menus = CateringAppManager.menuManager.getAllMenus();
        observableMenus = FXCollections.observableList(menus);
        menuList.setItems(observableMenus);
    }

    @FXML
    private void newMenuAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Nuovo menu");
        dialog.setHeaderText("Creazione di un menu");
        dialog.setContentText("Inserisci opzionalmente un titolo:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(title -> {
            CateringAppManager.menuManager.createMenu(title);
            menuEditController.setup();
            mainContainer.setCenter(menuEditPane);
        });
    }

    @FXML
    private void editMenuAction() {
        CateringAppManager.menuManager.chooseMenu(this.selectedMenu);
        menuEditController.setup();
        mainContainer.setCenter(menuEditPane);
    }

    @FXML
    private void copyMenuAction() {
        CateringAppManager.menuManager.copyMenu(this.selectedMenu);
        menuEditController.setup();
        mainContainer.setCenter(menuEditPane);

    }

    @FXML
    private void deleteMenuAction() {
        CateringAppManager.menuManager.deleteMenu(this.selectedMenu);
        this.resetMenuList();
    }
}