package GameXmlParser.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ido on 13/08/2016.
 */
public class Constraints {
    private List<Constraint> constraints;

    public Constraints() {
        this.constraints = new ArrayList<>();
    }

    public Constraints(int size) {
        this.constraints = new ArrayList<>(size);
    }

    public List<Constraint> getConstraintsList() { return constraints; }

    public Constraint getConstraint(int index) {
        return constraints.get(index);
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }
}
