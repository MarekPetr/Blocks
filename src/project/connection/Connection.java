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

	/**
	 * Sets AbstractItem instance as input for Connection
	 * @param input AbstractItem instance to be set as Connection input
	 */
	private void setInBlock(AbstractItem input) {
	    this.inBlock = input;
	    this.inBlock.addLink(getId());
	}

	/**
	 * Sets AbstractItem instance as output for Connection
	 * @param output AbstractItem instance to be set as Connection output
	 */
	private void setOutBlock(AbstractItem output) {
		this.outBlock = output;
	}

	/**
	 * Copy values from Connection input block to Connection output block
	 */
	public void transferValue() { this.outBlock.inValue.putAll(this.inBlock.outValue); }

	/**
	 * Returns Connection id
	 * @return Connection id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Returns Connection input block
	 * @return Connection input block
	 */
	public AbstractItem getInBlock() {
		return this.inBlock;
	}

	/**
	 * Connection output block
	 * @return Connection output block
	 */
	public AbstractItem getOutBlock() {
		return this.outBlock;
	}

	/**
	 * Return true if this object is equal to one specified in function parameter
	 * @param obj Connection to compare
	 * @return true if this object is equal to one specified in function parameter
	 */
	public boolean equals(Object obj) {
		Connection con = (Connection) obj;

		if (this.id != con.id) { return false; }

		if (this.inBlock != con.inBlock || this.outBlock != con.outBlock) { return false; }

		return  true;
	}

	/**
	 * Return current outValue of Connection input block
	 * @return current outValue of Connection input block
	 */
	public Map<String, Double> showValue() {
		return this.inBlock.outValue;
	}
}