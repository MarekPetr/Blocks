package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 4/24/18.
 */
public class DraggableNodeIN extends DraggableNode {
    @FXML private TextField key1;
    @FXML private TextField key2;
    @FXML private TextField key3;
    @FXML private TextField value1;
    @FXML private TextField value2;
    @FXML private TextField value3;

    private String current_key;
    private int current_index;
    private List<String> keys = new ArrayList<>(5);

    public DraggableNodeIN(RootLayout layout) {
        super(layout);
    }

    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeIN.fxml"));
    }

    @Override
    public void buildInputHandlers() {
        setKeyField(key1, 1);
        setValueField(value1, 2);

        setKeyField(key2, 3);
        setValueField(value2, 4);

        setKeyField(key3, 5);
        setValueField(value3, 6);
    }

    private void setKeyField(TextField text_field, int index) {
        text_field.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                if (keys.size() > index && keys.get(index - 1) != null) {
                    layout.blocks.get(getId()).item.inValue.remove(current_key, layout.blocks.get(getId()).item.inValue.get(current_key));
                    System.out.println("Removing key " + current_key);
                }
                keys.add(index - 1, text_field.getText());
                current_key = text_field.getText();
                current_index = index;

                System.out.printf("%d. saved\n", index);
                this.requestFocus();
            }
        });
    }

    private void setValueField(TextField text_field, int index) {
        text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d\\.*")) {
                text_field.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        text_field.setOnKeyPressed(ke -> {
            TextField input_field = (TextField) ke.getSource();
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if (!current_key.isEmpty()) {
                    if (current_index == index - 1) {
                        double value = get_double_input(input_field);
                        layout.blocks.get(getId()).item.setInValue(current_key, value);
                    }
                }
                keys.add(index - 1, text_field.getText());
                System.out.printf("%d. saved\n", index);
            }
        });
    }
}

