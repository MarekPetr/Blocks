package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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
                current_key = text_field.getText();
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
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                double value = 0;
                boolean success = true;
                try {
                    value = Double.parseDouble(input_field.getText());
                } catch (NumberFormatException e) {
                    success = false;
                    // TODO PRIDAT VYPIS CHYBY NA OBRAZOVKU
                    System.err.println("Input is not a float value");
                }

                if (success) {
                    layout.blocks.get(getId()).item.setInValue(current_key, value);
                    System.out.printf("%d. saved\n", index);
                    this.requestFocus();
                }
            }
        });
    }
}

