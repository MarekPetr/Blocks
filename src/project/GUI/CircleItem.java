package project.GUI;

import javafx.scene.shape.Circle;
import project.items.Item;

/**
 * Created by petr on 4/19/18.
 */
public class CircleItem extends Circle {
    private ItemType type;

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }
}
