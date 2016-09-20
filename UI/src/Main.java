/* Created by Amitai Handler on 8/14/16. */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        String sceneFile = "GriddlersGame.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(sceneFile);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        MainController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        System.out.println("  fxmlResource = " + sceneFile);
        primaryStage.setTitle("Griddlers Game!");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
