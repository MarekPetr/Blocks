package project.blockArray;
import project.connection.Connection;
import project.items.*;

import java.io.Serializable;

public class BlockArrayItem implements Serializable {
    public AbstractItem item;
    public Connection con;

    public BlockArrayItem(AbstractItem _item) {
        this.item = _item;
    }

    public BlockArrayItem(Connection _con) {
        this.con = _con;
    }
}
