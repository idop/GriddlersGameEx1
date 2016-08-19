package Game;

/**
 * Created by ido on 14/08/2016.
 */
public class SolutionBoard {
    private BoardSquare[][] solutionBoard;

    public SolutionBoard(int rows, int columns) {
        solutionBoard = new BoardSquare[rows][columns];
        markSolutionBoardAsWhite();
    }

    private void markSolutionBoardAsWhite() {
        for (int i = 0; i < solutionBoard.length; i++) {
            for (int j = 0; j < solutionBoard[i].length; j++) {
                solutionBoard[i][j] = BoardSquare.White;
            }
        }
    }

    public void setBoardSquareAsBlack(int i, int j) {
        solutionBoard[i][j] = BoardSquare.Black;
    }

    public BoardSquare getBoardSquare(int i, int j) {
        return solutionBoard[i][j];
    }
}
