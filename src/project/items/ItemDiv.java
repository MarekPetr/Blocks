package project.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */
public class ItemDiv extends AbstractItem {
    public ItemDiv(String name, int numberOfPorts, double operand) {
        super(name, numberOfPorts, operand);
    }

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
