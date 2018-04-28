package project.GUI;

import project.blockArray.BlockArray;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by petr on 4/27/18.
 */
public class LoadScheme {
    private BlockArray blockArr;
    private RootLayout rootLayout;

    public LoadScheme(BlockArray blockArray, RootLayout root) {
        this.blockArr = blockArray;
        this.rootLayout = root;
    }

    public void printBlocks() {
        // print IDs ant types of all blocks
        for (int i = 0; i < blockArr.size(); i++) {
            System.out.println("Name: " + blockArr.get(i).item.getName() + ", type: " + blockArr.get(i).item.getType());
        }
    }
}
