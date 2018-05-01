package project.items;

import java.util.Map;

public class ItemMul extends AbstractItem {
    public ItemMul(String name, double operand) {
        super(name, operand);
    }

    public ItemMul(String name) { super(name); }

    public void execute() {
        for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
            super.outValue.put(entry.getKey(), entry.getValue() * super.operand);
        }
    }
}
