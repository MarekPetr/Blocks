/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import project.blockArray.BlockArray;
import java.util.ListIterator;

/**
 * This class performs loading of already saved scheme.
 */
public class LoadScheme {
    private BlockArray toLoad;
    private AnchorPane right_pane;
    private RootLayout rootLayout;

    /**
     * Constructs LoadScheme.
     * @param toLoad loaded state
     * @param right_pane pane where scheme is loaded
     * @param rootLayout root layout containing whole stage
     */
    public LoadScheme(BlockArray toLoad, AnchorPane right_pane, RootLayout rootLayout) {
        this.toLoad = toLoad;
        this.right_pane = right_pane;
        this.rootLayout = rootLayout;
    }

    /**
     * Performs loading of a scheme.
     */
    public void load(){
        deleteNodes();
        spawnBlocks();
        toLoad.cleanVals();
        connectNodes();
    }

    /**
     * Adds blocks on scene.
     */
    private void spawnBlocks() {
        // print IDs ant types of all blocks
        DraggableNode node;
        for (int i = 0; i < toLoad.size(); i++) {
            DragIconType type = toLoad.get(i).getType();
            String id = toLoad.get(i).getName();
            System.out.println("Name: " + toLoad.get(i).getName() + ", type: " + toLoad.get(i).getType());

            node = rootLayout.addNode(type, id, false);

            double coordsX = toLoad.get(i).getX();
            double coordsY = toLoad.get(i).getY();

            node.setLayoutX(coordsX);
            node.setLayoutY(coordsY);
        }
    }

    /**
     * Connects spawned blocks.
     */
    private void connectNodes() {
        System.out.println("loading");
        String sourceId;
        String targetId;
        String connectId;


        for (int i = 0; i < toLoad.connections.size(); i++) {
            sourceId = toLoad.connections.get(i).getInBlock().getName();
            targetId = toLoad.connections.get(i).getOutBlock().getName();
            connectId = toLoad.connections.get(i).getId();

            NodeLink link = new NodeLink(rootLayout, sourceId, connectId);

            DraggableNode source = null;
            DraggableNode target = null;
            right_pane.getChildren().add(0,link);

            for (Node n : right_pane.getChildren()) {

                if (n.getId() == null)
                    continue;

                if (n.getId().equals(sourceId))
                    source = (DraggableNode) n;

                if (n.getId().equals(targetId))
                    target = (DraggableNode) n;
            }

            if (source != null && target != null && source != target) {
                System.out.println("Binding");
                link.bindEnds(source, target);
            } else {
                link.setVisible(false);
            }
        }
    }

    /**
     * Clears workspace
     */
    public void deleteNodes() {
        for (ListIterator<Node> iterNode = right_pane.getChildren().listIterator();
             iterNode.hasNext();)
        {
            Node node = iterNode.next();
            if (node.getId() == null)
                continue;
            iterNode.remove();
            node.setVisible(false);
        }

    }
}
