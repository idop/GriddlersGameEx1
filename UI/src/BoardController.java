import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Created by amitaihandler on 9/12/16.
 */

public class GridController {

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

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize(Stage stage) {
        int rows = 5;
        int columns = 5;

        stage.setTitle("Griddler puzzle");
        GridPane grid = new GridPane();
        grid.getStyleClass();

        for(int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(40);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(40);
            grid.getRowConstraints().add(row);
        }
        stage.show();
    }
}
