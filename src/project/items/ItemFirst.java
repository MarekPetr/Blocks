package project.items;

/**
 * Created by petr on 3/29/18.
 */
public class ItemFirst extends AbstractItem {
    public ItemFirst(String name, int numberOfPorts) {
        super(name, numberOfPorts, 0);
        super.state.put("value", (double) 0);
    }

    public void execute() {
        super.outValue.putAll(super.state);
    }
}
