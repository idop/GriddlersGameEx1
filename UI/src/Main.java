/* Created by Amitai Handler on 8/14/16. */


import Game.BoardSquare;
import Game.Game;
import Game.PlayerTurn;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        String sceneFile = "GriddlersGame.fxml";
        //Parent root = null;
        URL url  = null;
        try
        {
            url  = getClass().getResource( sceneFile );
            Parent root = FXMLLoader.load( url );
            System.out.println( "  fxmlResource = " + sceneFile );
            primaryStage.setTitle("Griddlers Game!");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch ( Exception ex )
        {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "  * url: " + url );
            System.out.println( "  * " + ex );
            System.out.println( "    ----------------------------------------\n" );
            throw ex;
        }
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
