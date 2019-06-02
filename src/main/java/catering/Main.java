package catering;

import catering.businesslogic.CateringAppManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private CateringAppManager app;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.app = CateringAppManager.getInstance();

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("assign_tasks_main.fxml"));
        Parent main = mainLoader.load();
        Scene mainScene = new Scene(main);

        MainController mainController = mainLoader.getController();

        primaryStage.setScene(mainScene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        // primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void openMenuEditing() {

    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}