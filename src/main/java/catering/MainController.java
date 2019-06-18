package catering;

import catering.businesslogic.managers.CateringAppManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label userName;

    @FXML
    public void initialize() {
        userName.setText(CateringAppManager.userManager.getCurrentUser().toString());

        try {
            FXMLLoader menuListLoader = new FXMLLoader(getClass().getResource("menulist.fxml"));
            Parent menuList = menuListLoader.load();
            MenuListController menuListController = menuListLoader.getController();

            mainPane.setCenter(menuList);
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }
}