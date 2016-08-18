package Game;

/**
 * Created by ido on 19/08/2016.
 */
public class PlayerTurnException extends Exception {
    public PlayerTurnException(String message) {
        super(message);
    }

    public PlayerTurnException(String message, Throwable cause) {
        super(message, cause);
    }
}
