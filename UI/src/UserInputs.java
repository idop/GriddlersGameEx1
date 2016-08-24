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
        String comment = "";
        BoardSquare squareType;
        List<GameMove> moves = new ArrayList<GameMove>();
        Direction direction;
        Scanner in = new Scanner(System.in);
        String userInput;
        PlayerTurn res = new PlayerTurn();
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("Please provide a move by the following format: (values separated by ',')");
                System.out.println("1. 'Down' or 'Right' for the Direction (case insensitive)");
                System.out.println("2. Starting row number");
                System.out.println("3. Starting column number");
                System.out.println("4. Number of squares to change ");
                System.out.println("5. New square type: 'Black' 'White' or 'Empty' (case insensitive)");
                System.out.println("6. [OPTIONAL] add textual comment for move done");
                System.out.println("EXAMPLE: \"right,1,1,3,black[,it makes perfect sense!!]\"");

                userInput = in.nextLine();
                String[] tokens = userInput.split(",");
                tokens = trimStringArray(tokens);
                // check for move direction
                if (tokens[0].toUpperCase().equals("DOWN"))
                    direction = Direction.Down;
                else if (tokens[0].toUpperCase().equals("RIGHT"))
                    direction = Direction.Right;
                else
                    throw new Exception("Selection invalid for row or column move - choose 'Down' or 'Right'.");

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
                if (tokens[4].toUpperCase().equals("BLACK"))
                    squareType = BoardSquare.valueOf("Black");
                else if (tokens[4].toUpperCase().equals("WHITE"))
                    squareType = BoardSquare.valueOf("White");
                else if (tokens[4].toUpperCase().equals("EMPTY"))
                    squareType = BoardSquare.valueOf("Empty");
                else
                    throw new Exception("Value selected for sqaures state invalid. please select 'Black'/'White'/'Empty'");

                if (tokens.length > 5)
                {
                    comment +=tokens[5];
                    for (int i = 6; i < tokens.length; i++)
                    {
                        comment += ", "+ tokens[i];
                    }
                }

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
                res.setComment(comment);
                validInput = true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Invalid Input please try again.");
                System.out.println();
            }
        }

        return res;
    }

    private static String[] trimStringArray(String[] tokens) {
        for (int i = 0;  i < tokens.length; ++i){
            tokens[i] = tokens[i].trim();
        }
        return tokens;
    }
}
