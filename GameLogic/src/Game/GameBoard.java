package Game;

/**
 * Created by ido on 13/08/2016.
 */
public class GameBoard {


    private BoardSquare[][] board;
    private int rows;
    private int columns;

    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        board = new BoardSquare[rows][columns];
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                board[i][j] = BoardSquare.Empty;
            }
        }
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return columns;
    }

    public BoardSquare[][] getBoard() {
        return board.clone();
    }
}
