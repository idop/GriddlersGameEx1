package Game.Player;


import Game.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ido on 18/08/2016.
 */
public class Player {
    private static final String undoExceptionMessage = "Undo List is Empty";
    private static final String redoExceptionMessage = "Redo List is Empty";
    private final int numberOfBoardSquares;
    private String name;
    private PlayerType playerType;
    private List<PlayerTurn> undoList;
    private List<PlayerTurn> redoList;
    private GameBoard gameBoard;
    private boolean playerWon = false;
    private PlayerGameStatistics statistics;

    public Player(String name, PlayerType playerType, GameBoard gameBoard) {
        this.name = name;
        this.playerType = playerType;
        undoList = new ArrayList<>();
        redoList = new ArrayList<>();
        statistics = new PlayerGameStatistics();
        this.gameBoard = gameBoard;
        numberOfBoardSquares = gameBoard.getNumberOfSquares();
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
        redoList.add(revertStep(solution, undoList, undoExceptionMessage));
        statistics.incNumberOfUndoTurns();
    }

    public void redoTurn(SolutionBoard solution) throws PlayerTurnException {
        undoList.add(revertStep(solution, redoList, redoExceptionMessage));
        statistics.incNumberOfRedoMoves();
    }

    public BoardSquare[][] getBoard() {
        return gameBoard.getBoard();
    }

    public void doTurn(PlayerTurn turn, SolutionBoard solution) {
        undoList.add(doActualTrun(turn, solution));
        redoList.clear();
        statistics.incNumberOfTurns();
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
    }

    public void printMoveHistory()
    {
        for (PlayerTurn turn: this.undoList)
        {
            turn.printTurn();
        }
    }
}
