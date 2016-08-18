/**
 * Created by amitaihandler on 8/16/16.
 */
import Game.*;

public class GameView
{
    private static final int undecided = 63;
    private static final int empty = 88;
    private static final int black = 254;
    private Character[][] board;
    private Game gGame;
    private int rows;
    private int columns;
    private int actualRows;
    private int actualColumns;


    public GameView(Game i_Game)
    {
        gGame           = i_Game;
        actualRows      = gGame.gameBoard.getRows();
        actualColumns   = gGame.gameBoard.getColumns();
        rows            = actualRows + gGame.getMaxRowConstraints() * 2;        //*2 to leave space between constraints
        columns         = actualColumns + gGame.getMaxColConstraints() * 2;     //*2 to leave space between constraints
        board           = new Character[rows][columns];
        resetBoard();
    }

    public void printGameBoardView()
    {

    }

    public void resetBoard()
    {
        // resets the "actual board" to '?'
        for (int i = rows - actualRows; i < rows; i++)
        {
            for (int j = columns - actualColumns; j < actualColumns; j++)
            {
                board[i][j] = (char)undecided;
            }
        }

        // fill in the row constraints
        for (int i = rows - actualRows; i < rows; i++)
        {
            for (int j = columns - actualColumns; j >= 0; j = j - 2)                // leave space between constraints
            {

            }
        }

        // fill in the column constraints

    }
}
