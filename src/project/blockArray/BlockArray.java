package project.blockArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import project.GUI.*;
import project.connection.Connection;
import project.items.*;

//TODO
public class BlockArray implements Serializable {
    private boolean first = true;
    private boolean found = false;
    private int size;
    private static final int DEFAULT_CAPACITY = 5;
    private static final Object[] empty_element_data = {};

    private @FXML transient AnchorPane right_pane;
    private String lastStepID = null;

    public List<Connection> connections;
    public static int current_step_index;
    public static List<AbstractItem> current_step_items;
    public static List<AbstractItem> next_step_items;
    private  Object[] blockArray;


    private static final long serialVersionUID = 3787098173998467225L;

    public BlockArray() {
        super();
        this.blockArray = empty_element_data;
        this.connections = new ArrayList<>();
        current_step_index = 0;
        current_step_items = new ArrayList<>();
        next_step_items = new ArrayList<>();
    }

    public void cleanVals() {
        for (int i = 0; i < size(); i++) {
            get(i).item.outValue.clear();
            if (!(get(i).item instanceof ItemFirst)) {
                get(i).item.inValue.clear();
            }
        }
    }

    public int size() { return this.size; }

    public void addToList(BlockArrayItem e) {
        if (size == blockArray.length) {
            ensureCapacity(size + 1);
        }
        if (e.con != null) {
            connections.add(e.con);
        } else {
            if (e.item instanceof ItemFirst && this.first) {
                this.first = false;
                add(0, e);
            } else if (e.item instanceof ItemLast) {
                add(e);
            } else {
                if (isEmpty()) {
                    add(e);
                } else if (contains(ItemLast.class)) {
                    add(this.size - 1, e);
                } else {
                    add(this.size, e);
                }
            }
        }
    }

    private boolean contains(Class C) {
        for (int i = 0; i < size; i++) {
            if (get(i).item.getClass().equals(C)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsConnection(AbstractItem in, AbstractItem out) {
        for (Connection connection : connections) {
            if (connection.getInBlock().equals(in) && connection.getOutBlock().equals(out)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsConnectionByID(String id) {
        for (Connection connection : connections) {
            if (connection.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private void add(BlockArrayItem e) {
        ensureCapacity(size + 1);
        blockArray[size++] = e;
    }

    private void add(int index, BlockArrayItem e) {
        ensureCapacity(size + 1);
        System.arraycopy(blockArray, index, blockArray, index + 1, size - index);
        blockArray[index] = e;
        size++;
    }

    public BlockArrayItem get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound exception.");
        } else {
            return (BlockArrayItem) blockArray[index];
        }
    }

    public BlockArrayItem get(String name) {
        for (int i = 0; i < size; i++) {
            if (get(i).item.getName().equals(name)) {
                return get(i);
            }
        }
        return null;
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = blockArray.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity + (oldCapacity / 2);
            if (newCapacity < minCapacity)
            newCapacity = minCapacity;
            blockArray = Arrays.copyOf(blockArray, newCapacity);
        }
    }

    private int indexC(String name) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getId().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void remove(String name) {
        System.out.println("Item " + name + " is being removed.");
        int index = index(name);
        System.arraycopy(blockArray, index + 1, blockArray, index, size - index - 1);
        this.size--;
        removeConnections(name);
    }

    private void removeConnections(String input_name) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getInBlock().getName().equals(input_name)) {
                System.out.println("Removing connection " + connections.get(i).getId() + " with input: " + input_name + " and output: " + connections.get(i).getOutBlock().getName());
                System.arraycopy(connections.toArray(), i + 1, connections.toArray(), i, connections.size() - i - 1);
            }
        }
    }

    public void removeConnection(String input_name, String output_name) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getInBlock().getName().equals(input_name) && connections.get(i).getOutBlock().getName().equals(output_name)) {
                System.out.println("Removing connection " + connections.get(i).getId() + " with input: " + input_name + " and output: " + output_name);
                System.arraycopy(connections.toArray(), i + 1, connections.toArray(), i, connections.size() - i - 1);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            blockArray[i] = null;
        }
        size = 0;
        this.connections.clear();
    }

    private int index(String name) {
        for (int i = 0; i < size(); i++) {
            if (get(i).item.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public boolean cyclesExists() {
        for (int i = 0; i < connections.size(); i++) {
            AbstractItem in = connections.get(i).getInBlock();
            String name = in.getName();
            for (int j = 0; j < index(name); j++) {
                if (connections.get(i).getOutBlock().equals(get(j).item)) {
                    System.out.println("ERROR: Cycle found.");
                    return true;
                }
            }
        }
        return false;
    }

    private void checkIfMissing() {
        for (int i = 0; i < size; i++)
            if (!(get(i).item instanceof ItemFirst)) {
                for (Connection connection : connections) {
                    if (connection.getOutBlock().equals(get(i).item)) {
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Missing connection between blocks.");
                    System.exit(-1);
                }
                found = false;
            }
    }

    public void run() {
        if (cyclesExists()) {
            return;
        }
        
        //checkIfMissing();
        if (!contains(ItemFirst.class)) {
            System.out.println("ERROR: System does not contain block of type ItemFist.");
            return;
        }

        if (!contains(ItemLast.class)) {
            System.out.println("ERROR: System does not contain block of type ItemLast.");
            return;
        }

        if (get(0).item.inValue.isEmpty()) {
            System.out.println("ERROR: In value is null.");
            return;
        }

        for (int i = 0; i < size; i++) {
            if (get(i).item.links != null) {
                get(i).item.execute();
                for (Connection connection : connections) {
                    if (connection.getInBlock().equals(get(i).item)) {
                        connection.transferValue();
                    }
                }
            }
        }
    }

    public void runStep() {
        if (current_step_index == 0) {
            current_step_items.clear();
            cleanVals();
            get(0).item.execute();
            highlightBlock(get(0).item.getName());
            for (Connection connection : connections) {
                if (connection.getInBlock().equals(get(0).item)) {
                    connection.transferValue();
                    next_step_items.add(connection.getOutBlock());
                }
            }
            current_step_index++;
        } else {
            current_step_items = new ArrayList<>(next_step_items);
            next_step_items.clear();
            if (current_step_items.isEmpty()) {
                current_step_items.clear();
                setBlockBorder(lastStepID, true);
                next_step_items.clear();
                current_step_index = 0;
                return;
            }
            int items_size = current_step_items.size();
            for (AbstractItem current_step_item : current_step_items) {
                current_step_item.execute();
                highlightBlock(current_step_item.getName());
                for (Connection connection : connections) {
                    if (connection.getInBlock().equals(current_step_item)) {
                        highlightBlock(current_step_item.getName());
                        connection.transferValue();
                        next_step_items.add(connection.getOutBlock());
                    }
                }
            }
        }
    }

    public void setRightPane(AnchorPane right_pane) {
        this.right_pane = right_pane;
    }

    private void highlightBlock(String id) {

        if (lastStepID != null) {
            System.out.println("lastStep" + lastStepID);
            setBlockBorder(lastStepID, true);
        }
        setBlockBorder(id, false);
    }

    private void setBlockBorder(String id, boolean deleteBorder) {

        System.out.println("id " + id);
        VBox block;
        if (right_pane == null)
            return;

        for (Node n: right_pane.getChildren()) {
            if (n.getId() == null)
                continue;
            if (n instanceof DraggableNodeIN || n instanceof DraggableNodeOP ||
                    n instanceof DraggableNodeOUT) {

                block = ((DraggableNode) n).getBlock();

                if (n.getId().equals(id)) {
                    block.setBorder(null);
                    if (!deleteBorder) {
                        lastStepID = id;
                        block.setBorder(new Border(
                                new BorderStroke(Color.CHARTREUSE, BorderStrokeStyle.SOLID, null, new BorderWidths(4))));
                    }
                }
            }
        }
    }
}
