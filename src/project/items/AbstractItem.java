package project.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public abstract class AbstractItem implements Item, Serializable {
    String name;
    double operand;
    public Map<String, Double> inValue = new HashMap<>();
    public Map<String, Double> outValue = new HashMap<>();
    public List<String> links = new ArrayList<>();

    public AbstractItem(String name) {
        this.name = name;
    }

    public AbstractItem(String name, double operand) {
        this.name = name;
        this.operand = operand;
    }

    public void setOperand(double val) {
        this.operand = val;
        System.out.println("Operand for " + name + " was set to " + val);
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getInValue() {
        return this.inValue;
    }

    public Map<String, Double> getOutValue() {
        return this.outValue;
    }

    public List<String> getLinks() { return this.links; }

    public void addLink(String link) { this.links.add(link); }

    public void clean() {
        this.links.clear();
        this.inValue.clear();
        this.outValue.clear();
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

        return true;
    }

    public void setInValue(String _key, double _value) {
        System.out.println("To item " + name + " inValue with key " + _key + " and value " + _value);
        if (_key == null) {
            System.out.println("WARNING: Key is null.");
        }
        this.inValue.put(_key, _value);
    }
}
