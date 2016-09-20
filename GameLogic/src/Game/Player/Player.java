package Game.Player;


import Game.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ido on 18/08/2016.
 */
public class Player {
    private static final String undoExceptionMessage = "Undo List is Empty";
    private static final String redoExceptionMessage = "Redo List is Empty";
    private final int numberOfBoardSquares;
    private int id;
    private String name;
    private PlayerType playerType;
    private List<PlayerTurn> undoList;
    private GameBoard gameBoard;
    private boolean playerWon = false;
    private PlayerGameStatistics statistics;
    private int turnNumber = 1;
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty idProperty;
    private final SimpleStringProperty typeProperty;
    private final SimpleStringProperty scoreProperty;


    public Player(String name, PlayerType playerType, int id, GameBoard gameBoard) {
        this.name = name;
        this.playerType = playerType;
        this.id = id;
        undoList = new ArrayList<>();
        statistics = new PlayerGameStatistics();
        this.gameBoard = gameBoard;
        numberOfBoardSquares = gameBoard.getNumberOfSquares();
        nameProperty = new SimpleStringProperty(name);
        idProperty = new SimpleStringProperty(String.valueOf(id));
        typeProperty = new SimpleStringProperty(playerType.toString());
        scoreProperty = new SimpleStringProperty(String.format(" %,.2f%%", statistics.getScore()));
    }

    public String getScoreProperty() {
        return scoreProperty.get();
    }

    public SimpleStringProperty scorePropertyProperty() {
        return scoreProperty;
    }

    public void setScoreProperty(String scoreProperty) {
        this.scoreProperty.set(scoreProperty);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public String getPlayerStatisticsString()
    {
        return statistics.toString();
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public ObservableList<PlayerTurn> getUndoList() {
        return FXCollections.observableList(this.undoList.stream().collect(Collectors.toList()));
    }


    private PlayerTurn revertStep(SolutionBoard solution, List<PlayerTurn> listToRevert, String exceptionMassage) throws PlayerTurnException {

        PlayerTurn revertedTurn;
        if (listToRevert.size() != 0) {
            PlayerTurn turn = listToRevert.get(listToRevert.size() - 1);
            listToRevert.remove(listToRevert.size() - 1);
            turn.undo();
            revertedTurn = doActualTrun(turn, solution);
        } else {
            throw new PlayerTurnException(exceptionMassage);
        }
        return revertedTurn;
    }

    public void undoTurn(SolutionBoard solution) throws PlayerTurnException {
        revertStep(solution, undoList, undoExceptionMessage);
        turnNumber--;
    }

    public BoardSquare[][] getBoard() {
        return gameBoard.getBoard();
    }

    public void doTurn(PlayerTurn turn, SolutionBoard solution) {
        turn.setTurnNumber(turnNumber);
        turnNumber++;
        undoList.add(doActualTrun(turn, solution));
    }

    public boolean checkIfPlayerWon() {
        return playerWon;
    }

    private PlayerTurn doActualTrun(PlayerTurn turn, SolutionBoard solution) {
        int row;
        int column;
        for (GameMove gameMove : turn.getMoves()) {
            row = gameMove.getRow();
            column = gameMove.getColumn();
            gameMove.setPreviousBoardSquare(gameBoard.getBoardSquare(row, column));
            gameBoard.setBoardSquare(row, column, gameMove.getNewBoardSquare());
        }
        checkSolution(solution);
        return turn;
    }

    private void checkSolution(SolutionBoard solution) {
        int numberOfCorrcetSquares = 0;
        for (int i = 0; i < gameBoard.getRows(); ++i) {
            for (int j = 0; j < gameBoard.getColumns(); ++j) {
                if (gameBoard.getBoardSquare(i, j) == solution.getBoardSquare(i, j)) {
                    ++numberOfCorrcetSquares;
                }
            }
        }
        playerWon = (numberOfCorrcetSquares == numberOfBoardSquares);
        statistics.setScore((double) numberOfCorrcetSquares / (double) numberOfBoardSquares);
        scoreProperty.setValue(String.format(" %,.2f%%", statistics.getScore()));
    }

    public void printMoveHistory()
    {
        for (PlayerTurn turn: this.undoList)
        {
            turn.printTurn();
        }
    }

    public String getScoreString() {
        return statistics.getScoreAsString();
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public String getIdProperty() {
        return idProperty.get();
    }

    public String getTypeProperty() {
        return typeProperty.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return id == player.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getUndoListSize() {
        return undoList.size();
    }
}
