package Game.Player;

import Game.*;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by ido on 11/09/2016.
 */
public class ComputerPlayer extends Player {

    Random random = new Random();
    int maxNumberOfMovesToGet;

    public ComputerPlayer(String name, PlayerType playerType, int id, GameBoard gameBoard) {
        super(name, playerType, id, gameBoard);
        maxNumberOfMovesToGet = (getGameBoard().getColumns() + getGameBoard().getRows()) / 2;
    }

    @Override
    public void doTurn(PlayerTurn turn, SolutionBoard solution) {
        while (turn.getMoves().size() == 0) {
            turn = getAiTurn(turn);
        }
        super.doTurn(turn, solution);
    }

    private PlayerTurn getAiTurn(PlayerTurn turn) {
        BoardSquare squareType = (random.nextInt(2) == 0) ? BoardSquare.Black : BoardSquare.White;
        int numberOfMovesToGet = random.nextInt(maxNumberOfMovesToGet);
        HashSet<Point2D> drawnPoints = new HashSet<>();
        int row;
        int column;
        for (int i = 0; i < numberOfMovesToGet; i++) {
            row = random.nextInt(getGameBoard().getRows());
            column = random.nextInt(getGameBoard().getColumns());
            Point2D thePoint = new Point2D(row, column);
            if (!drawnPoints.contains(thePoint)) {
                drawnPoints.add(thePoint);
                turn.addGameMove(new GameMove(row, column, squareType));
            }
        }
        return turn;
    }

}
