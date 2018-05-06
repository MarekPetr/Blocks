/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.items;

/**
 * This class in internal representation of DraggableNode with type Out
 */
public class ItemLast extends AbstractItem {
    public ItemLast(String name) {
        super(name, 0);
    }

    public void execute() {
        super.outValue.putAll(super.inValue);
        System.out.println(super.outValue);
    }
}
