package project.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by petr on 4/17/18.
 */
public class UserInterface {
    Button button;
    Stage window;
    Scene scene;
    Group root;

    Line line;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    double menuWidth = 100;

    public UserInterface(Stage window) {
        this.window = window;
    }


    public void init() {
        window.setTitle("Title of the Window");
        button = new Button();
        button.setText("Click me");
        button.setOnAction(e -> System.out.println("Lambda expressions are awesome!"));

        root = new Group();
        scene = new Scene(root, 630, 750);
        line = new Line();


        double y = 20;
        double width = 40;
        double space = 15;
        double offset = 2*space + width;
        double x = space;
        //root.getChildren().add(button);

        // draws menuItem items
        for (int i = 0; i < 5; i++) {
            root.getChildren().add(getRectangle(x, y, true));
            x += offset;
        }

        drawLine();
        
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                // draws line under menuItem items
                root.getChildren().remove(line);
                drawLine();
            }
        });

        window.setTitle("Drawing a Rectangle");
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

    public Rectangle getRectangle(double x, double y, boolean menuItem) {
        Rectangle rectangle = new Rectangle();

        //Setting the properties of the rectangle
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(50.0f);
        rectangle.setHeight(50.0f);

        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.LIGHTGRAY);        
        
        if (menuItem) {
            System.out.printf("menuItem\n");
            rectangle = newItem(rectangle);
        } else {
            System.out.printf("setMovable\n");
            rectangle = setMovable(rectangle);
        }

        return rectangle;
    }

    public Rectangle newItem(Rectangle rectangle) {

        rectangle.setOnMousePressed(e -> {
            Rectangle rect = ((Rectangle)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY) {
                root.getChildren().add(getRectangle(rect.getX(), rect.getY()+100, false));
                System.out.printf("taham ven\n");
            }
        });
        return rectangle;
    }

    public Rectangle setMovable(Rectangle rectangle) {

        rectangle.setOnMouseDragged(e -> {
            Rectangle rect = ((Rectangle)(e.getSource()));
            double offsetX = e.getSceneX() - orgSceneX;
            double offsetY = e.getSceneY() - orgSceneY;

            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            double leftBorder = -rect.getX();
            double rightBorder = scene.getWidth() - rect.getWidth() - rect.getX();
            double upBorder = -rect.getY() + menuWidth;
            double downBorder = scene.getHeight() - rect.getHeight() - rect.getY();

            if (newTranslateX >= leftBorder && newTranslateX <= rightBorder) {
                rect.setTranslateX(newTranslateX);
            }
            if (newTranslateY >= upBorder && newTranslateY <= downBorder)
                rect.setTranslateY(newTranslateY);
        });

        // Sets new item coordinates
        rectangle.setOnMousePressed(e -> {
            Rectangle rect = ((Rectangle)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY) {
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = rect.getTranslateX();
                orgTranslateY = rect.getTranslateY();
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.printf("Udela port\n");
            }
        });

        return rectangle;

    }

}
