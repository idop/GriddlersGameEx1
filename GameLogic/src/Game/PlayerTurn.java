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

    public PlayerTurn(List<GameMove> i_moves) {
        this.moves = i_moves;
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
}
