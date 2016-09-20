package Game;

/**
 * Created by ido on 13/08/2016.
 */
public enum BoardSquare {
    Black, White, Empty;

    @Override
    public String toString() {
        String res;
        switch (this) {
            case Black:
                return " BLACK ";
            case White:
                return " WHITE ";
            case Empty:
            default:
                return " EMPTY  ";
        }
    }
}
