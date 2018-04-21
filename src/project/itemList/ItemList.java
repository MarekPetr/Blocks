package project.itemList;


import project.items.AbstractItem;
import project.items.ItemFirst;
import project.items.ItemLast;

import java.util.Arrays;

public class ItemList {
    private int size;
    private static final int DEFAULT_CAPACITY = 5;
    private static final Object[] empty_element_data = {};

    private transient Object[] itemList;

    public ItemList() {
        super();
        this.itemList = empty_element_data;
    }
    
    public void addToList(AbstractItem it) {
        if (size == itemList.length) {
            ensureCapacity(size + 1);
        }

        if (it instanceof ItemFirst) {
            add(0, it);
        } else if (it instanceof ItemLast) {
            add(size, it);
        } else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < get(i).links.size(); j++) {
                    if (get(i).links.get(j).getOutBlock().equals(it)) {
                        add(i + 1, it);
                    } else {
                        add(it);
                    }
                }
            }
        }
    }

    private void add(AbstractItem it) {
        ensureCapacity(size + 1);
        itemList[size++] = it;
    }

    private void add(int index, AbstractItem it) {
            ensureCapacity(size + 1);
        System.arraycopy(itemList, index, itemList, index + 1, size - index);
        itemList[index] = it;
        size++;
    }

    public AbstractItem get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound exception.");
        } else {
            return (AbstractItem) itemList[index];
        }
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = itemList.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity + (oldCapacity / 2);
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            itemList = Arrays.copyOf(itemList, newCapacity);
        }
    }

    public AbstractItem remove(int index) {
        AbstractItem item = (AbstractItem) itemList[index];

        System.arraycopy(itemList, index, itemList, index - 1, size - index);
        this.size--;
        return item;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            itemList[i] = null;
        }
        size = 0;
    }

    private void lookForCycles() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < get(i).links.size(); j++) {
                AbstractItem check = get(i).links.get(j).getOutBlock();
                for (int k = 0; k < i; k++) {
                    if (check.equals(get(k))) {
                        System.out.println("Cycle found! Unable to proceed. Exiting...");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    public void run() {
        lookForCycles();

        if (!(get(0) instanceof ItemFirst) || !(get(size - 1) instanceof ItemLast)) {
            System.out.println("First or last item in the list does not match requirements. Exiting...");
            System.exit(-1);
        }

        for (int i = 0; i < size; i++) {
            get(i).execute();
            for (int j = 0; j < get(i).links.size(); j++) {
                get(i).links.get(j).transferValue();
            }
        }
    }
}
