package project.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public abstract class AbstractItem implements Item {
    int numberOfPorts;
    String name;
    public Map<String, Double> state = new HashMap<>();
    double operand;
    public Map<String, Double> inValue = new HashMap<>();
    public Map<String, Double> outValue = new HashMap<>();
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

    public Map<String, Double> getState() {
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

    public void setInValue(String _key, double _value) {
        this.inValue.put(_key, _value);
    }
}
