package project.items;

import java.util.Map;

public class ItemMinus extends AbstractItem {
    public ItemMinus(String name, double operand) {
        super(name, operand);
    }

    public ItemMinus(String name) { super(name); }

    public void execute() {
        for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
            super.outValue.put(entry.getKey(), entry.getValue() - super.operand);
        }
    }
}
