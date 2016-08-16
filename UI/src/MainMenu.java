/* Created by Amitai Handler on 8/14/16. */
import java.util.Scanner;
import GameXmlParser.*;

public class MainMenu {
    private void draw()
    {
        Scanner in = new Scanner(System.in);
        GameBoardXmlParser parser = null;

        int selection;

        do
        {
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
                    if(parser != null) {}
                    else System.out.println("Please provide configuration first (option [1] in menu).");
                    break;
                case 3:
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
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to the Griddler!");
        System.out.println("------------------------");
        MainMenu mainMenu = new MainMenu();
        mainMenu.draw();
    }

}
