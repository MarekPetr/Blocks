package project;

import project.items.AbstractItem;
import project.repository.Repository;
import project.items.ItemDiv;
import project.items.ItemMinus;
import project.items.ItemPlus;
import project.connection.Connection;

/**
 * Created by petr on 4/17/18.
 */
public class TryConnection {
    public void run() {
        AbstractItem item1, item2, item3, item4, it5, it6;
        Connection con1, con2;
        item1 = new ItemPlus("first", 2, 4);
        System.out.printf("1jmeno: %s, stav:%f\n", item1.getName(), item1.getState());
        item1.execute();
        System.out.printf("1jmeno: %s, stav:%f\n\n", item1.getName(), item1.getState());

        item2 = new ItemMinus("second", 2, 3);
        System.out.printf("2jmeno: %s, stav:%f\n", item2.getName(), item2.getState());
        item2.execute();
        System.out.printf("2jmeno: %s, stav:%f\n", item2.getName(), item2.getState());

        item3 = new ItemPlus("third", 2, 1);
        item4 = new ItemPlus("Fourth", 2, 2);
        item3.createConnection();
        item3.setConnection(item4);
        item3.execute();
        item3.links.get(0).transferValue();
        item4.execute();
        System.out.printf("%f\n", item4.outValue);

        it5 = new ItemDiv("Fourth", 2, 3);
        item4.createConnection();
        item4.setConnection(it5);
        item4.links.get(0).transferValue();
        it5.execute();

        System.out.printf("5: %f\n", it5.outValue);

        Repository rep1 = new Repository("Repo1");
        rep1.add(item1);
    }

}
