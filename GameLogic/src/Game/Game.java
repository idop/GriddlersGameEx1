package Game;

import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;

import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class Game {

    private final GameType gameType;
    private GameBoard gameBoard;
    private List<Constraints> rowConstraints;
    private List<Constraints> columnConstraints;
    private SolutionBoard solutionBoard;

    public GameType getGameType() {
        return gameType;
    }

    public BoardSquare[][] getGameBoardSquares() {
        return gameBoard.getBoard();
    }

    public Constraints getRowConstraint(int i) {
        return rowConstraints.get(i);
    }

    public Constraints getColumnConstraint(int i) {
        return columnConstraints.get(i);
    }

    public Game(GameBoardXmlParser gameBoardXmlParser) {
        gameType = gameBoardXmlParser.getGameType();
        gameBoard = new GameBoard(gameBoardXmlParser.getRows(), gameBoardXmlParser.getColumns());
        rowConstraints = gameBoardXmlParser.getRowConstraints();
        columnConstraints = gameBoardXmlParser.getColumnConstraints();
        solutionBoard = gameBoardXmlParser.getSolutionBoard();
    }
}
