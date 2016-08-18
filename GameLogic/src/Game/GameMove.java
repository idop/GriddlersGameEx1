package Game;

/**
 * Created by amitaihandler on 8/18/16.
 */
public class GameMove
{
    private int row;
    private int column;
    public MoveDirection direction;

    public enum MoveDirection
    {
        Horizontal,
        Diagonal
    }

    public GameMove(int i_row, int i_column, int i_direction)
    {
        row     = i_row;
        column  = i_column;
        if (i_direction == 0)
        {
           direction =  MoveDirection.Horizontal;
        }
        else if (i_direction == 1)
        {
            direction = MoveDirection.Diagonal;
        }
    }
}
