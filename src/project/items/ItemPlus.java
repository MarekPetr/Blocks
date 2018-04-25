package project.items;

import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public class ItemPlus extends AbstractItem {
    public ItemPlus(String name, double operand) {
        super(name, operand);
    }

    public ItemPlus(String name) { super(name); }

    public void execute() {
        for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
            super.outValue.put(entry.getKey(), entry.getValue() + super.operand);
        }
    }
}
