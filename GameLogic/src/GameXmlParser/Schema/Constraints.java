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

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public Constraints(int size) {
        this.constraints = new ArrayList<>(size);
    }

    public int size() {
        return constraints.size();
    }

    public Constraint getConstraint(int index) {
        return constraints.get(index);
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraints)) return false;

        Constraints that = (Constraints) o;

        return constraints.equals(that.getConstraints());

    }


}
