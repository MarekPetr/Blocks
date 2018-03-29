package project.connection;
import project.items.AbstractItem;

public class Connection {
	private final int id;
	private AbstractItem inBlock;
	private AbstractItem outBlock;

	public Connection(int id, AbstractItem input, AbstractItem output) {
		this.id = id;
		setInBlock(input);
		setOutBlock(output);
	}

	public void setInBlock(AbstractItem input) {
		this.inBlock = input;
	}

	public void setOutBlock(AbstractItem output) {
		this.outBlock = output;
	}

	public void transferValue() {
		this.outBlock.inValue = this.inBlock.outValue;
	}

	public int getId() {
		return this.id;
	}

	public AbstractItem getInBlock() {
		return this.inBlock;
	}

	public AbstractItem getOutBlock(){
		return this.outBlock;
	}
}