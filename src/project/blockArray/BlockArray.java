package project.blockArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.connection.Connection;
import project.items.*;

//TODO
public class BlockArray{
    private boolean first = true;
    private boolean found = false;
    private int size;
    private static final int DEFAULT_CAPACITY = 5;
    private static final Object[] empty_element_data = {};
    public List<Connection> connections;

    private transient Object[] blockArray;

    public BlockArray() {
        super();
        this.blockArray = empty_element_data;
        this.connections = new ArrayList<>();
    }

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

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = blockArray.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity + (oldCapacity / 2);
            if (newCapacity < minCapacity)
            newCapacity = minCapacity;
            blockArray = Arrays.copyOf(blockArray, newCapacity);
        }
    }

    public BlockArrayItem remove(int index) {
        BlockArrayItem item = (BlockArrayItem) blockArray[index];

        for (int i = index; i < this.size; i++) {
            blockArray[i] = blockArray[i + 1];
        }
        this.size--;
        return item;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            blockArray[i] = null;
        }
        size = 0;
        this.connections.clear();
    }

    private int index(String name) {
        for (int i = 0; i < size; i++) {
            if (get(i).item.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void checkForCycles() {
        for (int i = 0; i < connections.size(); i++) {
            AbstractItem in = connections.get(i).getInBlock();
            String name = in.getName();
            for (int j = 0; j < index(name); j++) {
                if (connections.get(i).getOutBlock().equals(get(j).item)) {
                    System.out.println("Cycle found. Exiting...");
                    System.exit(-1);
                }
            }
        }
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
                    System.out.println("Missing connection between blocks. Exiting...");
                    System.exit(-1);
                }
                found = false;
            }
    }

    public void run() {
        checkForCycles();
        checkIfMissing();

        for (int i = 0; i < size; i++) {
            get(i).item.execute();
            for (Connection connection : connections) {
                if (connection.getInBlock().equals(get(i).item)) {
                    connection.transferValue();
                }
            }
        }
    }
}
