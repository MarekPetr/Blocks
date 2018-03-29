package project.items;

/**
 * Created by petr on 3/29/18.
 */
public class ItemLast extends AbstractItem {
    public ItemLast(String name, int numberOfPorts) {
        super(name, numberOfPorts, 0);
    }

    public void execute() {
        super.state = inValue;
        super.outValue = 0;
    }
}
