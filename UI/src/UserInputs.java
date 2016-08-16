import java.util.Scanner;

/**
 * Created by amitaihandler on 8/14/16.
 */
public  class UserInputs
{
    public static String getPath()
    {
        String res;
        Scanner in = new Scanner(System.in);
        System.out.println("Please provide file path:");
        res = in.nextLine();

        return res;
    }
}
