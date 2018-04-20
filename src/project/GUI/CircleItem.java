package project.GUI;
import javafx.scene.shape.Circle;

/**
 * Created by petr on 4/19/18.
 */
public class CircleItem extends Circle {
    private ItemType type;

    public CircleItem (double centerX, double centerY, double radius, ItemType type) {
        super(centerX, centerY, radius);
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }
}
