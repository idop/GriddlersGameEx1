package Game;

/**
 * Created by ido on 13/08/2016.
 */
public enum BoardSquare {
    Black, White, Empty;

    @Override
    public String toString() {
        switch (this) {
            case Black:
                return "| O |";
            case White:
                return "| X |";
            case Empty:
            default:
                return "|   |";
        }
    }
}
