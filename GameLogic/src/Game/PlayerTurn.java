package Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ido on 18/08/2016.
 */
public class PlayerTurn {
    private List<GameMove> moves;

    public PlayerTurn() {
        this.moves = new ArrayList<GameMove>();
    }

    public void addGameMove(GameMove move) {
        moves.add(move);
    }

    public void undo() {
        if (moves != null) {
            for (GameMove move : moves) {
                move.undo();
            }
        }
    }
}
