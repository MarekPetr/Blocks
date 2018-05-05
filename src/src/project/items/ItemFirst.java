/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.items;

/**
 * This class in internal representation of DraggableNode with type In
 */
public class ItemFirst extends AbstractItem {
    public ItemFirst(String name) {
        super(name, 0);
    }

    public void execute() {
        super.outValue.putAll(super.inValue);
    }
}
