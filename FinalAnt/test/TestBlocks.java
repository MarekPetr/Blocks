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

import java.io.*;


public class TestBlocks {
    private AbstractItem itFirst, itPlus, itMinus, itMul, itDiv, itLast;
    private Connection con1, con2, con3, conFirstPlus, conPlusLast;
    private BlockArray Blocks = new BlockArray();

    // Made by Petr Marek
    @Before
    public void setUp() {
        itFirst = new ItemFirst("first");
        itPlus = new ItemPlus("plus", 100);
        itMinus = new ItemMinus("minus", 80);
        itMul = new ItemMul("Mul", 2);
        itDiv = new ItemDiv("Div", 10);
        itLast = new ItemLast("Last");
        conFirstPlus = new Connection("conFirstPlus", itFirst, itPlus);
        conPlusLast = new Connection("conPlusLast", itPlus, itLast);
    }

    // Vypracoval Petr Marek
    // Tests item connections
    @Test
    public void test01() {
        con1 = new Connection("con1", itPlus, itMinus);
        itPlus.setInValue("value", 0.0);
        itPlus.execute();
        Assert.assertEquals("Test Plus out value",100.0, itPlus.outValue.get("value"), 1.0);
        con1.transferValue();
        Assert.assertEquals("Test Minus in value",100.0, itMinus.inValue.get("value"), 1.0);

        itMinus.execute();
        con2 = new Connection("con2", itMinus, itMul);
        Assert.assertEquals("Test Minus out value",20.0, itMinus.outValue.get("value"), 1.0);
        con2.transferValue();
        Assert.assertEquals("Test Mul in value",20.0, itMul.inValue.get("value"), 1.0);

        itMul.execute();
        con3 = new Connection("con3", itMul, itDiv);
        Assert.assertEquals("Test Mul out value",40.0, itMul.outValue.get("value"), 1.0);
        con3.transferValue();
        Assert.assertEquals("Test Div in value",40.0, itDiv.inValue.get("value"), 1.0);

        itDiv.execute();
        Assert.assertEquals("Test result value",4.0, itDiv.outValue.get("value"), 1.0);

        itMinus.clean();
        itMul.clean();
        itLast.clean();
    }

    // Made by Jakub Štefanišin
    // Tests adding items to list in the order from first to last
    @Test
    public void test02() {
        itFirst.setInValue("value", 0.0);
        Blocks.addToList(itFirst);
        Blocks.connections.add(conFirstPlus);
        Blocks.addToList(itPlus);
        Blocks.connections.add(conPlusLast);
        Blocks.addToList(itLast);
        Assert.assertEquals("Test First out value", 0.0, itFirst.inValue.get("value"), 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100.0, itLast.inValue.get("value"), 1.0);
        Blocks.clear();
    }

    @Test
    public void test03() {
        itFirst.setInValue("value", 0.0);
        Blocks.connections.add(conFirstPlus);
        Blocks.addToList(itFirst);
        Blocks.addToList(itLast);
        Blocks.connections.add(conPlusLast);
        Blocks.addToList(itPlus);
        Assert.assertEquals("Test First out value", 0.0, itFirst.getInValue().get("value"), 1.0);
        Blocks.run();
        Assert.assertEquals("Test Last in value", 100.0, itLast.inValue.get("value"), 1.0);
        Blocks.clear();
    }
    @Test
    public void test04() throws IOException {
        itFirst.setInValue("value", 0.0);
        Blocks.addToList(itFirst);
        Blocks.connections.add(conFirstPlus);
        Blocks.addToList(itPlus);
        Blocks.connections.add(conPlusLast);
        Blocks.addToList(itLast);
        FileOutputStream fos = new FileOutputStream("save.txt");
        ObjectOutput oos = new ObjectOutputStream(fos);
        oos.writeObject(Blocks);
        oos.close();
    }

    @Test
    public void test05() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("save.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        BlockArray bb = (BlockArray) ois.readObject();
        ois.close();
        Assert.assertEquals("Test First in value", 0.0, bb.get(0).inValue.get("value"), 1.0);
        bb.run();
        Assert.assertEquals("Test Last in value", 100.0, bb.get(bb.size() - 1).inValue.get("value"), 1.0);
    }
}
