package project.items;

import java.util.Map;

/**
 * Created by petr on 3/28/18.
 */

public interface Item {
    String getName();

    int numberOfPorts();

    void execute();
}
