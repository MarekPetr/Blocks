/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.items;

import java.util.Map;

public class ItemPow extends AbstractItem {
    public ItemPow(String name, double operand) {
        super(name, operand);
    }

    public ItemPow(String name) { super(name); }

    public void execute() {
        for (Map.Entry<String, Double> entry : super.inValue.entrySet()) {
                super.outValue.put(entry.getKey(), Math.pow(entry.getValue(), super.operand));
        }
    }
}
