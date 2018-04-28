package project.GUI;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import project.blockArray.BlockArray;
import project.blockArray.BlockArrayItem;
import project.connection.Connection;
import project.items.*;

import javax.annotation.processing.FilerException;
import java.io.*;
import java.util.UUID;

import static project.GUI.DragIconType.*;

/**
 * Created by petr on 4/21/18.
 */
public class RootLayout extends AnchorPane {
    private EventHandler mIconDragOverRoot=null;
    private EventHandler mIconDragDropped=null;
    private EventHandler mIconDragOverRightPane=null;

    private @FXML SplitPane base_pane;
    private @FXML Button run_button;
    private @FXML Button step_button;
    private @FXML Button load_button;
    private @FXML Button save_button;

    private @FXML AnchorPane right_pane;
    private @FXML VBox left_pane;

    //
    BlockArray blocks;
    Stage primaryStage;

    // semi transparent icon drawn on drag and drop
    private DragIcon mDragOverIcon = null;

    public RootLayout(Stage primaryStage) {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/RootLayout.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        blocks = new BlockArray();

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {

        run_button.setOnMouseClicked(event -> {
            System.out.println("Kliknul RUN");
            blocks.cleanVals();
            blocks.run();
        });

        step_button.setOnMouseClicked(event -> {
            System.out.println("Kliknul STEP");
            blocks.runStep();
        });

        save_button.setOnMouseClicked(event -> {
            FileOutputStream fos;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Scheme");
            File file = fileChooser.showSaveDialog(primaryStage);
            try {
                if (file != null) {
                    fos = new FileOutputStream(file);
                    ObjectOutput oos = new ObjectOutputStream(fos);
                    oos.writeObject(blocks);
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        load_button.setOnMouseClicked(event ->{
            BlockArray current_state = blocks;
            boolean load = false;
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    blocks = (BlockArray) ois.readObject();
                    ois.close();
                    load = true;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (load){
                LoadScheme loadScheme = new LoadScheme(blocks, right_pane, this);
                loadScheme.load();
            }

        });


        //Add one icon that will be used for the drag-drop processblue
        //This is added as a child to the root AnchorPane so it can begreen
        //visible on both sides of the split pane.green
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 6; i++) {

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
                    DraggableNode node;
                    DragIconType type = DragIconType.valueOf(container.getValue("type"));
                    node = addNode(type, UUID.randomUUID().toString(), true);

                    Point2D cursorPoint = container.getValue("scene_coords");

                    node.relocateToPoint(
                            new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                    );
                }
            }

            //AddLink drag operation
            container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

            if (container != null) {

                //bind the ends of our link to the nodes whose id's are stored in the drag container
                String sourceId = container.getValue("source");
                String targetId = container.getValue("target");

                if (sourceId != null && targetId != null) {
                    String linkID = UUID.randomUUID().toString();
                    NodeLink link = new NodeLink(this, sourceId, linkID);

                    //add our link at the top of the rendering order so it's rendered first
                    right_pane.getChildren().add(0,link);

                    DraggableNode source = null;
                    DraggableNode target = null;

                    for (Node n: right_pane.getChildren()) {

                        if (n.getId() == null)
                            continue;

                        if (n.getId().equals(sourceId))
                            source = (DraggableNode) n;

                        if (n.getId().equals(targetId))
                            target = (DraggableNode) n;

                    }

                    if (source != null && target != null && source != target) {
                        blocks.addToList(new BlockArrayItem(new Connection(link.getId(), blocks.get(sourceId).item, blocks.get(targetId).item)));
                        System.out.println("Connection between " + blocks.get(sourceId).item.getName() + " and " + blocks.get(targetId).item.getName() + " added.");
                        link.bindEnds(source, target);
                    }
                }
            }
            event.consume();
        });
    }

    public DraggableNode addNode(DragIconType type, String id, boolean toList) {
        DraggableNode node;
        if (type == in) {
            node = new DraggableNodeIN(this, id);
        } else if (type == out) {
            node = new DraggableNodeOUT(this, id);
        } else {
            node = new DraggableNodeOP(this, id);
        }
        node.setType(type);
        right_pane.getChildren().add(node);

        if (toList) {
            switch (node.getType()) {
                case in:
                    blocks.addToList(new BlockArrayItem(new ItemFirst(node.getId())));
                    System.out.println("ItemFirst added.");
                    break;
                case out:
                    blocks.addToList(new BlockArrayItem(new ItemLast(node.getId())));
                    System.out.println("ItemLast added.");
                    break;
                case plus:
                    blocks.addToList(new BlockArrayItem(new ItemPlus(node.getId())));
                    System.out.println("ItemPlus added.");
                    break;
                case minus:
                    blocks.addToList(new BlockArrayItem(new ItemMinus(node.getId())));
                    System.out.println("ItemMinus added.");
                    break;
                case mul:
                    blocks.addToList(new BlockArrayItem(new ItemMul(node.getId())));
                    System.out.println("ItemMul added.");
                    break;
                case div:
                    blocks.addToList(new BlockArrayItem(new ItemDiv(node.getId())));
                    System.out.println("ItemDiv added.");
                    break;
            }
            blocks.get(node.getId()).item.setType(node.getType());
        }
        return node;
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
