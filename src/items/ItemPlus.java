package items;

/**
 * Created by petr on 3/28/18.
 */
public class ItemPlus extends AbstractItem{
    public ItemPlus(String name, int numberOfPorts, double operand) {
        super(name, numberOfPorts, operand);
    }

    public void execute(){
        super.state = inValue;
        super.state += super.operand;
        super.outValue = super.state;
    }
}
