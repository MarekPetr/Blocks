/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
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

    /**
     * Sets Block type based on DraggableNode type
     * @param itemType type to be set
     */
    public void setType(DragIconType itemType) { this.type = itemType; }

    /**
     * Return Block type
     * @return Block type
     */
    public DragIconType getType() { return this.type; }

    /**
     * Sets Block's coordinations in layour
     * @param x x coordination
     * @param y y coordination
     */
    public void setCoords(double x, double y) {
        this.coordsX = x;
        this.coordsY = y;
    }

    /**
     * Return x coordinate of Block in layout
     * @return x coordinate of Block in layout
     */
    public double getX() {
        return this.coordsX;
    }

    /**
     * Return y coordinate of Block in layout
     * @return y coordinate of Block in layout
     */
    public double getY() {
        return this.coordsY;
    }

    /**
     * Returns current coordinates of Block in layout
     * @return current coordinates of Block in layout
     */
    public Point2D getCoords(Point2D coords) {
        return new Point2D(this.coordsX, this.coordsY);
    }

    /**
     * Sets Block operand value
     * @param val operand value to be set
     */
    public void setOperand(double val) {
        this.operand = val;
    }

    /**
     * Return operand value of Block
     * @return operand value of Block
     */
    public double getOperand() {
        return this.operand;
    }

    /**
     * Return Block's name
     * @return Block's name
     */
    public String getName() {
        return id;
    }

    /**
     * Returns Block's input value map
     * @return Block's input value map
     */
    public Map<String, Double> getInValue() {
        return this.inValue;
    }

    /**
     * Returns Block's output value map
     * @return Block's output value map
     */
    public Map<String, Double> getOutValue() {
        return this.outValue;
    }

    /**
     * Returns List of identifiers of all Connections coming out of this Block
     * @return List of identifiers of all Connections coming out of this Block
     */
    public List<String> getLinks() { return this.links; }

    /**
     * Adds identifier of new Connection coming out of this Block
     * @param link identifier of Connection
     */
    public void addLink(String link) { this.links.add(link); }

    /**
     * Cleans input and output map of this Block and cleans List of identifiers of Connections coming out of this Block
     */
    public void clean() {
        this.links.clear();
        this.inValue.clear();
        this.outValue.clear();
    }

    /**
     * Returns hashCode for the identifier of this Block
     * @return hashCode for the identifier of this Block
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns true if this Block is equal to the one specified in function parameter
     * @param obj Block to be compared
     * @return true if this Block is equal to the one specified in function parameter
     */
    @Override
    public boolean equals(Object obj) {
        AbstractItem item = (AbstractItem) obj;

        if (!item.id.equals(this.id)) {
            return false;
        }

        return true;
    }

    /**
     * Adds key and value to this Block's input map
     * @param _key key to be added
     * @param _value value to be added
     */
    public void setInValue(String _key, double _value) {
        if (_key == null) {
            printErr("Key is null.");
        }
        this.inValue.put(_key, _value);
    }
}
