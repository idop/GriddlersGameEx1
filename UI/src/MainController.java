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

public class MainController {

    private static final int MAX_NUMBER_OF_TURNS_PER_ROUND = 2;
    private static final String STRING_EMPTY = "";
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
    private Label ttlMovesLabel;

    @FXML
    private Label currentTurnLabel;

    @FXML
    private ListView<PlayerTurn> moveList;

    @FXML
    private TextArea moveDescription;

    @FXML
    private TextArea moveComment;

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
        currentMoveNumber = 1;
        startGameBtn.setDisable(true);
        disablePlayerControls();
        playersTableViewList.setDisable(true);
        statusBlackRBtn.setToggleGroup(statusButtons);
        statusBlackRBtn.requestFocus();
        statusEmptyRBtn.setToggleGroup(statusButtons);
        statusUndecidedRBtn.setToggleGroup(statusButtons);
        statusButtons.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (statusBlackRBtn.isSelected())
                selectedStatusForMove = BoardSquare.Black;
            else if (statusEmptyRBtn.isSelected())
                selectedStatusForMove = BoardSquare.White;
            else
                selectedStatusForMove = BoardSquare.Empty;
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
        updatePlayer(game.getCurrentPlayer());
        initMoveList();
        initUnselectBtn();
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

        playersTableViewList.getSelectionModel().selectedItemProperty().addListener(this::onPlayerSelect);

    }

    public void onPlayerSelect(ObservableValue<? extends Player> obs, Player oldSelection, Player newSelection) {
        if (newSelection != null) {
            Player selectedPlayer = playersTableViewList.getSelectionModel().getSelectedItem();
            if (!game.isGameEnded()) {
                if (selectedPlayer.getPlayerType().equals(PlayerType.Computer)) {
                    boardController.redrawBoardUI(selectedPlayer.getGameBoard());
                    boardView.setDisable(true);
                    updatePlayer(selectedPlayer);
                } else {
                    if (game.getCurrentPlayer().equals(selectedPlayer)) {
                        boardController.redrawBoardUI(selectedPlayer.getGameBoard());
                        boardView.setDisable(false);
                        updatePlayer(selectedPlayer);
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Invalid selection");
                        alert.setContentText("Cannot peek to a human player board");
                        alert.showAndWait();
                    }
                }
            } else {
                boardController.redrawBoardUI(selectedPlayer.getGameBoard());
                boardView.setDisable(true);
                updatePlayer(selectedPlayer);
            }
        }
    }

    public void updatePlayer(Player player) {
        currentPlayerScore.setText(player.getScoreProperty().toString());
        currentPlayerName.setText(player.getName());
        ttlMovesLabel.setText(String.format("Round: %d/%d", game.getCurrentRound(), game.getMaxNumberOfRounds()));
        currentTurnLabel.setText(Integer.toString(currentMoveNumber));
        updatePlayerMoveList(player);

    }

    @FXML
    private void makeMove(ActionEvent event) {
        makeHumanPlayerMove();

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
            if (game.getCurrentPlayer().getPlayerType().equals(PlayerType.Human)) {
                boardController.redrawBoardUI(game.getGameBoard());
                currentMoveNumber = 1;
                updatePlayer(game.getCurrentPlayer());
            } else {
                makeComputerTurn();
            }
        }

    }

    private void makeComputerMove() {
        doTurn(new PlayerTurn());

    }

    private void makeHumanPlayerMove() {
        PlayerTurn turn = getPlayerTurn();
        doTurn(turn);
        playerUndoLastBtn.setDisable(false);
        if (currentMoveNumber > MAX_NUMBER_OF_TURNS_PER_ROUND) {
            playerMakeMoveBtn.setDisable(true);
        }


    }

    private void doTurn(PlayerTurn turn) {
        game.doPlayerTurn(turn);
        currentMoveNumber++;
        boardController.redrawBoardUI(game.getGameBoard());
        updatePlayer(game.getCurrentPlayer());
    }

    private void updatePlayerMoveList(Player player) {
        ObservableList<PlayerTurn> turnList = player.getUndoList();
        if (turnList != null) {
            moveList.setItems(turnList);
        }

        moveList.setCellFactory(lv -> new ListCell<PlayerTurn>() {
            @Override
            public void updateItem(PlayerTurn item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getListViewName();  // get text from item
                    setText(text);
                }
            }
        });
    }

    private void initMoveList() {
        moveList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlayerTurn>() {
            @Override
            public void changed(ObservableValue<? extends PlayerTurn> observable, PlayerTurn oldValue, PlayerTurn newValue) {
                if (newValue != null)
                    moveDescription.setText(newValue.getTurnString());
                else
                    moveDescription.setText("");
            }
        });
    }

    private void initUnselectBtn() {
        selectedSquares = boardController.getSelectedSquares();
        selectedSquares.addListener(new ListChangeListener<BoardController.Square>() {
            @Override
            public void onChanged(Change<? extends BoardController.Square> c) {
                if (selectedSquares.size() > 0)
                    playerClearSelectionBtn.setDisable(true);
                else
                    playerClearSelectionBtn.setDisable(false);
            }
        });
    }

    @FXML
    private void undoTurn(ActionEvent event) {
        try {
            game.undoTurn();
            playerMakeMoveBtn.setDisable(false);
            boardController.redrawBoardUI(game.getGameBoard());
            currentMoveNumber = (currentMoveNumber - 1) < 0 ? 0 : currentMoveNumber - 1;
            updatePlayer(game.getCurrentPlayer());
            if (game.getCurrentPlayer().getUndoList().size() == 0) {
                playerUndoLastBtn.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private PlayerTurn getPlayerTurn() {
        PlayerTurn turn = new PlayerTurn();
        turn.setComment(moveComment.getText());
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        alert.setHeaderText("Game Ended");
        alert.setContentText("Game Ended");
        alert.showAndWait();
    }

}
