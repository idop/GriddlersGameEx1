import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Game.Game;
import Game.GameBoard;

/**
 * Created by amitaihandler on 9/12/16.
 */

public class BoardController {
    /*
    @FXML
    private AnchorPane centerPane;

    @FXML
    private AnchorPane overviewPane;

    @FXML
    private AnchorPane topNumberPane;

    @FXML
    private AnchorPane leftNumberPane;

    @FXML
    private AnchorPane rootPane;
    */
    private Stage primaryStage;
    private Node root;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public BoardController(Node e)
    {
        this.root = e;
    }

    public Node getBoard(Game game, GameBoard gameBoard) {

        int rows = gameBoard.getRows();
        int columns = gameBoard.getColumns();

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: White");

        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(20);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }


        grid.setPrefSize(400,400);
        grid.setGridLinesVisible(true);

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: teal");
        stackPane.setMaxWidth(600);
        stackPane.setMaxHeight(400);
        stackPane.setPrefSize(600,400);
        stackPane.getChildren().add(grid);
        stackPane.setAlignment(grid, Pos.CENTER);

        return grid;
    }
}
