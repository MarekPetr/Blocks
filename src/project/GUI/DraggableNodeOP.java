package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class DraggableNodeOP extends DraggableNode {
    @FXML private TextField value;
    @FXML private VBox table;
    @FXML private AnchorPane body_handle;


    public DraggableNodeOP(RootLayout layout, String id) {
        super(layout, id);
    }

    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeOP.fxml"));
    }

    @Override
    public void buildInputHandlers() {
        String id = getNodeId();
        if (layout.blocks.itemExists(id)) {
            double val = layout.blocks.get(id).item.getOperand();
            System.out.println(val);
            value.setText(String.valueOf(val));
        }
        setTextField(value);
    }

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
                layout.blocks.get(getId()).item.setOperand(value);
                this.requestFocus();
            }
        });
    }

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
