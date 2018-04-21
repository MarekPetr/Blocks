package project.items;

import project.connection.Connection;

import java.util.*;

/**
 * Created by petr on 3/28/18.
 */
public abstract class AbstractItem implements Item {
    String name;
    int numberOfPorts;
    double operand;
    public Map<String, Double> state = new HashMap<>();
    public Map<String, Double> inValue = new HashMap<>();
    public Map<String, Double> outValue = new HashMap<>();
    public List<Connection> links = new ArrayList<>();

    public AbstractItem(String name, int numberOfPorts, double operand) {
        this.name = name;
        this.numberOfPorts = numberOfPorts;
        this.operand = operand;
    }

    public void createConnection() {
        this.links.add(new Connection(UUID.randomUUID().toString(), this));
    }

    public void createConnection(AbstractItem output) {
        createConnection();
        setConnection(output);
    }

    public void setConnection(AbstractItem output) {
        this.links.get(this.links.size()).setOutBlock(output);
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
