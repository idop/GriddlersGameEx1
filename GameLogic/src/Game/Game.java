package Game;

import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.GameType;

/**
 * Created by ido on 13/08/2016.
 */
public class Game {
    private GameBoard gameBoard;
    private GameType gameType;

    public Game(GameBoardXmlParser gameBoardXmlParser) {
        gameBoard = new GameBoard(gameBoardXmlParser.getRows(), gameBoardXmlParser.getColumns());
        gameType = gameBoardXmlParser.getGameType();
    }
}
