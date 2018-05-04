/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * This class represents draggable block
 * of right pane used for operations.
 */
public class DraggableNodeOP extends DraggableNode {
    @FXML private TextField value;
    @FXML private VBox table;
    @FXML private AnchorPane body_handle;

    /**
     * Constructs the DraggableNodeOP.
     * @param layout root layout
     * @param id ID of this future block
     */
    public DraggableNodeOP(RootLayout layout, String id) {
        super(layout, id);
    }

    /**
     * Sets resource file for current node.
     * @return FXMLLoader object
     */
    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeOP.fxml"));
    }

    /**
     * Builds handlers for input tables.
     */
    @Override
    public void buildInputHandlers() {
        String id = getNodeId();
        if (layout.blocks.itemExists(id)) {
            double val = layout.blocks.get(id).getOperand();
            System.out.println(val);
            value.setText(String.valueOf(val));
        }
        setTextField(value);
    }

    /**
     * Sets the operand value of item corresponding to current node.
     * @param text_field reference to field where value is stored
     */
    private void setTextField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d.*")) {
                field.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        field.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                TextField input_field = (TextField) ke.getSource();
                double value = get_double_input(input_field);
                System.out.printf("operand value saved\n");
                layout.blocks.get(getId()).setOperand(value);
                this.requestFocus();
            }
        });
    }

    /**
     * Builds middle handler of node - displaying input/output.
     */
    @Override
    public void buildBodyHandler() {
        table.setVisible(false);
        body_handle.setOnMouseClicked(event -> {
            if (table.isVisible()) {
                table.setVisible(false);
            } else {
                table.setVisible(true);
            }
        });
    }
}
