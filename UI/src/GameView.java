/**
 * Created by amitaihandler on 8/16/16.
 */
import Game.*;

public class GameView
{
    private Character[][] board;
    private int rows;
    private int columns;

    public GameView(Game gGame)
    {
        //*2 to leave space between constraints
        this.rows       = gGame.gameBoard.getRows() + gGame.getMaxRowConstraints()*2;
        this.columns    = gGame.gameBoard.getColumns() + gGame.getMaxColConstraints()*2;
    }

    public void printGameBoardView()
    {

    }
}
