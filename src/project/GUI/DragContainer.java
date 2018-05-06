/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.DataFormat;
import javafx.util.Pair;

/**
 * This class represents container for storing
 * IDs of drag and drop objects.
 */
public class DragContainer implements Serializable {

    private static final long serialVersionUID = -1890998765646621338L;

    public static final DataFormat AddNode =
            new DataFormat("project.DragIcon.add");

    public static final DataFormat DragNode =
            new DataFormat("project.DraggableNode.drag");

    public static final DataFormat AddLink =
            new DataFormat("project.NodeLink.add");

    private final List <Pair<String, Object> > mDataPairs = new ArrayList <Pair<String, Object> > ();

    public void addData (String key, Object value) {
        mDataPairs.add(new Pair<String, Object>(key, value));
    }

     /**
     * Returns a particular value of given key from DragContainer instance
     * @param <T> the expected class of the value.
     * @param key key of which value is to be returned
     * @return T value of given key
     */
    public <T> T getValue (String key) {
        for (Pair<String, Object> data: mDataPairs) {
            if (data.getKey().equals(key)) {
                return (T) data.getValue();
            }
        }
        return null;
    }

    /**
     * Returns total list of data from DragContainer instance
     * @return List list containing all keys and values
     */
    public List <Pair<String, Object> > getData () { return mDataPairs; }
}
