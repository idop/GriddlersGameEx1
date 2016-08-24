/* Created by Amitai Handler on 8/14/16. */

import Game.BoardSquare;
import Game.Game;
import Game.PlayerTurn;
import GameXmlParser.GameBoardXmlParser;
import GameXmlParser.Schema.Constraint;
import GameXmlParser.Schema.Constraints;

import java.util.Scanner;

public class MainMenu {
    private static final String GAME_STARTED_MESSAGE = "Game Already Started";
    private GameBoardXmlParser parser = null;
    private Game game = null;
    boolean gameStarted = false;
    private boolean playerWon = false;

    private void draw() {
        Scanner in = new Scanner(System.in);

        int selection;

        do {
            System.out.println();
            System.out.println("[1] Provide configuration file path");
            System.out.println("[2] Start Game");
            System.out.println("[3] Show board state");
            System.out.println("[4] Make move");
            System.out.println("[5] Show moves history");
            System.out.println("[6] Undo last move");
            System.out.println("[7] Redo move");
            System.out.println("[8] Get game statistics");
            System.out.println("[9] Quit game");

            while (!in.hasNextInt()) {
                System.out.println("Please select a number from the menu list.");
                in.nextLine();
            }
            selection = in.nextInt();


            switch (selection) {
                case 1:
                    if (!gameStarted) {
                        parser = this.getParser();
                    } else {
                        System.out.println(GAME_STARTED_MESSAGE);
                    }
                    break;

                case 2:
                    if (parser != null) {
                        if (!gameStarted) {
                            game = new Game(parser);
                            gameStarted = true;
                            System.out.println("Game initiated!");
                        } else {
                            System.out.println(GAME_STARTED_MESSAGE);
                        }
                    } else System.out.println("Please provide configuration first (option [1] in menu).");
                    break;

                case 3:
                    if (gameStarted) {
                        printGameBoard();
                    } else System.out.println("Please start a game first.");
                    break;

                case 4:
                    if (gameStarted) {
                        PlayerTurn playerTurn = UserInputs.getMove(game.getGameBoard());
                        game.doPlayerTurn(playerTurn);
                        playerWon = game.checkIfPlayerWon();
                    } else System.out.println("Please start a game first.");
                    break;

                case 5:
                    if (gameStarted) {
                        game.printPlayerMoveHistory();
                    } else System.out.println("Please start a game first.");
                    break;

                case 6:
                    if (gameStarted) {
                        try {
                            game.undoTurn();
                            System.out.println("Undid last turn successfully");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            System.out.println("Was unable to undo turn..");
                        }
                    } else System.out.println("Please start a game first.");
                    break;

                case 7:
                    if (gameStarted) {
                        try {
                            game.redoTurn();
                            System.out.println("Redid last undo successfully");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            System.out.println("Was unable to redo turn..");
                        }
                    }
                    break;
                case 8:
                    if (gameStarted) {
                        System.out.println(game.getPlayerStatistics());
                    }

                default:
                    break;
            }

        } while (selection != 9 && !playerWon);

        if (gameStarted) {
            printGameBoard();
            System.out.println("Player WON!!!!!");
            System.out.println("Player Game Statistics:");
            System.out.println(game.getPlayerStatistics());
        }
    }

    public GameBoardXmlParser getParser() {
        try {
            String path = UserInputs.getPath();
            System.out.println(path);
            GameBoardXmlParser parser = new GameBoardXmlParser(path);
            System.out.println("Xml File Loaded Successfully");
            return parser;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private void printGameBoard() {
        BoardSquare[][] boardSquares = game.getGameBoard().getBoard();
        int rows = game.getGameBoard().getRows();
        int columns = game.getGameBoard().getColumns();
        // print column indices
        String currentRow = "   |";
        for (int i = 1; i <= columns; i++) {
            if (i < 10)
                currentRow += "00" + Integer.toString(i) + "|";
            else
                currentRow += "0" + Integer.toString(i) + "|";
        }
        System.out.println(currentRow);

        // print board lines
        for (int i = 1; i <= rows; i++) {
            // print row separator
            currentRow = "";
            for (int k = 1; k <= columns + game.getMaxRowConstraints() + 1; k++)
                currentRow += "----";
            System.out.println(currentRow);

            // add row index
            currentRow = "";
            if (i < 10)
                currentRow += "00" + Integer.toString(i) + "|";
            else
                currentRow += "0" + Integer.toString(i) + "|";

            // print the actual board state
            for (int j = 0; j < columns; j++)
                currentRow += boardSquares[i - 1][j].toString() + "|";

            // add row constraints
            Constraints constraints = game.getRowConstraint(i - 1);
            for (int n = 0; n < constraints.size(); n++) {
                currentRow += " ";
                Constraint currentConstraint = constraints.getConstraint(n);
                if (currentConstraint.isPerfect()) {
                    currentRow += "*";
                }
                currentRow += currentConstraint.toString();
                currentRow += " ";
            }
            System.out.println(currentRow);
        }

        // print row separator
        currentRow = "";
        for (int k = 1; k <= columns + game.getMaxRowConstraints() + 1; k++)
            currentRow += "----";
        System.out.println(currentRow);

        // print column constraint
        for (int i = 0; i < game.getMaxColumnConstraints(); i++) {
            currentRow = "   |";
            for (int j = 0; j < columns; j++) {
                Constraints currentColumnConstraints = game.getColumnConstraint(j);
                if (currentColumnConstraints.size() > i) {
                    if (currentColumnConstraints.getConstraint(i).isPerfect()) {
                        currentRow += "*";
                    } else {
                        currentRow += " ";
                    }

                    if (currentColumnConstraints.getConstraint(i).getConstraint() < 10) {
                        currentRow += " ";
                    }
                    currentRow += currentColumnConstraints.getConstraint(i).toString() + "|";
                } else {
                    currentRow += "   |";
                }
            }
            System.out.println(currentRow);
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Griddler!");
        System.out.println("------------------------");
        MainMenu mainMenu = new MainMenu();
        mainMenu.draw();
    }

}
