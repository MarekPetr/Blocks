/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import project.items.ItemFirst;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents draggable block
 * of right pane used for input.
 */
public class DraggableNodeIN extends DraggableNode {
    @FXML private TextField key1;
    @FXML private TextField key2;
    @FXML private TextField key3;
    @FXML private TextField value1;
    @FXML private TextField value2;
    @FXML private TextField value3;
    @FXML private AnchorPane body_handle;

    public String current_key;
    public int current_index;
    public List<String> keys = new ArrayList<>(5);

    @FXML private Pane table_pane;

    /**
     * Constructs the DraggableNodeIN.
     * @param layout root layout
     * @param id ID of this future block
     */
    public DraggableNodeIN(RootLayout layout, String id) {
        super(layout, id);
    }

    /**
     * Sets resource file for current node.
     * @return FXMLLoader object
     */
    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeIN.fxml"));
    }

    /**
     * Builds handlers for input tables.
     */
    @Override
    public void buildInputHandlers() {
        if (layout.blocks.contains(ItemFirst.class)) {
            Map<String, Double> map = layout.blocks.get(0).getInValue();
            int i = 1;
            for (Map.Entry<String, Double> entry : map.entrySet())
            {
                String value = String.valueOf(entry.getValue());
                if (i == 1) {
                    key1.setText(entry.getKey());
                    value1.setText(value);
                } else if (i == 2) {
                    key2.setText(entry.getKey());
                    value2.setText(value);
                } else if (i == 3) {
                    key3.setText(entry.getKey());
                    value3.setText(value);
                }
                i++;
            }
        }

        setKeyField(key1, 1);
        setValueField(value1, 2);

        setKeyField(key2, 3);
        setValueField(value2, 4);

        setKeyField(key3, 5);
        setValueField(value3, 6);
    }

    /**
     * Builds middle handler of node - displaying input/output.
     */
    @Override
    public void buildBodyHandler() {
        table_pane.setVisible(false);
        body_handle.setOnMouseClicked(event -> {
            if (table_pane.isVisible()) {
                table_pane.setVisible(false);
            } else {
                table_pane.setVisible(true);
            }
        });
    }

    /**
     * Sets the key value of item corresponding to current node.
     * @param text_field reference to field where value is stored
     * @param index order of text field
     */
    private void setKeyField(TextField text_field, int index) {
        text_field.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                TextField input_field = (TextField) ke.getSource();
                if (keys.size() >= index && keys.get(index - 1) != null) {
                    layout.blocks.get(getId()).inValue.remove(current_key);
                    System.out.println("Removing key " + current_key);
                }
                keys.add(index - 1, input_field.getText());
                current_key = input_field.getText();
                current_index = index;

                System.out.printf("%d. saved\n", index);
                this.requestFocus();
            }
        });
    }

    /**
     * Sets the value of item corresponding to current node.
     * @param text_field reference to field where value is stored
     * @param index order of text field 
     */
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
                        layout.blocks.get(getId()).setInValue(current_key, value);
                    }
                }
                keys.add(index - 1, text_field.getText());
                System.out.printf("%d. saved\n", index);
            }
        });
    }
}

