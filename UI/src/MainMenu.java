/* Created by Amitai Handler on 8/14/16. */

import java.util.Scanner;

public class MainMenu {
    private static void draw()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the Griddler");
        int selection;

        do
        {
            System.out.println("[1] New Game");
            System.out.println("[2] Quit");
            selection = in.nextInt();

            switch(selection)
            {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }

        }while (selection != 3);
    }


    public static void main(String[] args)
    {
        draw();
    }

}
