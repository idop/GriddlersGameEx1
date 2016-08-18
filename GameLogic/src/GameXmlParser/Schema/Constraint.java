package GameXmlParser.Schema;

/**
 * Created by ido on 13/08/2016.
 */
public class Constraint {
    private final int constraint;
    private boolean isPerfect;

    public Constraint(int constraint) {
        this.constraint = constraint;
        isPerfect = false;
    }

    public void setPerfect(boolean perfect) {
        isPerfect = perfect;
    }

    public boolean isPerfect() {

        return isPerfect;
    }

    @Override
    public String toString() {
        return Integer.toString(constraint);
    }

    public int getConstraint() {
        return constraint;
    }
}
