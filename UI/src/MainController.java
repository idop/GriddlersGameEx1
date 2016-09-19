import Game.BoardSquare;
import Game.Game;
import Game.GameMove;
import Game.Player.Player;
import Game.PlayerTurn;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.GameDefinitionsXmlParserException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Iterator;

public class MainController {

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
    public StackPane boardView;

    @FXML
    private AnchorPane playersDisplay;

    @FXML
    private TableView<Player> playersTableViewList;

    @FXML
    private RadioButton statusBlackRBtn;

    @FXML
    private RadioButton statusEmptyRBtn;

    @FXML
    private RadioButton statusUndecidedRBtn;


    private BoardController boardController;
    private Stage primaryStage;
    private GameBoardXmlParser gameXmlParser;
    private FileChooser fileChooser = new FileChooser();
    private Game game;
    private ObservableList<BoardController.Square> selectedSquares;
    private BoardSquare selectedStatusForMove = BoardSquare.Black;
    final ToggleGroup statusButtons = new ToggleGroup();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        startGameBtn.setDisable(true);
        playerClearSelectionBtn.setDisable(true);
        playerUndoLastBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(true);
        statusBlackRBtn.setToggleGroup(statusButtons);
        statusBlackRBtn.setSelected(true);
        statusBlackRBtn.requestFocus();
        statusEmptyRBtn.setToggleGroup(statusButtons);
        statusUndecidedRBtn.setToggleGroup(statusButtons);
        statusButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (statusBlackRBtn.isSelected())
                    selectedStatusForMove = BoardSquare.Black;
                else if (statusEmptyRBtn.isSelected())
                    selectedStatusForMove = BoardSquare.White;
                else
                    selectedStatusForMove = BoardSquare.Empty;
            }
        });
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
            this.game = new Game(gameXmlParser);
            initAndShowBoard();
            initPlayerTable();
        } catch (GameDefinitionsXmlParserException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Provided game xml file is not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }

    private void initAndShowBoard() {
        boardController = new BoardController(game);
        boardView.setStyle("-fx-background-color: dimgray");
        Node board = boardController.getBoardUI(game.getGameBoard());
        boardView.getChildren().add(board);
    }

    @FXML
    void clearSelection(ActionEvent event) {
        boardController.unSelectAllSquares();
    }

    @FXML
    void startGame(ActionEvent event) {
        boardController.setEnabled(true);
        startGameBtn.setDisable(true);
        loadPuzzleBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(false);
        currentPlayerName.setText(game.getCurrentPlayer().getName());
        updatePlayerScore();
        selectedSquares = boardController.getSelectedSquares();
        selectedSquares.addListener(new ListChangeListener<BoardController.Square>() {
            @Override
            public void onChanged(Change<? extends BoardController.Square> c) {
                System.out.println("selectedList change!");
            }
        });
    }

    private void initPlayerTable() {
        final ObservableList<Player> tableData = FXCollections.observableArrayList(game.getPlayers());
        final int ID = 0;
        final int Type = 1;
        final int NAME = 2;
        playersTableViewList.getColumns().clear();
        playersTableViewList.setEditable(false);
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("idProperty"));
        playersTableViewList.getColumns().clear();
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("typeProperty"));
        playersTableViewList.getColumns().clear();
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("nameProperty"));

        playersTableViewList.setItems(tableData);
        playersTableViewList.getColumns().addAll(idCol, typeCol, nameCol);

    }

    public void updatePlayerScore() {
        currentPlayerScore.setText(game.getCurrentPlayer().getScoreString());
    }

    @FXML
    private void makeMove(ActionEvent event) {
        PlayerTurn turn = new PlayerTurn();
        selectedSquares = boardController.getSelectedSquares();
        for (Iterator<BoardController.Square> iterator = selectedSquares.iterator(); iterator.hasNext(); ) {
            BoardController.Square square = iterator.next();
            int row = square.getRow();
            int col = square.getCol();
            GameMove move = new GameMove(row, col, selectedStatusForMove);
            turn.addGameMove(move);
            iterator.remove();
        }
        game.doPlayerTurn(turn);
        boardController.redrawBoardUI(game);
        updatePlayerScore(); //TODO: check why player score is not updated
    }

}
