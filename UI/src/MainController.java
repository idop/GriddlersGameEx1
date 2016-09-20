import Game.BoardSquare;
import Game.Game;
import Game.GameMove;
import Game.Player.Player;
import Game.Player.PlayerType;
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
import java.util.List;

public class MainController {

    private static final int MAX_NUMBER_OF_TURNS_PER_ROUND = 2;
    @FXML
    private VBox root;

    @FXML
    private Label puzzleFilePath;

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

    @FXML
    private Button endRoundBtn;

    @FXML
    private Label currentMoveLabel;

    @FXML
    private Label ttlMovesLabel;

    @FXML
    private Label currentTurnLabel;

    @FXML
    private ListView<PlayerTurn> moveList;

    @FXML
    private TextArea moveDescription;

    private BoardController boardController;
    private Stage primaryStage;
    private GameBoardXmlParser gameXmlParser;
    private FileChooser fileChooser = new FileChooser();
    private Game game;
    private ObservableList<BoardController.Square> selectedSquares;
    private BoardSquare selectedStatusForMove = BoardSquare.Black;
    private final ToggleGroup statusButtons = new ToggleGroup();
    private boolean isGameFinished = false;
    private int currentMoveNumber = 1;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        startGameBtn.setDisable(true);
        disablePlayerControls();
        statusBlackRBtn.setToggleGroup(statusButtons);
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

    private void disablePlayerControls() {
        playerClearSelectionBtn.setDisable(true);
        playerUndoLastBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(true);
        statusBlackRBtn.setSelected(true);
        statusEmptyRBtn.setDisable(true);
        statusUndecidedRBtn.setDisable(true);
        playerClearSelectionBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(true);
        playerUndoLastBtn.setDisable(true);
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
            puzzleFilePath.textProperty().setValue("Puzzle File: " + absolutePath);
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
        boardView.setDisable(true);
    }

    @FXML
    void clearSelection(ActionEvent event) {
        boardController.unSelectAllSquares();
    }

    @FXML
    void startGame(ActionEvent event) {
        playersTableViewList.setDisable(false);
        boardView.setDisable(false);
        startGameBtn.setDisable(true);
        loadPuzzleBtn.setDisable(true);
        playerMakeMoveBtn.setDisable(false);
        statusBlackRBtn.setDisable(false);
        statusEmptyRBtn.setDisable(false);
        statusUndecidedRBtn.setDisable(false);
        playerClearSelectionBtn.setDisable(false);
        playerMakeMoveBtn.setDisable(false);
        playerUndoLastBtn.setDisable(false);
        updatePlayer();
        selectedSquares = boardController.getSelectedSquares();
        selectedSquares.addListener(new ListChangeListener<BoardController.Square>() {
            @Override
            public void onChanged(Change<? extends BoardController.Square> c) {
                System.out.println("selectedList change!");
            }
        });
        if (game.getCurrentPlayer().getPlayerType().equals(PlayerType.Computer)) {
            makeComputerTurn();
        }
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
        idCol.setSortable(false);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("idProperty"));
        playersTableViewList.getColumns().clear();
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setMinWidth(100);
        typeCol.setSortable(false);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("typeProperty"));
        playersTableViewList.getColumns().clear();
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setSortable(false);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("nameProperty"));
        TableColumn scoreCol = new TableColumn("Score");
        scoreCol.setMinWidth(100);
        scoreCol.setSortable(false);
        scoreCol.setCellValueFactory(
                new PropertyValueFactory<Player, String>("scoreProperty"));

        playersTableViewList.setItems(tableData);
        playersTableViewList.getColumns().addAll(idCol, typeCol, nameCol, scoreCol);

    }

    public void updatePlayer() {
        currentPlayerScore.setText(game.getCurrentPlayer().getScoreProperty().toString());
        currentPlayerName.setText(game.getCurrentPlayer().getName());
    }

    @FXML
    private void makeMove(ActionEvent event) {
        makeHumenPlayerMove();
    }

    private void makeMultiPlayerMove() {
        makeHumenPlayerMove();
        //Todo update Move Label
    }

    private void makeComputerTurn() {
        makeComputerMove();
        if (game.checkIfPlayerWon()) {
            endCurrentGame();
        }
        makeComputerMove();
        if (game.checkIfPlayerWon()) {
            endCurrentGame();
        } else {
            endRound();
        }


    }

    @FXML
    private void endRound() {
        game.endRound();
        if (game.isGameEnded()) {
            endCurrentGame();
        } else {
            boardController.redrawBoardUI(game.getGameBoard());
            updatePlayer(); //TODO: check why player score is not updated
            currentMoveNumber = 1;
        }

    }

    private void makeComputerMove() {
        doTurn(new PlayerTurn());

    }

    private void makeHumenPlayerMove() {
        PlayerTurn turn = getPlayerTurn();
        doTurn(turn);
        if (currentMoveNumber == MAX_NUMBER_OF_TURNS_PER_ROUND) {
            playerMakeMoveBtn.setDisable(true);
        } else {
            currentMoveNumber++;
        }

    }

    private void doTurn(PlayerTurn turn) {
        game.doPlayerTurn(turn);
        boardController.redrawBoardUI(game.getGameBoard());
        updatePlayerMoveList();
        updatePlayer(); //TODO: check why player score is not updated
    }

    private void updatePlayerMoveList(){
        ObservableList<PlayerTurn> turnList = game.getCurrentPlayer().getUndoList();
        moveList.setItems(turnList);
        for (PlayerTurn player: turnList) {

        }
    }

    @FXML
    private void undoTurn(ActionEvent event){
        //TODO: implement undoTurn()..
    }


    private PlayerTurn getPlayerTurn() {
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
        return turn;
    }

    private void endCurrentGame() {
        disablePlayerControls();
        boardView.setDisable(true);
        loadPuzzleBtn.setDisable(false);
    }

}
