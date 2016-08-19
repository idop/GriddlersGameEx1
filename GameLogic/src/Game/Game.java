package Game;

import Game.Player.Player;
import Game.Player.PlayerType;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class Game {

    private final GameType gameType;
    private final int numberOfPlayers;
    private final SolutionBoard solutionBoard;
    private List<Player> players;
    private int currentPlayerId = 0;
    private List<Constraints> rowConstraints;
    private List<Constraints> columnConstraints;
    private int maxColumnConstraints;
    private int maxRowConstraints;
    private boolean playerWon = false;

    public Game(GameBoardXmlParser gameBoardXmlParser) {
        gameType = gameBoardXmlParser.getGameType();
        rowConstraints = gameBoardXmlParser.getRowConstraints();
        columnConstraints = gameBoardXmlParser.getColumnConstraints();
        solutionBoard = gameBoardXmlParser.getSolutionBoard();
        maxColumnConstraints = getMaxConstraints(gameBoardXmlParser.getColumns(), this.columnConstraints);
        maxRowConstraints = getMaxConstraints(gameBoardXmlParser.getRows(), this.rowConstraints);
        numberOfPlayers = 1; // TODO Gerelize This and Read This From Parser
        players = new ArrayList<>(numberOfPlayers); // TODO
        for (int i = 0; i < numberOfPlayers; i++) {// TODO
            players.add(new Player("default Player", PlayerType.Human, new GameBoard(gameBoardXmlParser.getRows(), gameBoardXmlParser.getColumns())));// TODO
        }// TODO
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameBoard getGameBoard() {
        return players.get(currentPlayerId).getGameBoard();
    }

    public Constraints getRowConstraint(int i) {
        return rowConstraints.get(i);
    }

    public Constraints getColumnConstraint(int i) {
        return columnConstraints.get(i);
    }


    public int getMaxColumnConstraints() {
        return maxColumnConstraints;
    }

    private int getMaxConstraints(int columns, List<Constraints> constraints) {
        int max = 0;
        for (int i = 0; i < columns; i++) {
            int currentListSize = constraints.get(i).size();
            if (currentListSize > max) {
                max = currentListSize;
            }
        }
        return max;
    }

    public int getMaxRowConstraints() {

        return maxRowConstraints;
    }

    public void doPlayerTurn(PlayerTurn turn) {
        Player currentPlayer = players.get(currentPlayerId);
        currentPlayer.doTurn(turn, solutionBoard);
        setPerfectConstraints();
        playerWon = currentPlayer.checkIfPlayerWon();
        if (!playerWon) {
            currentPlayerId = (currentPlayerId + 1) % numberOfPlayers;
        }
    }


    public void undoTurn() throws PlayerTurnException {
        players.get(currentPlayerId).undoTurn(solutionBoard);
        setPerfectConstraints();
        currentPlayerId = (currentPlayerId + 1) % numberOfPlayers;
    }

    public void redoTurn() throws PlayerTurnException {
        players.get(currentPlayerId).redoTurn(solutionBoard);
        setPerfectConstraints();
        currentPlayerId = (currentPlayerId + 1) % numberOfPlayers;
    }

    public boolean checkIfPlayerWon() {
        return playerWon;
    }

    private void setPerfectConstraints() {
        //TODO
    }
}
