/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.items;

import java.util.Map;

import static project.Errors.printErr;
public class ItemDiv extends AbstractItem {
    public ItemDiv(String name, double operand) {
        super(name, operand);
    }

    public ItemDiv(String name) { super(name); }

    public void execute() {
        if (super.operand != 0.0) {
            for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
                super.outValue.put(entry.getKey(), entry.getValue() / super.operand);
            }
        } else {
            printErr("Division by zero!");
            throw new ArithmeticException("Division by zero!");
        }
    }
}
