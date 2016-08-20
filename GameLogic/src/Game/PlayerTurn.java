package Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ido on 18/08/2016.
 */
public class PlayerTurn {
    private List<GameMove> moves;
    private String moveDirection;

    public PlayerTurn() {
        this.moves = new ArrayList<GameMove>();
    }

    public PlayerTurn(List<GameMove> i_moves, String i_direction)
    {
        this.moves = i_moves;
        this.moveDirection = i_direction;
    }

    public void addGameMove(GameMove move) {
        moves.add(move);
    }

    public List<GameMove> getMoves() {
        return moves;
    }

    public void undo() {
        if (moves != null) {
            for (GameMove move : moves) {
                move.undo();
            }
        }
    }

    public void printTurn()
    {
        System.out.printf("| Move direction: %s | Start location - row %d col %d | Move size: %d | Move type: %s%n",
                this.moveDirection, this.moves.get(0).getRow() + 1, this.moves.get(0).getColumn() + 1,      // fixing numbering of row/col
                this.moves.size(), this.moves.get(0).getNewBoardSquare().name());
    }
}
