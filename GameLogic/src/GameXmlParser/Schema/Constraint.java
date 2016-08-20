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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraint)) return false;

        Constraint that = (Constraint) o;

        if (constraint != that.getConstraint()) return false;
        return isPerfect == that.isPerfect();

    }

    @Override
    public int hashCode() {
        int result = constraint;
        result = 31 * result + (isPerfect ? 1 : 0);
        return result;
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
