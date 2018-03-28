package project;
import project.items.AbstractItem;
import project.items.ItemMinus;
import project.items.ItemPlus;
import project.repository.Repository;

/**
 * Created by petr on 3/28/18.
 */
public class Blocks {
    public static void main(final String[] args) {
        double resValue = 0;

        AbstractItem item1, item2;
        item1 = new ItemPlus("first", 2, 4);
        System.out.printf("1jmeno: %s, stav:%f\n", item1.getName(), item1.getState());
        item1.execute();
        System.out.printf("1jmeno: %s, stav:%f\n\n", item1.getName(), item1.getState());

        item2 = new ItemMinus("second", 2, 3);
        System.out.printf("2jmeno: %s, stav:%f\n", item2.getName(), item2.getState());
        item2.execute();
        System.out.printf("2jmeno: %s, stav:%f\n", item2.getName(), item2.getState());

        Repository rep1 = new Repository("Repo1");
        rep1.add(item1);

    }
}
