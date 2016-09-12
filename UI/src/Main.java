/* Created by Amitai Handler on 8/14/16. */


import Game.Game;
import GameXmlParser.GameBoardXmlParser;
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

    private static final String GAME_STARTED_MESSAGE = "Game Already Started";
    private GameBoardXmlParser parser = null;
    private Game game = null;
    boolean gameStarted = false;
    private boolean playerWon = false;

    public GameBoardXmlParser getParser() {
        try {
            String path = InputManager.getPath();
            System.out.println(path);
            GameBoardXmlParser parser = new GameBoardXmlParser(path);
            System.out.println("Xml File Loaded Successfully");
            return parser;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
