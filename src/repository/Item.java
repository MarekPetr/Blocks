package repository;

/**
 * Created by petr on 3/28/18.
 */
public interface Item {
    String getName();

    int numberOfPorts();

    void execute();

    double getState();
}
