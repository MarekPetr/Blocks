package project.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

/**
 * Created by petr on 4/24/18.
 */
public class DraggableNodeOUT extends DraggableNode {
    private DragContainer container;

    @FXML private Label key1;
    @FXML private Label value1;
    @FXML private Label key2;
    @FXML private Label value2;
    @FXML private Label key3;
    @FXML private Label value3;


<<<<<<< HEAD
    public DraggableNodeOUT(DragContainer container) {
        super();
=======
    public DraggableNodeOUT(DragContainer container, RootLayout layout) {
        super(layout);
>>>>>>> Jakub-BlockArray
    }

    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeOUT.fxml"));
    }

    @Override
    public void buildInputHandlers() {
    }
}

