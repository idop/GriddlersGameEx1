package Game;

import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;

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

    public boolean validRowsConstraints(Constraints[] rowConstraints) {
        boolean res = true;
        for (int i = 0; i < rowConstraints.length && res; ++i) {
            Constraints derivedRowConstraintsFromSolution = deriveRowConstraintsFromSolutionBoard(i);
            if (derivedRowConstraintsFromSolution.size() == 0) {
                derivedRowConstraintsFromSolution.addConstraint(new Constraint(0));
            }
            res = rowConstraints[i].equals(derivedRowConstraintsFromSolution);
        }
        return res;
    }


    public boolean validColumnsConstraints(Constraints[] columnConstraints) {
        boolean res = true;
        for (int i = 0; i < columnConstraints.length; ++i) {
            Constraints derivedColumnConstraintsFromSolution = deriveColumnConstraintsFromSolutionBoard(i);
            res = columnConstraints[i].equals(derivedColumnConstraintsFromSolution);
        }
        return res;
    }


    private Constraints deriveRowConstraintsFromSolutionBoard(int row) {
        Constraints res = new Constraints();
        int numberOfBlackSquaresInARow = solutionBoard[row][0] == BoardSquare.Black ? 1 : 0;
        BoardSquare previousBoardSquare = solutionBoard[row][0];
        for (int i = 1; i < solutionBoard[row].length; ++i) {
            if (solutionBoard[row][i] == BoardSquare.Black) {
                numberOfBlackSquaresInARow++;
                if (i != solutionBoard[row].length - 1) {
                    previousBoardSquare = BoardSquare.Black;
                } else {
                    res.addConstraint(new Constraint(numberOfBlackSquaresInARow));
                }
            } else if (solutionBoard[row][i] == BoardSquare.White) {
                if (previousBoardSquare == BoardSquare.Black) {
                    res.addConstraint(new Constraint(numberOfBlackSquaresInARow));
                    numberOfBlackSquaresInARow = 0;
                }
                previousBoardSquare = BoardSquare.White;
            }

        }
        return res;

    }

    private Constraints deriveColumnConstraintsFromSolutionBoard(int column) {
        Constraints res = new Constraints();
        int numberOfBlackSquaresInARow = solutionBoard[0][column] == BoardSquare.Black ? 1 : 0;
        BoardSquare previousBoardSquare = solutionBoard[0][column];
        for (int i = 1; i < solutionBoard.length; ++i) {
            if (solutionBoard[i][column] == BoardSquare.Black) {
                numberOfBlackSquaresInARow++;
                if (i != solutionBoard.length - 1) {
                    previousBoardSquare = BoardSquare.Black;
                } else {
                    res.addConstraint(new Constraint(numberOfBlackSquaresInARow));
                }
            } else if (solutionBoard[i][column] == BoardSquare.White) {
                if (previousBoardSquare == BoardSquare.Black) {
                    res.addConstraint(new Constraint(numberOfBlackSquaresInARow));
                    numberOfBlackSquaresInARow = 0;
                }
                previousBoardSquare = BoardSquare.White;
            }

        }
        return res;
    }
}
