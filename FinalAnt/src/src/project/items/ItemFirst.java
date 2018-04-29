package project.items;

/**
 * Created by petr on 3/29/18.
 */
public class ItemFirst extends AbstractItem {
    public ItemFirst(String name) {
        super(name, 0);
    }

    public void execute() {
        super.outValue.putAll(super.inValue);
    }
}
