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
        super.state.putAll(super.inValue);
        for (Map.Entry<String, Double> entry : super.state.entrySet()) {
            super.state.put(entry.getKey(), entry.getValue() * super.operand);
        }
        super.outValue.putAll(super.state);
    }
}
