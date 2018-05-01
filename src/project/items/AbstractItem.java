package project.items;

import javafx.geometry.Point2D;
import project.GUI.DragIconType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static project.Errors.printErr;

public abstract class AbstractItem implements Item, Serializable {
    public String id;
    public double coordsX;
    public double coordsY;
    double operand;
    public DragIconType type = null;
    public Map<String, Double> inValue = new HashMap<>();
    public Map<String, Double> outValue = new HashMap<>();
    public List<String> links = new ArrayList<>();
    private static final long serialVersionUID = 946567827079246598L;

    public AbstractItem(String id) {
        this.id = id;
    }

    public AbstractItem(String id, double operand) {
        this.id = id;
        this.operand = operand;
    }

    public void setType(DragIconType itemType) { this.type = itemType; }

    public DragIconType getType() { return this.type; }



    public void setCoords(double x, double y) {
        //System.out.println("Coords set to x: " + coordsX + ", y:" + coordsY);
        this.coordsX = x;
        this.coordsY = y;
    }

    public double getX() {
        return this.coordsX;
    }

    public double getY() {
        return this.coordsY;
    }

    public Point2D getCoords(Point2D coords) {
        return new Point2D(this.coordsX, this.coordsY);
    }

    public void setOperand(double val) {
        this.operand = val;
        //System.out.println("Operand for " + id + " was set to " + val);
    }

    public double getOperand() {
        return this.operand;
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
        //System.out.println("To item " + id + " inValue with key " + _key + " and value " + _value);
        if (_key == null) {
            printErr("Key is null.");
        }
        this.inValue.put(_key, _value);
    }
}
