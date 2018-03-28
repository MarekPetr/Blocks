package project.connection;
import project.items.AbstractItem;

public class Connection {
	private final int id;
	private AbstractItem inBlock;
	private AbstractItem outBlock;

	public Connection(int _id) {
		this.id = _id;
	}

	public Connection(int _id, AbstractItem _input, AbstractItem _output) {
		this.id = _id;
		setInBlock(_input);
		setOutBlock(_output);
	}

	public void setInBlock(AbstractItem _input) {
		this.inBlock = _input;
	}

	public void setOutBlock(AbstractItem _output) {
		this.outBlock = _output;
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