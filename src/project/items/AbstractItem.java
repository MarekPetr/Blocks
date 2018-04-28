package project.items;

import javafx.geometry.Point2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public abstract class AbstractItem implements Item, Serializable {
    public String id;
    public double coordsX;
    public double coordsY;
    double operand;
    public Map<String, Double> inValue = new HashMap<>();
    public Map<String, Double> outValue = new HashMap<>();
    public List<String> links = new ArrayList<>();

    public AbstractItem(String id) {
        this.id = id;
    }

    public AbstractItem(String id, double operand) {
        this.id = id;
        this.operand = operand;
    }

    public void setCoords(double _x, double _y) {
        System.out.println("Coords set to x: " + coordsX + ", y:" + coordsY);
        this.coordsX = _x;
        this.coordsY = _y;
    }

    public Point2D getCoords(Point2D coords) {
        return new Point2D(this.coordsX, this.coordsY);
    }

    public void setOperand(double val) {
        this.operand = val;
        System.out.println("Operand for " + id + " was set to " + val);
    }

    public String getName() {
        return id;
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
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        AbstractItem item = (AbstractItem) obj;

        if (!item.id.equals(this.id)) {
            return false;
        }

        return true;
    }

    public void setInValue(String _key, double _value) {
        System.out.println("To item " + id + " inValue with key " + _key + " and value " + _value);
        if (_key == null) {
            System.out.println("WARNING: Key is null.");
        }
        this.inValue.put(_key, _value);
    }
}
