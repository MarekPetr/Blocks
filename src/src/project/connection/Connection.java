/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.connection;

import project.items.AbstractItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the connection between AbstractItem elements
 */
public class Connection implements Serializable {
	private final String id;
	private AbstractItem inBlock;
	private AbstractItem outBlock;

	public Connection(String id, AbstractItem input, AbstractItem output) {
		this.id = id;
		setInBlock(input);
		setOutBlock(output);
	}

	private void setInBlock(AbstractItem input) {
	    this.inBlock = input;
	    this.inBlock.addLink(getId());
	}

	private void setOutBlock(AbstractItem output) { this.outBlock = output; }

	public void transferValue() { this.outBlock.inValue.putAll(this.inBlock.outValue); }

	public String getId() { return this.id; }

	public AbstractItem getInBlock() { return this.inBlock; }

	public AbstractItem getOutBlock(){ return this.outBlock; }

	public boolean equals(Object obj) {
		Connection con = (Connection) obj;

		if (this.id != con.id) { return false; }

		if (this.inBlock != con.inBlock || this.outBlock != con.outBlock) { return false; }

		return  true;
	}

	public Map<String, Double> showValue() { return this.inBlock.outValue; }
}