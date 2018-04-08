/**
 * Created by petr on 3/28/18.
 */

import project.blockArray.BlockArray;
import project.blockArray.BlockArrayItem;
import project.connection.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import project.items.*;

public class TestBlocks {
    private AbstractItem itFirst, itPlus, itMinus, itMul, itDiv, itLast;
    private Connection con1, con2, con3, conFirstPlus, conPlusLast;
    private BlockArray Blocks = new BlockArray();

    // Made by Petr Marek
    @Before
    public void setUp() {
        itFirst = new ItemFirst("first", 1);
        itPlus = new ItemPlus("plus", 2, 100);
        itMinus = new ItemMinus("minus", 2, 80);
        itMul = new ItemMul("Mul", 2, 2);
        itDiv = new ItemDiv("Div", 2, 10);
        itLast = new ItemLast("Last", 1);
        conFirstPlus = new Connection(4, itFirst, itPlus);
        conPlusLast = new Connection(5, itPlus, itLast);
    }

    // Vypracoval Petr Marek
    // Tests item connections
    @Test
    public void test01() {
        con1 = new Connection(1, itPlus, itMinus);
        itPlus.execute();
        Assert.assertEquals("Test Plus out value",100, itPlus.outValue, 1.0);
        con1.transferValue();
        Assert.assertEquals("Test Minus in value",100, itMinus.inValue, 1.0);

        itMinus.execute();
        con2 = new Connection(2, itMinus, itMul);
        Assert.assertEquals("Test Minus out value",20, itMinus.outValue, 1.0);
        con2.transferValue();
        Assert.assertEquals("Test Mul in value",20, itMinus.outValue, 1.0);

        itMul.execute();
        con3 = new Connection(3, itMul, itDiv);
        Assert.assertEquals("Test Mul out value",40, itMul.outValue, 1.0);
        con3.transferValue();
        Assert.assertEquals("Test Div in value",40, itDiv.inValue, 1.0);

        itDiv.execute();
        Assert.assertEquals("Test result value",4, itDiv.outValue, 1.0);
    }

    // Made by Jakub Štefanišin
    // Tests adding items to list in the order from first to last
    @Test
    public void test02() {
        Blocks.add_to_list(new BlockArrayItem(itFirst));
        Blocks.add_to_list(new BlockArrayItem(conFirstPlus));
        Blocks.add_to_list(new BlockArrayItem(itPlus));
        Blocks.add_to_list(new BlockArrayItem(conPlusLast));
        Blocks.add_to_list(new BlockArrayItem(itLast));
        Assert.assertEquals("Test First out value", 0, itFirst.outValue, 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100, itLast.inValue, 1.0);
        Blocks.clear();
    }

    // Made by Jakub Štefanišin
    // Tests adding items to list in any order
    @Test
    public void test03() {
        Blocks.add_to_list(new BlockArrayItem(conFirstPlus));
        Blocks.add_to_list(new BlockArrayItem(itFirst));
        Blocks.add_to_list(new BlockArrayItem(itLast));
        Blocks.add_to_list(new BlockArrayItem(conPlusLast));
        Blocks.add_to_list(new BlockArrayItem(itPlus));
        Assert.assertEquals("Test First out value", 0, itFirst.outValue, 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100, itLast.inValue, 1.0);
        Blocks.clear();
    }
}
