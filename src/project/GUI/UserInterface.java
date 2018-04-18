package project.GUI;

import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        for (int i = 0; i < 5; i++) {
            root.getChildren().add(getCircle(x, y, true));
            x += offset;
        }

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

    public Circle getCircle(double x, double y, boolean menuItem) {
        Circle circle = new Circle();

        //Setting the properties of the circle
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(circRadius/2);

        circle.setStroke(Color.BLACK);
        circle.setFill(Color.LIGHTGRAY);
        circle.toFront();
        
        if (menuItem) {
            System.out.printf("menuItem\n");
            circle = newCircle(circle);
        } else {
            System.out.printf("setMovable\n");
            circle = setMovable(circle);
        }
        return circle;
    }

    public Circle newCircle(Circle circle) {

        circle.setOnMousePressed(e -> {
            Circle circ = ((Circle)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY) {
                circ = getCircle(circ.getCenterX(), circ.getCenterY()+100, false);
                root.getChildren().add(circ);
                System.out.printf("taham ven\n");

            }
        });
        return circle;
    }

    public Circle setMovable(Circle circle) {

        circle.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                Circle circ = ((Circle)(e.getSource()));
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
            Circle circ = ((Circle)(e.getSource()));
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
