import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.GameDefinitionsXmlParserException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private VBox root;

    @FXML
    private TextField puzzleFilePath;

    @FXML
    private Button loadPuzzleBtn;

    @FXML
    private Button startGameBtn;

    @FXML
    private AnchorPane playPanel;

    @FXML
    private Label currentPlayerScore;

    @FXML
    private Label currentPlayerName;

    @FXML
    private Button playerClearSelectionBtn;

    @FXML
    private Button playerUndoLastBtn;

    @FXML
    private Button playerMakeMoveBtn;

    @FXML
    private TitledPane playerMoveHistory;

    @FXML
    private TitledPane playerStatistics;

    @FXML
    private AnchorPane boardView;

    @FXML
    private AnchorPane playersDisplay;

    @FXML
    private TableView<?> playersTableViewList;

    private Stage primaryStage;
    private GameBoardXmlParser gameXmlParser;
    private FileChooser fileChooser = new FileChooser();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        startGameBtn.setDisable(true);
        playerClearSelectionBtn.setDisable(true);
        playerUndoLastBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(true);
    }

    @FXML
    void loadXmlFile(ActionEvent event) {
        fileChooser.setTitle("Select Game Xml");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        fileChooser.setInitialDirectory(selectedFile.getParentFile());
        try {
            gameXmlParser = new GameBoardXmlParser(absolutePath);
            puzzleFilePath.textProperty().setValue(absolutePath);
            startGameBtn.setDisable(false);
        } catch (GameDefinitionsXmlParserException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Provided game xml file is not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void startGame(ActionEvent event) {

    }

}
