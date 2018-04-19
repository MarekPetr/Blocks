package project.blockArray;

import java.util.Arrays;
import project.items.*;


public class BlockArray{
    private int size;
    private static final int DEFAULT_CAPACITY = 5;
    private static final Object[] empty_element_data = {};

    private transient Object[] blockArray;

    public BlockArray() {
        super();
        this.blockArray = empty_element_data;
    }

    public boolean addToList(BlockArrayItem e) {
        if (size == blockArray.length) {
            ensureCapacity(size + 1);
        }

        if (e.item instanceof ItemFirst) {
            add(0, e);
        } else if (e.item instanceof ItemLast) {
            add(size, e);
        } else if (e.con != null) {
            for(int i = 0; i < size; i++) {
                if(size != 0) {
                    if (get(i).item != null && get(i).item.getName().equals(e.con.getInBlock().getName())) {
                        add(i + 1, e);
                    } else if (get(i).item != null && get(i).item.getName().equals(e.con.getOutBlock().getName())) {
                        add(i, e);
                    }
                } else {
                    add(e);
                }
            }
        } else if(e.item != null) {
            for(int i = 0; i < size; i++) {
                if(size != 0) {
                    if (get(i).con != null && get(i).con.getOutBlock().getName().equals(e.item.getName())) {
                        add(i + 1, e);
                    } else if (get(i).con != null && get(i).con.getInBlock().getName().equals(e.item.getName())) {
                        add(i, e);
                    }
                } else {
                    add(e);
                }
            }
        }
        return true;
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
    }

    private void checkForCycles() {
        for (int i = 0; i < size; i++) {
            if (get(i).con != null) {
                AbstractItem check = get(i).con.getOutBlock();
                for (int j = 0; j < i; j++) {
                    if (get(j).item != null && check.equals(get(j).item)) {
                        System.out.println("Cycle found, exiting");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    public boolean contains(AbstractItem item) {
        return indexOf(item) >= 0;
    }

    public int indexOf(AbstractItem item) {
        if (item == null) {
            for (int i = 0; i < size; i++) {
                if (get(i).item == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (get(i).item != null && item.equals(get(i).item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean containsConnection(AbstractItem it, int option) {
        return indexOfCon(it, option) >= 0;
    }

    public int indexOfCon(AbstractItem it, int opt) {
        for (int i = 0; i < size; i++) {
            if (get(i).con != null) {
                switch (opt) {
                    case 1:
                        if (it.equals(get(i).con.getInBlock())) { return i; }
                        break;
                    case 2:
                        if (it.equals(get(i).con.getOutBlock())) { return i; }
                        break;
                    default:
                        System.out.println("Invalid option, exiting");
                        System.exit(-1);
                }
            }
        }
        return -1;
    }

    private void checkIfMissing() {
        for (int i = 0; i < size; i++) {
            if (get(i).con != null) {
                if (!(contains(get(i).con.getInBlock()) && contains(get(i).con.getOutBlock()))) {
                    System.out.println("BlockArray is missing item, exiting");
                    System.exit(-1);
                }
            }
            if (get(i).item != null) {
                if (get(i).item instanceof ItemFirst) {
                    if (!(containsConnection(get(i).item, 1))) {
                        System.out.println("BlockArray is missing connection, exiting");
                        System.exit(-1);
                    }
                } else if (get(i).item instanceof ItemLast) {
                    if (!(containsConnection(get(i).item, 2))) {
                        System.out.println("BlockArray is missing connection, exiting");
                        System.exit(-1);
                    }
                } else {
                    if (!(containsConnection(get(i).item, 1)) || !(containsConnection(get(i).item, 2))) {
                        System.out.println("BlockArray is missing connection, exiting");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    public void run() {
        checkIfMissing();
        checkForCycles();

        if (get(0).con != null || !(get(0).item instanceof ItemFirst)) {
            System.out.println("First item in list has to be instance of ItemFirst, exiting");
            System.exit(-1);
        }
        if (get(size - 1).con != null || !(get(size - 1).item instanceof ItemLast)) {
            System.out.println("Last item in list has to be instance of ItemLast, exiting");
            System.exit(-1);
        }
        for(int i = 0; i < size; i++) {
            if(get(i).item != null) {
                get(i).item.execute();
            }
            if(get(i).con != null) {
                get(i).con.transferValue();
            }
        }
    }
}
