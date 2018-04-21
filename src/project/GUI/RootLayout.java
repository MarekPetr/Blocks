package project.GUI;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by petr on 4/21/18.
 */
public class RootLayout extends AnchorPane {
    private EventHandler mIconDragOverRoot=null;
    private EventHandler mIconDragDropped=null;
    private EventHandler mIconDragOverRightPane=null;

    private @FXML SplitPane base_pane;
    private @FXML AnchorPane right_pane;
    private @FXML VBox left_pane;

    // semi transparent icon drawn on drag and drop
    private DragIcon mDragOverIcon = null;

    public RootLayout() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/RootLayout.fxml")
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
    private void initialize() {

        //Add one icon that will be used for the drag-drop processblue
        //This is added as a child to the root AnchorPane so it can begreen
        //visible on both sides of the split pane.green
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 7; i++) {

            DragIcon icn = new DragIcon();
            addDragDetection(icn);

            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
        }
        buildDragHandlers();
    }

    private void buildDragHandlers() {
        //drag over transition to move widget form left pane to right pane
        mIconDragOverRoot = (EventHandler<DragEvent>) event -> {
            Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

            if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                return;
            }
            event.consume();
        };

        mIconDragOverRightPane = (EventHandler<DragEvent>) event -> {
            event.acceptTransferModes(TransferMode.ANY);
            mDragOverIcon.relocateToPoint(
                    new Point2D(event.getSceneX(), event.getSceneY())
            );
            event.consume();
        };

        mIconDragDropped = (EventHandler<DragEvent>) event -> {
            // retrieve container
            DragContainer
                    container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

            // updates scene_cords with cordinates of current mouse cursor
            container.addData("scene_coords",
                    new Point2D(event.getSceneX(), event.getSceneY()));

            // creates new container for storing node info
            ClipboardContent content = new ClipboardContent();
            content.put(DragContainer.AddNode, container);

            // updates DragBoard's content with Clipboard object
            // containing updated DragContainer data
            event.getDragboard().setContent(content);
            event.setDropCompleted(true);
        };

        // this - refers to root AnchorPane layout
        // User can drop the icon even in left_pane to stop dragging operation
        // This handler controls both successful and failed tries to drag and drop icon
        this.setOnDragDone (event -> {

            right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

            mDragOverIcon.setVisible(false);

            DragContainer container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

            if (container != null) {
                if (container.getValue("scene_coords") != null) {

                    // add Node operation
                    DraggableNode node = new DraggableNode();
                    node.setType(DragIconType.valueOf(container.getValue("type")));
                    right_pane.getChildren().add(node);

                    Point2D cursorPoint = container.getValue("scene_coords");

                    node.relocateToPoint(
                            new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                    );
                }
            }

            //AddLink drag operation
            container = (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

            if (container != null) {
                System.out.println(container.getData());
            }

            event.consume();
        });
    }

    private void addDragDetection(DragIcon dragIcon) {

        dragIcon.setOnDragDetected (event -> {
            // set the other drag event handles on their respective objects
            base_pane.setOnDragOver(mIconDragOverRoot);
            right_pane.setOnDragOver(mIconDragOverRightPane);
            right_pane.setOnDragDropped(mIconDragDropped);

            // get a reference to the clicked DragIcon object
            DragIcon icn = (DragIcon) event.getSource();

            //begin drag ops
            mDragOverIcon.setType(icn.getType());
            mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

            // drag content
            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer();

            container.addData ("type", mDragOverIcon.getType().toString());
            content.put(DragContainer.AddNode, container);

            mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
            mDragOverIcon.setVisible(true);
            mDragOverIcon.setMouseTransparent(true);
            event.consume();
        });
    }
}
