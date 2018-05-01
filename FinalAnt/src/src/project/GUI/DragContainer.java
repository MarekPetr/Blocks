/**
* DragContainer class is used for saving objects 
* used in drag and drop operations.
*
* @author  Petr Marek
* @author  Jakub Štefanišin
*/

package project.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.DataFormat;
import javafx.util.Pair;

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

    // get a particular value
    public <T> T getValue (String key) {
        for (Pair<String, Object> data: mDataPairs) {
            if (data.getKey().equals(key)) {
                return (T) data.getValue();
            }
        }
        return null;
    }

    // Get total list of data
    public List <Pair<String, Object> > getData () { return mDataPairs; }
}
