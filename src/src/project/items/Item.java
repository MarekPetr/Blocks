/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project.items;

/**
 * Interface for creating Blocks
 */
public interface Item {
    /**
    * Returns name (ID) of item.
    * @return name (ID) of item
    */
    String getName();
    
    /**
    * Performs an operation on item. 
    */ 
    void execute();
}
