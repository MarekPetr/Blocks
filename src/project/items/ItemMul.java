package project.items;

import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public class ItemMul extends AbstractItem {
    public ItemMul(String name, int numberOfPorts, double operand) {
        super(name, numberOfPorts, operand);
    }

    public void execute() {
        for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
            super.outValue.put(entry.getKey(), entry.getValue() * super.operand);
        }
    }
}
