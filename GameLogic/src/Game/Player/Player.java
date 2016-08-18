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
    private String name;
    private PlayerType playerType;
    private List<PlayerTurn> undoList;
    private List<PlayerTurn> redoList;
    private GameBoard gameBoard;
    private boolean playerWon = false;

    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
        undoList = new ArrayList<>();
        redoList = new ArrayList<>();
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

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }


    private void revertStep(SolutionBoard solution, List<PlayerTurn> listToRevert, String exceptionMassage) throws PlayerTurnException {
        if (listToRevert.size() != 0) {
            PlayerTurn turn = listToRevert.get(listToRevert.size() - 1);
            listToRevert.remove(listToRevert.size() - 1);
            turn.undo();
            doActualTrun(turn, solution);
        } else {
            throw new PlayerTurnException(exceptionMassage);
        }
    }

    public void undoTurn(SolutionBoard solution) throws PlayerTurnException {
        revertStep(solution, undoList, undoExceptionMessage);
    }

    public void redoTurn(SolutionBoard solution) throws PlayerTurnException {
        revertStep(solution, redoList, redoExceptionMessage);
    }

    public BoardSquare[][] getBoard() {
        return gameBoard.getBoard();
    }

    public void doTurn(PlayerTurn turn, SolutionBoard solution) {
        undoList.add(doActualTrun(turn, solution));
        redoList.clear();
    }

    public boolean checkIfPlayerWon() {
        return playerWon;
    }

    private PlayerTurn doActualTrun(PlayerTurn turn, SolutionBoard solution) {
        throw new RuntimeException(); // TODO
    }
}
