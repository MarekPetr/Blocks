package items;
import repository.Item;
/**
 * Created by petr on 3/28/18.
 */
public abstract class AbstractItem implements Item {
    int numberOfPorts;
    String name;
    double state;
    double operand;
    public double inValue = 0;
    public double outValue = 0;

    public AbstractItem(String name, int numberOfPorts, double operand) {
        this.name = name;
        this.numberOfPorts = numberOfPorts;
        this.operand = operand;
    }

    public int numberOfPorts() {
        return numberOfPorts;
    }

    public String getName() {
        return name;
    }

    public double getState() {
        return state;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        AbstractItem item = (AbstractItem) obj;

        if (!item.name.equals(this.name)) {
            return false;
        }

        if (item.numberOfPorts != this.numberOfPorts) {
            return false;
        }

        return true;
    }

    public void setInValue(double _value) {
        this.inValue = _value;
    }
}
