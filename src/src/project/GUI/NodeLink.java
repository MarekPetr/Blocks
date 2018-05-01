/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import java.io.IOException;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.CubicCurve;

/**
 * This class represents the link between draggable nodes.
 */
public class NodeLink extends AnchorPane {
    @FXML private CubicCurve node_link;
    @FXML private VBox table;
    @FXML private Label key1;
    @FXML private Label value1;
    @FXML private Label key2;
    @FXML private Label value2;
    @FXML private Label key3;
    @FXML private Label value3;
    @FXML private Pane table_pane;

    private RootLayout layout;
    private String sourceID;

    // these controls distance of the control points of curve from its end points
    private final DoubleProperty mControlOffsetX = new SimpleDoubleProperty();
    private final DoubleProperty mControlOffsetY = new SimpleDoubleProperty();

    // these controls directions of the control point from end point
    private final DoubleProperty mControlDirectionX1 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionY1 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionX2 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionY2 = new SimpleDoubleProperty();

    public NodeLink(RootLayout lay, String sourceId, String id) {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/NodeLink.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        layout = lay;
        sourceID = sourceId;
        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        setId(id);
    }
    public CubicCurve getLink() {
        return node_link;
    }

    @FXML
    private void initialize() {
        mControlOffsetX.set(100.0);
        mControlOffsetY.set(50.0);

        // change direction of the curve - multiply the offset by -1 or 1
        // depending whether its drawn left-to-right or right-to-left
        mControlDirectionX1.bind(new When (
                node_link.startXProperty().greaterThan(node_link.endXProperty()))
                .then(-1.0).otherwise(1.0));
        mControlDirectionX2.bind(new When (
                node_link.startXProperty().greaterThan(node_link.endXProperty()))
                .then(1.0).otherwise(-1.0));


        node_link.controlX1Property().bind(
                Bindings.add(
                        node_link.startXProperty(),
                        mControlOffsetX.multiply(mControlDirectionX1)
                )
        );
        node_link.controlX2Property().bind(
                Bindings.add(
                        node_link.endXProperty(),
                        mControlOffsetX.multiply(mControlDirectionX2)
                )
        );
        node_link.controlY1Property().bind(
                Bindings.add(
                        node_link.startYProperty(),
                        mControlOffsetY.multiply(mControlDirectionY1)
                )
        );
        node_link.controlY2Property().bind(
                Bindings.add(
                        node_link.endYProperty(),
                        mControlOffsetY.multiply(mControlDirectionY2)
                )
        );

        table_pane.setVisible(false);
        node_link.setOnMouseEntered(event ->  {
            //
            Map<String, Double> map = layout.blocks.get(sourceID).getOutValue();
            int i = 1;
            for (Map.Entry<String, Double> entry : map.entrySet())
            {
                String value = String.valueOf(entry.getValue());
                if (i == 1) {
                    key1.setText(entry.getKey());
                    value1.setText(value);
                } else if (i == 2) {
                    key2.setText(entry.getKey());
                    value2.setText(value);
                } else if (i == 3) {
                    key3.setText(entry.getKey());
                    value3.setText(value);
                }
                i++;
            }
            table_pane.setVisible(true);
        });
        node_link.setOnMouseExited(event -> {
            table_pane.setVisible(false);
        });
    }

    // set starting point of the curve
    // - has impact on its shape, nothing else
    public void setStart(Point2D startPoint) {

        node_link.setStartX(startPoint.getX());
        node_link.setStartY(startPoint.getY());
    }
    // set ending point of the curve
    // - has impact on its shape, nothing else
    public void setEnd(Point2D endPoint) {

        node_link.setEndX(endPoint.getX());
        node_link.setEndY(endPoint.getY());
    }
    public void bindEnds (DraggableNode source, DraggableNode target) {
        node_link.startXProperty().bind(
                Bindings.add(source.layoutXProperty(),(40)));

        node_link.startYProperty().bind(
                Bindings.add(source.layoutYProperty(), (40)));

        node_link.endXProperty().bind(
                Bindings.add(target.layoutXProperty(), (40)));

        node_link.endYProperty().bind(
                Bindings.add(target.layoutYProperty(), (40)));

        source.registerLink (getId());
        target.registerLink (getId());
    }
}