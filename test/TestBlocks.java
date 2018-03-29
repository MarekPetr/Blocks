/**
 * Created by petr on 3/28/18.
 */

import project.connection.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import project.items.*;

public class TestBlocks {
    private AbstractItem itFirst, itPlus, itMinus, itMul, itDiv;
    private Connection con1, con2, con3;

    @Before
    public void setUp(){
        itPlus = new ItemPlus("plus", 2, 100);
        itMinus = new ItemMinus("minus", 2, 80);
        itMul = new ItemMul("Mul", 2, 2);
        itDiv = new ItemDiv("Div", 2, 10);
    }

    @Test
    public void test() {
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


}
