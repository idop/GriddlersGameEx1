package Game;

import Game.Player.Player;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;

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
    private long startTime;
    private int numberOfRounds;

    public Game(GameBoardXmlParser gameBoardXmlParser) {
        gameType = gameBoardXmlParser.getGameType();
        rowConstraints = gameBoardXmlParser.getRowConstraints();
        columnConstraints = gameBoardXmlParser.getColumnConstraints();
        solutionBoard = gameBoardXmlParser.getSolutionBoard();
        maxColumnConstraints = getMaxConstraints(gameBoardXmlParser.getColumns(), this.columnConstraints);
        maxRowConstraints = getMaxConstraints(gameBoardXmlParser.getRows(), this.rowConstraints);
        startTime = System.currentTimeMillis();
        players = gameBoardXmlParser.getPlayers();
        numberOfPlayers = players.size();
        switch (gameType) {
            case SinglePlayer:
                break;
            case MultiPlayer:
                numberOfRounds = gameBoardXmlParser.getMoves();
                break;
            case DynamicMultiPlayer:
                break;
        }
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


    public void printPlayerMoveHistory() {
        players.get(currentPlayerId).printMoveHistory();
    }

    public String getPlayerStatistics() {
        String res = players.get(currentPlayerId).getPlayerStatisticsString();
        String timeSinceStart = Long.toString((System.currentTimeMillis() - startTime) / 1000);
        res = res + " | Time since game begun (seconds): " + timeSinceStart;
        return res;
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
