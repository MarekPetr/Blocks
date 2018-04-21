/**
 * Created by petr on 3/28/18.
 */

import project.blockArray.BlockArray;
import project.blockArray.BlockArrayItem;
import project.connection.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import project.itemList.ItemList;
import project.items.*;


public class TestBlocks {
    private AbstractItem itFirst, itPlus, itMinus, itMul, itDiv, itLast;
    private Connection con1, con2, con3, conFirstPlus, conPlusLast;
    private ItemList Blocks = new ItemList();

    // Made by Petr Marek
    @Before
    public void setUp() {
        itFirst = new ItemFirst("first", 1);
        itPlus = new ItemPlus("plus", 2, 100);
        itMinus = new ItemMinus("minus", 2, 80);
        itMul = new ItemMul("Mul", 2, 2);
        itDiv = new ItemDiv("Div", 2, 10);
        itLast = new ItemLast("Last", 1);
        itFirst.createConnection(itPlus);
        itPlus.createConnection(itLast);
    }

    // Vypracoval Petr Marek
    // Tests item connections
    @Test
    public void test01() {
        itPlus.createConnection(itMinus);
        itPlus.state.put("value", (double) 0);
        itPlus.execute();
        Assert.assertEquals("Test Plus out value",100.0, itPlus.outValue.get("value"), 1.0);
        itPlus.links.get(0).transferValue();
        Assert.assertEquals("Test Minus in value",100.0, itMinus.inValue.get("value"), 1.0);

        itMinus.execute();
        itMinus.createConnection(itMul);
        Assert.assertEquals("Test Minus out value",20.0, itMinus.outValue.get("value"), 1.0);
        itMinus.links.get(0).transferValue();
        Assert.assertEquals("Test Mul in value",20.0, itMul.inValue.get("value"), 1.0);

        itMul.execute();
        itMul.createConnection(itDiv);
        Assert.assertEquals("Test Mul out value",40.0, itMul.outValue.get("value"), 1.0);
        itMul.links.get(0).transferValue();
        Assert.assertEquals("Test Div in value",40.0, itDiv.inValue.get("value"), 1.0);

        itDiv.execute();
        Assert.assertEquals("Test result value",4.0, itDiv.outValue.get("value"), 1.0);
    }

    // Made by Jakub Štefanišin
    // Tests adding items to list in the order from first to last
    @Test
    public void test02() {
        Blocks.addToList(itFirst);
        Blocks.addToList(itPlus);
        Blocks.addToList(itLast);
        Assert.assertEquals("Test First out value", 0.0, itFirst.state.get("value"), 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100.0, itLast.inValue.get("value"), 1.0);
        Blocks.clear();
    }
    /*
    @Test
    public void test03() {
        Blocks.addToList(new BlockArrayItem(conFirstPlus));
        Blocks.addToList(new BlockArrayItem(itFirst));
        Blocks.addToList(new BlockArrayItem(itLast));
        Blocks.addToList(new BlockArrayItem(conPlusLast));
        Blocks.addToList(new BlockArrayItem(itPlus));
        Assert.assertEquals("Test First out value", 0.0, itFirst.state.get("value"), 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100.0, itLast.inValue.get("value"), 1.0);
        Blocks.clear();
    }
    */
}
