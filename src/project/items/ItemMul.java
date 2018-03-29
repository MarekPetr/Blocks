package project.items;

/**
 * Created by petr on 3/28/18.
 */
public class ItemMul extends AbstractItem{
    public ItemMul(String name, int numberOfPorts, double operand) {
        super(name, numberOfPorts, operand);
    }

    public void execute(){
        super.state = inValue;
        super.state *= super.operand;
        super.outValue = super.state;
    }
}
