package project.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public class ItemDiv extends AbstractItem {
    public ItemDiv(String name, double operand) {
        super(name, operand);
    }

    public ItemDiv(String name) { super(name); }

    public void execute() {
        if (super.operand != 0.0)
            for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
                super.outValue.put(entry.getKey(), entry.getValue() / super.operand);
            }
        else {
            throw new ArithmeticException("Division by zero!");
        }
    }
}
