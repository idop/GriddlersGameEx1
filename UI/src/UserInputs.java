import Game.BoardSquare;
import Game.GameBoard;
import Game.GameMove;

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

    public static List<GameMove> getMove(GameBoard i_gameBoard)
    {
        int row;
        int col;
        int moveSize;
        int maxMoveSizeAllowed;
        boolean validMove = true;
        BoardSquare squareType;
        List<GameMove> moves = null;
        Direction direction;
        Scanner in = new Scanner(System.in);

        // get move direction
        System.out.println("Please choose the direction of the move 'Down' or 'Right'");
        while (!in.hasNext() || !(in.hasNext("^Down$") || in.hasNext("^Right$")))
        {
            System.out.println("Please match to one of the types exactly: 'Down', 'Right'");
            in.nextLine();
        }
        direction = Direction.valueOf(in.nextLine());

        // get location of move origin
        System.out.println("Please provide row:");
        while (!in.hasNextInt())
        {
            System.out.println("Please provide a number");
            in.nextInt();
        }
        row = in.nextInt();

        System.out.println("Please provide column:");
        while (!in.hasNextInt())
        {
            System.out.println("Please provide a number");
            in.nextInt();
        }
        col = in.nextInt();

        /*
        // get the size of the desired move
        if(direction == Direction.Down)
            maxMoveSizeAllowed = i_gameBoard.getRows() - row;
        else
            maxMoveSizeAllowed = i_gameBoard.getColumns() - col;
        */

        System.out.println("Please state how many slots to change:");
        //while (!in.hasNextInt() || !(in.nextInt() > maxMoveSizeAllowed))
        while (!in.hasNextInt())
        {
            System.out.println("Please provide a number that will not exceed the board boundaries");
            in.nextLine();
        }
        moveSize = in.nextInt();


        // select the operation wanted
        System.out.println("Please choose the square type: 'Black', 'White', 'Empty'");
        while (!in.hasNext() || !(in.hasNext("Black$") || in.hasNext("White$") || in.hasNext("Empty$")))
        {
            System.out.println("Please match to one of the types exactly: 'Black', 'White', 'Empty'");
            in.nextLine();
        }

        if (in.hasNext("Black$"))
            squareType = BoardSquare.valueOf("Black");
        else if (in.hasNext("White$"))
            squareType = BoardSquare.valueOf("White");
        else
            squareType = BoardSquare.valueOf("Empty");

        // build the actual move list
        for (int i = 0; i < moveSize; i++)
        {
            GameMove squareChange = new GameMove(row, col, squareType);
            moves.add(squareChange);
        }

        return moves;
    }
}
