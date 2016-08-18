/* Created by Amitai Handler on 8/14/16. */

import Game.*;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;

import java.util.Iterator;
import java.util.Scanner;

public class MainMenu {
    GameBoardXmlParser parser = null;
    Game game = null;
    GameBoard gameBoard;
    boolean gameStarted = false;

    private void draw()
    {
        Scanner in = new Scanner(System.in);

        int selection;

        do
        {
            System.out.println();
            System.out.println("[1] Provide configuration file path");
            System.out.println("[2] Start Game");
            System.out.println("[3] Show board state");
            System.out.println("[4] Make move");
            System.out.println("[5] Show moves history");
            System.out.println("[6] Undo last move");
            System.out.println("[7] Get game statistics");
            System.out.println("[8] Quit game");
            selection = in.nextInt();

            switch(selection)
            {
                case 1:
                    parser = this.getParser();
                    break;

                case 2:
                    if (parser != null && gameStarted == false)
                    {
                        game = new Game(parser);
                        gameStarted = true;
                        System.out.println("Game initiated!");
                    }
                    else System.out.println("Please provide configuration first (option [1] in menu).");
                    break;

                case 3:
                    if (game != null)
                    {
                        gameBoard = game.gameBoard;
                        printGameBoard(gameBoard);
                    }
                    else System.out.println("Please start a game first.");
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 7:
                    break;

                default:
                    break;
            }

        }while (selection != 8);
    }

    public GameBoardXmlParser getParser()
    {
        try
        {
            String path = UserInputs.getPath();
            System.out.println(path);
            GameBoardXmlParser parser = new GameBoardXmlParser(path);
            System.out.println("it works. hurray!");
            return parser;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private void printGameBoard(GameBoard gameBoard)
    {
        BoardSquare[][] boardSquares = gameBoard.getBoard();

        // print column indices
        String currentRow = "   |";
        for (int i = 1; i <= gameBoard.getColumns(); i++)
        {
            if (i < 10)
                currentRow += "00"  + Integer.toString(i) + "|";
            else
                currentRow += "0"   + Integer.toString(i) + "|";
        }
        System.out.println(currentRow);

        // print board lines
        for (int i = 1; i <= gameBoard.getRows(); i++)
        {
            // print row separator
            currentRow = "";
            for (int k = 1; k <= gameBoard.getColumns() + 1; k++)
                currentRow += "----";
            System.out.println(currentRow);

            // add row index
            currentRow = "";
            if (i < 10)
                currentRow += "00"  + Integer.toString(i) + "|";
            else
                currentRow += "0"   + Integer.toString(i) + "|";

            // print the actual board state
            for (int j = 0; j < gameBoard.getColumns(); j++)
                currentRow += boardSquares[i-1][j].toString() + "|";

            // add row constraints
            Constraints constraints = game.rowConstraints.get(i);
            for (int n = 0; n < constraints.size(); n++)
            {
                Constraint currentConstraint  = constraints.getConstraint(n);
                if(currentConstraint.isPerfect())
                {
                    currentRow += "*";
                }
                currentRow += currentConstraint.toString();
            }

            System.out.println(currentRow);
        }

        // print column constraint
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to the Griddler!");
        System.out.println("------------------------");
        MainMenu mainMenu = new MainMenu();
        mainMenu.draw();
    }

}
