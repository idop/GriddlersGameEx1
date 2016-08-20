package Game.Player;

/**
 * Created by ido on 18/08/2016.
 */
public class PlayerGameStatistics {
    private int numberOfTurns = 0;
    private int numberOfUndoTurns = 0;
    private int numberOfRedoTurns = 0;
    private float score = 0;

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void incNumberOfTurns() {
        numberOfTurns++;
    }

    public int getNumberOfUndoTurns() {
        return numberOfUndoTurns;
    }

    public int getNumberOfRedoTurns() {
        return numberOfRedoTurns;
    }

    public void incNumberOfUndoTurns() {
        numberOfUndoTurns++;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void incNumberOfRedoMoves() {
        numberOfRedoTurns++;
    }


    @Override
    public String toString()
    {
        String res = "Turns done until now: " + getNumberOfTurns() + " | Undo actions done: " + getNumberOfUndoTurns() +  " | Player score: " + getScore();
        return res;
    }
}
