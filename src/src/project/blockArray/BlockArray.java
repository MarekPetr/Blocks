/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
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

/**
 * This class represents internal implementation for creating schemes.
 */
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

    /**
     * Constructs the list.
     */
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

    /**
     * Cleans inValues and outValues for all AbstractItem elements in this list except ItemFirst instance's inValue.
     */
    public void cleanVals() {
        for (int i = 0; i < size(); i++) {
            get(i).outValue.clear();
            if (!(get(i) instanceof ItemFirst)) {
                get(i).inValue.clear();
            }
        }
    }

    /**
     * Returns the number of AbstractItem elements in this list.
     * @return the number of AbstractItem elements in this list
     */
    public int size() { return this.size; }

    /**
     * Adds the specified AbstractItem element to this list.
     * @param e AbstractItem element to be added
     */
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

    /**
     * Returns true if this list contains instance of the specified class.
     * More formally, returns true if and only if this list contains at least one instance of the class C.
     * @param C class whose instance presence in this list is to be tested
     * @return true if this list contains instance of the specified class
     */
    public boolean contains(Class C) {
        for (int i = 0; i < size; i++) {
            if (get(i).getClass().equals(C)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this list contains Connection between two specified AbstractItem instances.
     * @param in AbstractItem instance where Connection starts
     * @param out AbstractItem instance where Connection ends
     * @return true if this list contains specified Connection
     */
    public boolean containsConnection(AbstractItem in, AbstractItem out) {
        for (Connection connection : connections) {
            if (connection.getInBlock().equals(in) && connection.getOutBlock().equals(out)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this list contains Connection with the specified name.
     * @param id name of Connection whose presence is to be tested
     * @return true if this list contains specified Connection
     */
    public boolean containsConnectionByID(String id) {
        for (Connection connection : connections) {
            if (connection.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this list contains no AbstractItem elements.
     * @return true if this list contains no AbstractItem elements
     */
    private boolean isEmpty() {
        return size == 0;
    }

    /**
     * Appends the specified AbstractItem element to the end of this list.
     * @param e AbstractItem element to be appended to the list
     */
    private void add(AbstractItem e) {
        ensureCapacity(size + 1);
        blockArray[size++] = e;
    }

    /**
     * Inserts the specified AbstractItem element at the specified position in this list.
     * Shifts the element currently at that position (if any)
     * and any subsequent elements to the right (adds one to their indices).
     * @param index index at which the specified AbstractItem element is to be inserted
     * @param e AbstractItem element to be inserted to the list
     */
    private void add(int index, AbstractItem e) {
        ensureCapacity(size + 1);
        System.arraycopy(blockArray, index, blockArray, index + 1, size - index);
        blockArray[index] = e;
        size++;
    }

    /**
     * Returns the AbstractItem element at the specified position in this list.
     * @param index index of the AbstractItem element to return
     * @return the AbstractItem element at the specified position in this list
     */
    public AbstractItem get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound exception.");
        } else {
            return (AbstractItem) blockArray[index];
        }
    }

    /**
     * Returns the AbstractItem element with the specified name in this list.
     * @param name name of the AbstractItem element to return
     * @return the AbstractItem element with the specified name in this list
     */
    public AbstractItem get(String name) {
        for (int i = 0; i < size; i++) {
            if (get(i).getName().equals(name)) {
                return get(i);
            }
        }
        return null;
    }

    /**
     * Increases the capacity of this BlockArray instance,
     * if necessary, to ensure that it can hold at least
     * the number of elements specified by the minimum capacity argument.
     * @param minCapacity the desired minimum capacity
     */
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = blockArray.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity + (oldCapacity / 2);
            if (newCapacity < minCapacity)
            newCapacity = minCapacity;
            blockArray = Arrays.copyOf(blockArray, newCapacity);
        }
    }

    /**
     * Returns the index of the first occurrence of the specified Connection in this list.
     * Returns -1 if this list does not contain Connection with the specified name.
     * @param name name of Connection whose index to return
     * @return index of Connection with the specified name
     */
    private int indexC(String name) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getId().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes the AbstractItem element with the specified name in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Removes all connections coming in and out from this AbstractItem element.
     * @param name name of the AbstractItem element to be removed from this list
     */
    public void remove(String name) {
        removeConnections(name);
        List<Connection> to_remove = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getOutBlock().getName().equals(name)) {
                connections.get(i).getInBlock().links.remove(connections.get(i).getId());
                to_remove.add(connections.get(i));
            }
        }
        connections.removeAll(to_remove);
        //System.out.println("Item " + name + " is being removed.");
        int index = index(name);
        //
        if (get(index) instanceof ItemFirst && countInstances(ItemFirst.class) == 1) {
            this.first = true;
        }
        System.arraycopy(blockArray, index + 1, blockArray, index, size - index - 1);
        this.size--;
    }

    /**
     * Removes all connections coming out of the AbstractItem element with the specified name.
     * @param input_name name of the AbstractItem element where all Connections to be removed starts
     */
    private void removeConnections(String input_name) {
        List<Connection> to_remove = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getInBlock().getName().equals(input_name)) {
                to_remove.add(connections.get(i));
            }
        }
        connections.removeAll(to_remove);
    }

    /**
     * Removes connection connecting two AbstractItem elements specified by their names.
     * @param input_name name of the AbstractItem element where Connection to be removed starts
     * @param output_name name of the AbstractItem element where Connection to be removed ends
     */
    public void removeConnection(String input_name, String output_name) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getInBlock().getName().equals(input_name) && connections.get(i).getOutBlock().getName().equals(output_name)) {
                System.arraycopy(connections.toArray(), i + 1, connections.toArray(), i, connections.size() - i - 1);
            }
        }
    }

    /**
     * Removes all of the AbstractItem elements and Connections. The list will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            blockArray[i] = null;
        }
        size = 0;
        this.connections.clear();
    }

    /**
     * Returns the index of the first occurrence of the specified AbstractItem element in this list.
     * Returns -1 if this list does not contain AbstractItem element with the specified name.
     * @param name name of the AbstractItem element whose index to return
     * @return index of the AbstractItem element with the specified name
     */
    private int index(String name) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the first occurrence of instance of the specified Class in this list.
     * Returns -1 if this list does not contain any instance of the specified Class.
     * @param c Class whose instance's index to return
     * @return index of instance of the specified Class
     */
    private int index(Class c) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getClass().equals(c)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Return number of instances of the specified Class in this list.
     * @param c Class whose number of instances to return
     * @return number of instances of the specified Class
     */
    private int countInstances(Class c) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (get(i).getClass().equals(c)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if block scheme created from AbstractItem and Connection elements create cycle. 
     * This method is based on detecting cycles in a directed graph using colors. Uses Depth First Search method.
     * @return true if scheme contains cycle
     */
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

    /**
     * Auxiliary method used in cyclesExists method for detecting cycles.
     * @param i index of current AbstractItem element to be checked
     * @param whiteSet HashSet with indexes of AbstractItem elements which have not been processed yet
     * @param graySet HashSet with indexed of AbstractItem elements which are being processed
     * @param blackSet HashSet with indexed of AbstractItem elements which are already been processed
     */
    private boolean isCycleUtil(int i, HashSet<Integer> whiteSet, HashSet<Integer> graySet, HashSet<Integer> blackSet){
        whiteSet.remove(i);
        graySet.add(i);

        for (int j = 0; j < get(i).links.size() ; j++) {
            String id = get(i).links.get(j);
            int index = indexC(id);
            String name = connections.get(index).getOutBlock().getName();
            int adjI = index(name);

            if (graySet.contains(adjI))
                return true;

            if (blackSet.contains(adjI))
                continue;

            if (isCycleUtil(adjI, whiteSet, graySet, blackSet))
                return true;
        }
        graySet.remove(i);
        blackSet.add(i);
        return false;
    }

    /**
     * Runs the calculation for the created scheme based on type of blocks and values inserted.
     */
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

    /**
     * Return true if this list contains AbstractItem element with the specified name.
     * @param name name of AbstractItem element to be checked
     * @return true if list contains AbstractItem element with the specified name
     */
    public boolean itemExists(String name) {
        for (int i = 0; i < size; i++) {
            String current = get(i).getName();
            if (current.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if any of checks of constructed scheme failed.
     * @return true if any of checks failed
     */
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

    /**
     * Return true if set of Connections between instances of ItemFirst and ItemLast in this list exists.
     * @return true if set of Connections between ItemFist instance and ItemLast instance exists
     */
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

    /**
     * Runs one step of calculation for the created scheme based on type of blocks and values inserted.
     */
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

    /**
     * Updates right_pane variable reference to this instance
     * @param right_pane workspace pane
     */
    public void setRightPane(AnchorPane right_pane) {
        this.right_pane = right_pane;
    }

    /**
     * Highlight a block given by ID parameter. 
     * If there is a block already highlighted, delete its border (highlight).
     * @param id ID of block to highlight
     */
    private void highlightBlock(String id) {

        if (lastStepID != null) {
            setBlockBorder(lastStepID, true);
        }
        setBlockBorder(id, false);
    }

    /**
     * Finds node in workspace by it´s ID and set border (highlight) based on deleteBorder value.     
     * @param id ID of block to highlight
     * @param deleteBorder if true deletes border, otherwise creates border
     */
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

    /**
     * Returns ID of previously processed block.
     * @return ID of previously processed block
     */
    public String getLastStepID() {
        return this.lastStepID;
    }
}
