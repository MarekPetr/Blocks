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

    public boolean add_to_list(BlockArrayItem e) {
        if (size == blockArray.length) {
            ensureCapacity(size + 1);
        }

        if (e.item instanceof ItemFirst) {
            add(0, e);
            return true;
        } else if (e.item instanceof ItemLast) {
            add(size, e);
            return true;
        } else if (e.con != null) {
            for(int i = 0; i < size; i++) {
                if(size != 0) {
                    if (get(i).item != null && get(i).item.getName().equals(e.con.getInBlock().getName())) {
                        add(i + 1, e);
                        return true;
                    } else if (get(i).item != null && get(i).item.getName().equals(e.con.getOutBlock().getName())) {
                        add(i, e);
                        return true;
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
                        return true;
                    } else if (get(i).con != null && get(i).con.getInBlock().getName().equals(e.item.getName())) {
                        add(i, e);
                        return true;
                    }
                } else {
                    add(e);
                }
            }
        }
        return true;
    }

    public boolean add(BlockArrayItem e) {
        ensureCapacity(size + 1);
        blockArray[size++] = e;
        return true;
    }

    public void add(int index, BlockArrayItem e) {
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
        if (blockArray == empty_element_data) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        if (minCapacity - blockArray.length > 0) {
            int oldCapacity = blockArray.length;
            int newCapacity = oldCapacity + (oldCapacity /2);
            if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
            blockArray = Arrays.copyOf(blockArray, newCapacity);
        }
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            blockArray[i] = null;
        }
        size = 0;
    }

    public void run() {
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
