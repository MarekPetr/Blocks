package project.GUI;

import javafx.scene.shape.Line;

/**
 * Created by petr on 4/20/18.
 */
public class Linking extends Line {
    public Linking(CircleItem startItem, CircleItem endItem) {
        startXProperty().bind(startItem.centerXProperty());
        startYProperty().bind(startItem.centerYProperty());
        endXProperty().bind(endItem.centerXProperty());
        endYProperty().bind(endItem.centerYProperty());
    }
}
