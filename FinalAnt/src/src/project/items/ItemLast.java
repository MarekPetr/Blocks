package project.items;


/**
 * Created by petr on 3/29/18.
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
