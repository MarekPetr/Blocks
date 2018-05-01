package project.blockArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import project.GUI.*;
import project.connection.Connection;
import project.items.*;

import static project.Errors.printErr;

public class BlockArray implements Serializable {
    private boolean first = true;
    private int size;
    private static final Object[] empty_element_data = {};

    private @FXML transient AnchorPane right_pane;
    private String lastStepID = null;

    public List<Connection> connections;
    public static int current_step_index;
    public static List<AbstractItem> current_step_items;
    public static List<AbstractItem> next_step_items;
    private static List<AbstractItem> run_items;
    private static List<AbstractItem> check_items;
    private  Object[] blockArray;


    private static final long serialVersionUID = 3787098173998467225L;

    public BlockArray() {
        super();
        this.blockArray = empty_element_data;
        this.connections = new ArrayList<>();
        current_step_index = 0;
        current_step_items = new ArrayList<>();
        next_step_items = new ArrayList<>();
        run_items = new ArrayList<>();
        check_items = new ArrayList<>();
    }

    public void cleanVals() {
        for (int i = 0; i < size(); i++) {
            get(i).outValue.clear();
            if (!(get(i) instanceof ItemFirst)) {
                get(i).inValue.clear();
            }
        }
    }

    public int size() { return this.size; }

    public void addToList(AbstractItem e) {
        if (size == blockArray.length) {
            ensureCapacity(size + 1);
        }
        if (e instanceof ItemFirst && this.first) {
            this.first = false;
            add(0, e);
        } else {
            add(e);
        }
    }

    public boolean contains(Class C) {
        for (int i = 0; i < size; i++) {
            if (get(i).getClass().equals(C)) {
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

    private void add(AbstractItem e) {
        ensureCapacity(size + 1);
        blockArray[size++] = e;
    }

    private void add(int index, AbstractItem e) {
        ensureCapacity(size + 1);
        System.arraycopy(blockArray, index, blockArray, index + 1, size - index);
        blockArray[index] = e;
        size++;
    }

    public AbstractItem get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound exception.");
        } else {
            return (AbstractItem) blockArray[index];
        }
    }


    public AbstractItem get(String name) {
        for (int i = 0; i < size; i++) {
            if (get(i).getName().equals(name)) {
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
        removeConnections(name);
        List<Connection> to_remove = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getOutBlock().getName().equals(name)) {
                System.out.println("Removing connection " + connections.get(i).getId() + " with input: " + connections.get(i).getInBlock().getName() + " and output: " + name);
                connections.get(i).getInBlock().links.remove(connections.get(i).getId());
                to_remove.add(connections.get(i));
            }
        }
        connections.removeAll(to_remove);
        System.out.println("Item " + name + " is being removed.");
        int index = index(name);
        //
        if (get(index) instanceof ItemFirst && countInstances(ItemFirst.class) == 1) {
            this.first = true;
        }
        System.arraycopy(blockArray, index + 1, blockArray, index, size - index - 1);
        this.size--;
    }

    private void removeConnections(String input_name) {
        List<Connection> to_remove = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getInBlock().getName().equals(input_name)) {
                System.out.println("Removing connection " + connections.get(i).getId() + " with input: " + input_name + " and output: " + connections.get(i).getOutBlock().getName());
                to_remove.add(connections.get(i));
            }
        }
        connections.removeAll(to_remove);
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
            if (get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private int index(Class c) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getClass().equals(c)) {
                return i;
            }
        }
        return -1;
    }

    private int countInstances(Class c) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (get(i).getClass().equals(c)) {
                count++;
            }
        }
        return count;
    }

    public boolean cyclesExists() {
        HashSet<Integer> whiteSet = new HashSet<>();
        HashSet<Integer> graySet = new HashSet<>();
        HashSet<Integer> blackSet = new HashSet<>();

        for (int i = 0; i < blockArray.length ; i++) {
            whiteSet.add(i);
        }
        for (int i = 0; i < size() ; i++) {
            if(whiteSet.contains(i) &&
                    isCycleUtil(i,whiteSet,graySet,blackSet)){
                return true;
            }
        }
        return false;
    }

    private boolean isCycleUtil(int vertex, HashSet<Integer> whiteSet, HashSet<Integer> graySet, HashSet<Integer> blackSet){
        whiteSet.remove(vertex);
        graySet.add(vertex);

        for (int i = 0; i < get(vertex).links.size() ; i++) {
            String id = get(vertex).links.get(i);
            int index = indexC(id);
            String name = connections.get(index).getOutBlock().getName();
            int adjVertex = index(name);

            if (graySet.contains(adjVertex))
                return true;

            if (blackSet.contains(adjVertex))
                continue;

            if (isCycleUtil(adjVertex, whiteSet, graySet, blackSet))
                return true;
        }
        graySet.remove(vertex);
        blackSet.add(vertex);
        return false;
    }

    public void run() {
        if (cyclesExists()) {
            printErr("Cycle found.");
            return;
        }

        if (check()) { return; }
        int index = index(ItemFirst.class);
        run_items.add(get(index));
        while (!(run_items.isEmpty())) {
            run_items.get(0).execute();
            for (Connection connection : connections) {
                if (connection.getInBlock().equals(run_items.get(0))) {
                    connection.transferValue();
                    run_items.add(connection.getOutBlock());
                }
            }
            run_items.remove(0);
        }
        run_items.clear();
    }
    public boolean itemExists(String name) {
        for (int i = 0; i < size; i++) {
            String current = get(i).getName();
            if (current.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean check() {
        if (!contains(ItemFirst.class)) {
            printErr("System does not contain IN block.");
            return true;
        }

        if (countInstances(ItemFirst.class) != 1) {
            printErr("System contains more than one IN block.");
            return true;
        }

        if (!check_con()) {
            printErr("No connection between IN and OUT block.");
            return true;
        }

        if (!contains(ItemLast.class)) {
            printErr("System does not contain OUT block.");
            return true;
        }

        int index = index(ItemFirst.class);
        if (get(index).inValue.isEmpty()) {
            printErr("Input value in IN block is null.");
            return true;
        }
        return false;
    }

    private boolean check_con() {
        int index = index(ItemFirst.class);
        check_items.add(this.get(index));
        while (!(check_items.isEmpty())) {
            for (Connection connection : connections) {
                if (connection.getInBlock().equals(check_items.get(0))) {
                    if (connection.getOutBlock() instanceof ItemLast && connection.getOutBlock().links.isEmpty()) {
                        check_items.clear();
                        return true;
                    }
                    check_items.add(connection.getOutBlock());
                }
            }
            check_items.remove(0);
        }
        check_items.clear();
        return false;
    }

    public void runStep() {
        int index = index(ItemFirst.class);
        if (current_step_index == 0) {
            current_step_items.clear();
            cleanVals();
            get(index).execute();
            highlightBlock(get(index).getName());
            for (Connection connection : connections) {
                if (connection.getInBlock().equals(get(index))) {
                    connection.transferValue();
                    next_step_items.add(connection.getOutBlock());
                }
            }
            current_step_index++;
        } else {
            setBlockBorder(get(index).getName(), true);
            for (AbstractItem current_step_item : current_step_items) {
                setBlockBorder(current_step_item.getName(), true);
            }
            current_step_items = new ArrayList<>(next_step_items);
            for (AbstractItem current_step_item : current_step_items) {
                setBlockBorder(current_step_item.getName(), false);
            }
            next_step_items.clear();
            if (current_step_items.isEmpty()) {
                current_step_items.clear();
                setBlockBorder(lastStepID, true);
                next_step_items.clear();
                current_step_index = 0;
                return;
            }
            int items_size = current_step_items.size();
            for (int i = 0; i < items_size; i++) {
                current_step_items.get(i).execute();
                for (Connection connection : connections) {
                    if (connection.getInBlock().equals(current_step_items.get(i))) {
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
            setBlockBorder(lastStepID, true);
        }
        setBlockBorder(id, false);
    }

    public void setBlockBorder(String id, boolean deleteBorder) {
        VBox block;
        if (right_pane == null)
            return;

        for (Node n: right_pane.getChildren()) {
            if (n.getId() == null)
                continue;
            if (n instanceof DraggableNodeIN || n instanceof DraggableNodeOP ||
                    n instanceof DraggableNodeOUT)
            {
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

    public String getLastStepID() {
        return this.lastStepID;
    }
}
