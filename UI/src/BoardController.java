import javafx.fxml.FXML;
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
    private Parent root;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public BoardController(Parent e)
    {
        this.root = e;
    }

    public void drawBoard(Game game, GameBoard gameBoard) {
        int rows = gameBoard.getRows();
        int columns = gameBoard.getColumns();

        GridPane grid = new GridPane();
        grid.getStyleClass();

        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(40);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(40);
            grid.getRowConstraints().add(row);
        }
        root.getChildrenUnmodifiable().add(grid);
        //stage.show();
    }
}
