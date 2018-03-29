package project.items;

/**
 * Created by petr on 3/28/18.
 */
public class ItemDiv extends AbstractItem {
    public ItemDiv(String name, int numberOfPorts, double operand) {
        super(name, numberOfPorts, operand);
    }

    public void execute() {
        super.state = inValue;
        if (super.operand != 0.0)
            super.state /= super.operand;
        else {
            throw new ArithmeticException("Division by zero!");
        }
        super.outValue = super.state;
    }
}
