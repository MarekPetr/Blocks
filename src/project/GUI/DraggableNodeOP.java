package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.*;

public class DraggableNodeOP extends DraggableNode {
    @FXML private TextField value;


<<<<<<< HEAD
    public DraggableNodeOP() {
        super();
=======
    public DraggableNodeOP(RootLayout layout) {
        super(layout);
>>>>>>> Jakub-BlockArray
    }
    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeOP.fxml"));
    }

    @Override
    public void buildInputHandlers() {
        setTextField(value);
    }

    private void setTextField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        field.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                System.out.printf("operand value saved\n");
<<<<<<< HEAD
=======
                layout.blocks.get(getId()).item.setOperand(Double.parseDouble(field.getText()));
>>>>>>> Jakub-BlockArray
                this.requestFocus();
            }
        });
    }
}
