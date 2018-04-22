package project.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

public class DraggableNode extends AnchorPane {

    @FXML AnchorPane root_pane;

    private EventHandler mContextDragOver;
    private EventHandler  mContextDragDropped;

    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);

    @FXML private Label title_bar;
    @FXML private Label close_button;

    private final DraggableNode self;

    @FXML AnchorPane left_link_handle;
    @FXML AnchorPane right_link_handle;

    // reusable object during drag and drop operations
    // every node will have its own
    private NodeLink mDragLink = null;
    // reference to the right_pane - link can occur only there
    private AnchorPane right_pane = null;

    private EventHandler <MouseEvent> mLinkHandleDragDetected;
    private EventHandler <DragEvent> mLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;
    private EventHandler <DragEvent> mContextLinkDragDropped;

    // list of IDs of currently connected links
    private final List mLinkIds = new ArrayList();

    public DraggableNode() {
        // dragging has to be handled in root Anchor - referenced by 'this'
        self = this;
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DraggableNode.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        setId(UUID.randomUUID().toString());
    }

    @FXML
    private void initialize() {
        buildNodeDragHandlers();
        buildLinkDragHandlers();

        left_link_handle.setOnDragDetected(mLinkHandleDragDetected);
        right_link_handle.setOnDragDetected(mLinkHandleDragDetected);

        left_link_handle.setOnDragDropped(mLinkHandleDragDropped);
        right_link_handle.setOnDragDropped(mLinkHandleDragDropped);

        mDragLink = new NodeLink();
        mDragLink.setVisible(false);

        parentProperty().addListener((observable, oldValue, newValue)
                -> right_pane = (AnchorPane) getParent());
    }

    public void buildNodeDragHandlers() {
        //drag detection (on title bar) for node dragging
        title_bar.setOnDragDetected (event -> {

            // parent is the root AnchorPane
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            getParent().setOnDragOver (mContextDragOver);
            getParent().setOnDragDropped (mContextDragDropped);

            //begin drag ops
            mDragOffset = new Point2D(event.getX(), event.getY());
            // coordinates relative to upper left corner of a node
            relocateToPoint (new Point2D(event.getSceneX(), event.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer();

            container.addData ("type", mType.toString());
            content.put(DragContainer.DragNode, container);

            startDragAndDrop (TransferMode.ANY).setContent(content);

            event.consume();
        });

        //dragover to handle node dragging in the right pane view
        mContextDragOver = (EventHandler<DragEvent>) event -> {
            event.acceptTransferModes(TransferMode.ANY);
            relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

            event.consume();
        };

        //dragdrop for node dragging
        mContextDragDropped = (EventHandler<DragEvent>) event -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            event.setDropCompleted(true);
            event.consume();
        };

        //close button click
        //close button click
        close_button.setOnMouseClicked(event -> {
            AnchorPane parent  = (AnchorPane) self.getParent();
            parent.getChildren().remove(self);

            //iterate each link id connected to this node
            //find it's corresponding component in the right-hand
            //AnchorPane and delete it.

            //Note:  other nodes connected to these links are not being
            //notified that the link has been removed.
            for (ListIterator<String> iterId = mLinkIds.listIterator();
                 iterId.hasNext();) {

                String id = iterId.next();

                for (ListIterator <Node> iterNode = parent.getChildren().listIterator();
                     iterNode.hasNext();) {

                    Node node = iterNode.next();

                    if (node.getId() == null)
                        continue;

                    if (node.getId().equals(id))
                        iterNode.remove();
                }

                iterId.remove();
            }
        });
    }

    private void buildLinkDragHandlers() {
        mLinkHandleDragDetected = event -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            getParent().setOnDragOver(mContextLinkDragOver);
            getParent().setOnDragDropped(mLinkHandleDragDropped);

            //Set up user-draggable link
            // index zero means, that the curve is rendered before nodes
            // - to prevent being drawn over them
            right_pane.getChildren().add(0,mDragLink);
            mDragLink.setVisible(false);
            // get the position of the center of node, where drag operation began
            Point2D p = new Point2D(
                    getLayoutX() + (getWidth() / 2.0),
                    getLayoutY() + (getHeight() / 2.0)
            );
            mDragLink.setStart(p);

            //Drag content code
            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer ();

            AnchorPane link_handle = (AnchorPane) event.getSource();
            container.addData("source", getId()); //pass type of node to DragBoard
            content.put(DragContainer.AddLink, container); // pass DrabBoard container to ClipBoard content

            startDragAndDrop (TransferMode.ANY).setContent(content);

            event.consume();
        };

        // handles successful drop events
        mLinkHandleDragDropped = event -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            //get the drag data.  If it's null, abort.
            //This isn't the drag event we're looking for.
            DragContainer container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

            if (container == null)
                return;

            //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
            mDragLink.setVisible(false);
            right_pane.getChildren().remove(0);

            ClipboardContent content = new ClipboardContent();
            container.addData("target", getId());
            content.put(DragContainer.AddLink, container);
            event.getDragboard().setContent(content);

            event.setDropCompleted(true);
            event.consume();
        };

        mContextLinkDragOver = event -> {
            // this call must occur for successful drag operation
            event.acceptTransferModes(TransferMode.ANY);

            //Relocate user-draggable link to follow mouse cursor
            if (!mDragLink.isVisible())
                mDragLink.setVisible(true);
            mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

            event.consume();
        };

        // handles failed dropped events - out of node side handles
        mContextLinkDragDropped = event -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
            mDragLink.setVisible(false);
            right_pane.getChildren().remove(0);

            event.setDropCompleted(true);
            event.consume();
        };
    }

    public void registerLink(String linkId) {
        mLinkIds.add(linkId);
    }

    public void relocateToPoint (Point2D p) {
        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        // mDragOffset - offsets the mouse coordinates,
        // so that user can drag the item with label
        // and item wont center to mouse cursor
        relocate (
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }

    public DragIconType getType () { return mType; }

    public void setType (DragIconType type) {

        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");

        switch (mType) {

            case blue:
                getStyleClass().add("icon-blue");
                break;

            case red:
                getStyleClass().add("icon-red");
                break;

            case green:
                getStyleClass().add("icon-green");
                break;

            case grey:
                getStyleClass().add("icon-grey");
                break;

            case purple:
                getStyleClass().add("icon-purple");
                break;

            case yellow:
                getStyleClass().add("icon-yellow");
                break;

            case black:
                getStyleClass().add("icon-black");
                break;

            default:
                break;
        }
    }
}
