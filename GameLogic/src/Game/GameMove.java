package Game;

/**
 * Created by amitaihandler on 8/18/16.
 */
public class GameMove {
    private int row;
    private int column;
    private BoardSquare newBoardSquare;
    private BoardSquare previousBoardSquare;

    public GameMove() {
    }

    public GameMove(int row, int column, BoardSquare newBoardSquare) {
        this.row = row;
        this.column = column;
        this.newBoardSquare = newBoardSquare;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BoardSquare getNewBoardSquare() {
        return newBoardSquare;
    }

    public void setNewBoardSquare(BoardSquare newBoardSquare) {
        this.newBoardSquare = newBoardSquare;
    }

    public BoardSquare getPreviousBoardSquare() {
        return previousBoardSquare;
    }

    public void setPreviousBoardSquare(BoardSquare previousBoardSquare) {
        this.previousBoardSquare = previousBoardSquare;
    }

    public void undo() {
        BoardSquare tempBoardSquare = newBoardSquare;
        newBoardSquare = previousBoardSquare;
        previousBoardSquare = tempBoardSquare;
    }
}
