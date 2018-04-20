package project.GUI;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static project.GUI.ItemType.*;
import java.util.UUID;

/**
 * Created by petr on 4/17/18.
 */
public class UserInterface {
    private Stage window;
    private Scene scene;
    private Group root;

    private Line line;
    private double circRadius = 50;
    private double menuWidth = 1.8*circRadius;

    private Object sourceID;
    private Object targetID;

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
        CircleItem circle = new CircleItem(x, y, circRadius/2, type);

        circle.setStroke(Color.BLACK);
        if (circle.getType() == itemPlus) {
            circle.setFill(Color.LIGHTGRAY);
        } else if (circle.getType() == itemMinus) {
            circle.setFill(Color.RED);
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
                // TADY PRIDAT ITEM DO SEZNAMU
                circ.setId(UUID.randomUUID().toString()); // ID objektu
            }
        });
        return circle;
    }

    public CircleItem setMovable(CircleItem circle) {
        circle.setOnMouseDragged(e -> {
            CircleItem circ = ((CircleItem)(e.getSource()));
            System.out.printf("%s\n", circ.getId()); //ID objektu

            if (e.isPrimaryButtonDown()) {
                circ.toFront();
                if (e.getSceneX() >= circ.getRadius() && e.getSceneX() <= scene.getWidth()-circ.getRadius())
                    circ.setCenterX(e.getSceneX());
                if (e.getSceneY() >= circ.getRadius()+menuWidth && e.getSceneY() <= scene.getHeight()-circ.getRadius())
                    circ.setCenterY(e.getSceneY());
            } else if (e.isSecondaryButtonDown()) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    // TODO: Kontejner s ID itemu, ktere se spoji
                    CircleItem circle2 = new CircleItem(250,250, circRadius/2, itemPlus);
                    root.getChildren().add(circle2);

                    CircleItem circ2 = ((CircleItem)(e.getSource()));
                    Linking linking = new Linking(circ2, circle2);
                    linking.setStroke(Color.CYAN);
                    linking.setStrokeWidth(5);
                    root.getChildren().add(0, linking);
                }
            }
        });

        circle.setOnMousePressed((MouseEvent e) -> {
            CircleItem circ = ((CircleItem)(e.getSource()));
            if (e.getButton() == MouseButton.PRIMARY){
                scene.setOnKeyPressed(ke -> {
                    KeyCode keyCode = ke.getCode();
                    if (keyCode.equals(KeyCode.DELETE)) {
                        root.getChildren().remove(circ);
                    }
                });
            }
        });
        return circle;

    }
}
