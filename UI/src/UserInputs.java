import Game.BoardSquare;
import Game.GameBoard;
import Game.GameMove;
import Game.PlayerTurn;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by amitaihandler on 8/14/16.
 */
public  class UserInputs
{
    public enum Direction
    {
        Down,Right
    }

    public static String getPath()
    {
        String res;
        Scanner in = new Scanner(System.in);
        System.out.println("Please provide file path:");
        res = in.nextLine();

        return res;
    }

    public static PlayerTurn getMove(GameBoard i_gameBoard)
    {
        int row;
        int col;
        int moveSize;
        int maxMoveSizeAllowed;
        BoardSquare squareType;
        List<GameMove> moves = new ArrayList<GameMove>();
        Direction direction;
        Scanner in = new Scanner(System.in);
        String userInput;
        PlayerTurn res = new PlayerTurn();
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("Please provide a move by the following format:");
                System.out.println("'C' or 'R' for column or row move (respectively)");
                System.out.println("Row number");
                System.out.println("Column number");
                System.out.println("Move size");
                System.out.println("New square type: 'Black' 'White' or 'Empty' (match case)");
                System.out.println("Please enter all values as one line separated by ','");

                userInput = in.nextLine();
                String[] tokens = userInput.split(",");

                // check for move direction
                if (tokens[0].equals("C"))
                    direction = Direction.Down;
                else if (tokens[0].equals("R"))
                    direction = Direction.Right;
                else
                    throw new Exception("Selection invalid for row or column move - choose 'R' or 'C'.");

                // check for row input
                row = Integer.parseInt(tokens[1]);
                if (row < 1 || row > i_gameBoard.getRows())
                    throw new Exception("Invalid row number (out of range for the board)");
                row--;  // adjust to the matrix notation (that starts from zero...)

                // check for column input
                col = Integer.parseInt(tokens[2]);
                if (col < 1 || col > i_gameBoard.getColumns())
                    throw new Exception("Invalid column number (out of range for the board)");
                col--;  // adjust to the matrix notation (that starts from zero...)

                // validate the 'legality' of move
                if (direction == Direction.Down)
                    maxMoveSizeAllowed = i_gameBoard.getRows() - row;
                else
                    maxMoveSizeAllowed = i_gameBoard.getColumns() - col;

                // check for column input
                moveSize = Integer.parseInt(tokens[3]);
                if (moveSize > maxMoveSizeAllowed)
                    throw new Exception("Size of move is invalid (will go out of board range)");

                // check for new squares state
                if (tokens[4].equals("Black"))
                    squareType = BoardSquare.valueOf("Black");
                else if (tokens[4].equals("White"))
                    squareType = BoardSquare.valueOf("White");
                else if (tokens[4].equals("Empty"))
                    squareType = BoardSquare.valueOf("Empty");
                else
                    throw new Exception("Value selected for sqaures state invalid. please select 'Black'/'White'/'Empty'");

                // add the square changes to the move list
                if (direction == Direction.Down) {
                    for (int i = 0; i < moveSize; i++) {
                        GameMove squareChange = new GameMove(row + i, col, squareType);
                        moves.add(squareChange);
                    }
                } else {
                    for (int i = 0; i < moveSize; i++) {
                        GameMove squareChange = new GameMove(row, col + i, squareType);
                        moves.add(squareChange);
                    }
                }

                // instantiate the player turn
                res = new PlayerTurn(moves, direction.name());
                validInput = true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Invalid Input please try again.");
                System.out.println();
            }
        }

        return res;
    }
}
