package project.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by petr on 4/24/18.
 */
public class DraggableNodeOUT extends DraggableNode {
    @FXML private Label key1;
    @FXML private Label value1;
    @FXML private Label key2;
    @FXML private Label value2;
    @FXML private Label key3;
    @FXML private Label value3;
    @FXML private VBox table;


    public DraggableNodeOUT(RootLayout layout) {
        super(layout);
    }

    @Override
    public FXMLLoader setResource() {
        return new FXMLLoader(
                getClass().getResource("/DraggableNodeOUT.fxml"));
    }

    @Override
    public void buildInputHandlers() {
        table.setVisible(false);
        body_handle.setOnMouseClicked(event -> {
            //print values here
            Map<String, Double> map = layout.blocks.get(getId()).item.outValue;
            int i = 1;
            for (Map.Entry<String, Double> entry : map.entrySet())
            {
                System.out.println(entry.getKey() + "/" + entry.getValue());
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
            if (table.isVisible()) {
                table.setVisible(false);
            } else {
                table.setVisible(true);
            }
        });

    }
}

