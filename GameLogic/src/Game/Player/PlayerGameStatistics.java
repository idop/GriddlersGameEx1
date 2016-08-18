package Game.Player;

/**
 * Created by ido on 18/08/2016.
 */
public class PlayerGameStatistics {
    private int numberOfMoves;
    private int numberOfUndoMoves;
    private float score;

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public int getNumberOfUndoMoves() {
        return numberOfUndoMoves;
    }

    public void setNumberOfUndoMoves(int numberOfUndoMoves) {
        this.numberOfUndoMoves = numberOfUndoMoves;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
