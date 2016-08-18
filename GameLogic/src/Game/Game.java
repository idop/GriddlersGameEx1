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
    public GameBoard gameBoard;
    public List<Constraints> rowConstraints;
    public List<Constraints> columnConstraints;
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



    public int getMaxColConstraints()
    {
        int max = 0;
        for (int i=0; i < gameBoard.getColumns(); i++)
        {
            int currentListSize = columnConstraints.get(i).size();
            if (currentListSize > max)
            {
                max = currentListSize;
            }
        }
        return max;
    }

    public int getMaxRowConstraints()
    {
        int max = 0;
        for (int i=0; i < gameBoard.getColumns(); i++)
        {
            int currentListSize = rowConstraints.get(i).size();
            if (currentListSize > max)
            {
                max = currentListSize;
            }
        }
        return max;
    }
}
