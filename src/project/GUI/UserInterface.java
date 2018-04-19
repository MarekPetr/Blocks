package project.GUI;

import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static project.GUI.ItemType.*;

/**
 * Created by petr on 4/17/18.
 */
public class UserInterface {
    private Stage window;
    private Scene scene;
    private Group root;

    private Line line;
    private double circRadius = 50;

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private double menuWidth = 1.8*circRadius;

    public UserInterface(Stage window) {
        this.window = window;
    }


    public void init() {
        window.setTitle("Title of the Window");

        root = new Group();
        scene = new Scene(root, 630, 750);
        line = new Line();

        double y = circRadius;
        double width = circRadius;
        double space = circRadius/2;
        double offset = space + width;
        double x = width;

        // draws menuItem items

        root.getChildren().add(newCircle(x, y, true, itemPlus));
        x += offset;
        root.getChildren().add(newCircle(x, y, true, itemMinus));
        x += offset;
        root.getChildren().add(newCircle(x, y, true, itemMul));
        x += offset;

        drawLine();
        
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            // deletes last line
            root.getChildren().remove(line);
            // draws updated line
            drawLine();
        });

        window.setTitle("Drawing a Circle");
        window.setScene(scene);
        window.setMinHeight(menuWidth+100);
        window.setMinWidth(x);
        window.show();
    }

    public void drawLine() {
        line.setStartX(0.0f);
        line.setStartY(menuWidth);
        line.setEndX(scene.getWidth());
        line.setEndY(menuWidth);
        root.getChildren().add(line);
    }
    
    // creates new circle
    public CircleItem newCircle(double x, double y, boolean menuItem, ItemType type) {
        CircleItem circle = new CircleItem();

        //Setting the properties of the circle
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(circRadius/2);
        circle.setType(type);

        circle.setStroke(Color.BLACK);
        if (circle.getType() == itemPlus) {
            circle.setFill(Color.LIGHTGRAY);
        } else if (circle.getType() == itemMinus) {
            circle.setFill(Color.BLACK);
        } else if (circle.getType() == itemMul) {
            circle.setFill(Color.BLUE);
        }
        circle.toFront();
        
        if (menuItem) {
            System.out.printf("menuItem\n");
            circle = IconToItem(circle);
        } else {
            System.out.printf("setMovable\n");
            circle = setMovable(circle);
        }
        return circle;
    }
    
    // copy circle from menu to workspace
    public CircleItem IconToItem(CircleItem circle) {

        circle.setOnMousePressed(e -> {
            CircleItem circ = ((CircleItem)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY) {
                circ = newCircle(circ.getCenterX(), circ.getCenterY()+100, false, circ.getType());
                root.getChildren().add(circ);
                System.out.printf("taham ven\n");

            }
        });
        return circle;
    }

    public CircleItem setMovable(CircleItem circle) {

        circle.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                CircleItem circ = ((CircleItem)(e.getSource()));
                double offsetX = e.getSceneX() - orgSceneX;
                double offsetY = e.getSceneY() - orgSceneY;

                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                double leftBorder = -circ.getCenterX() + circ.getRadius();
                double rightBorder = scene.getWidth() - circ.getRadius() - circ.getCenterX();
                double upBorder = -circ.getCenterY() + menuWidth + circ.getRadius();
                double downBorder = scene.getHeight() - circ.getRadius() - circ.getCenterY();

                if (newTranslateX >= leftBorder && newTranslateX <= rightBorder) {
                    circ.setTranslateX(newTranslateX);
                }
                if (newTranslateY >= upBorder && newTranslateY <= downBorder)
                    circ.setTranslateY(newTranslateY);
            }
        });

        // Sets new item coordinates
        circle.setOnMousePressed(e -> {
            CircleItem circ = ((CircleItem)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY) {
                circ.toFront();
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = circ.getTranslateX();
                orgTranslateY = circ.getTranslateY();
            }
            else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.printf("Udela port\n");
            }
        });

        return circle;

    }
}
