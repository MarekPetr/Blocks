package project.blockArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.connection.Connection;
import project.items.*;

//TODO
public class BlockArray implements Serializable {
    private boolean first = true;
    private boolean found = false;
    private int size;
    private static final int DEFAULT_CAPACITY = 5;
    private static final Object[] empty_element_data = {};

    public List<Connection> connections;

    private static BlockArrayItem current_state;

    private  Object[] blockArray;

    public BlockArray() {
        super();
        this.blockArray = empty_element_data;
        this.connections = new ArrayList<>();
        current_state = null;
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
                    return false;
                }
            }
        }
        return true;
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
        if (current_state == null) {
        } else if (current_state.con != null) {
            System.out.println("Current state is connection: " + current_state.con.getId());
        } else if (current_state.item != null) {
            System.out.println("Current state is item: " + current_state.item.getName());
        }
        if (current_state == null) {
            current_state = get(0);
            current_state.item.execute();
        } else if (current_state.item instanceof ItemLast) {
            System.out.println("DONE");
            current_state =  null;
        } else if (current_state.con != null) {
            current_state = get(current_state.con.getOutBlock().getName());
            current_state.item.execute();
        } else if (current_state.item != null) {
            String link = current_state.item.links.get(0);
            int index = indexC(link);
            current_state = new BlockArrayItem(connections.get(index));
            current_state.con.transferValue();
        }
    }
}
