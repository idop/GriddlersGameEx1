package Game;

import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraints;
import GameXmlParser.Schema.GameType;

import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class Game {
    private GameBoard gameBoard;
    private GameType gameType;
    List<Constraints> rowConstraints;
    List<Constraints> columnConstraints;
    public Game(GameBoardXmlParser gameBoardXmlParser) {
        gameType = gameBoardXmlParser.getGameType();
        gameBoard = new GameBoard(gameBoardXmlParser.getRows(), gameBoardXmlParser.getColumns());

    }
}
