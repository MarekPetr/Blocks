/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

/**
 * This class represents icon used for dragging to right pane,
 * which after dropping creates a draggable node.
 */
public class DragIcon extends AnchorPane{

    @FXML AnchorPane root_pane;

    private DragIconType mType = null;

    public DragIcon() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DragIcon.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {}

    public void relocateToPoint (Point2D p) {

        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
                (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
                (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
        );
    }

    public DragIconType getType () { return mType; }

    public void setType (DragIconType type) {

        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");

        switch (mType) {

            case out:
                getStyleClass().add("icon-out");
                break;

            case in:
                getStyleClass().add("icon-in");
                break;

            case plus:
                getStyleClass().add("icon-plus");
                break;

            case minus:
                getStyleClass().add("icon-minus");
                break;

            case div:
                getStyleClass().add("icon-div");
                break;

            case mul:
                getStyleClass().add("icon-mul");
                break;

            case pow:
                getStyleClass().add("icon-pow");
                break;

            default:
                break;
        }
    }
}
