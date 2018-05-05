/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */
package project;

import javafx.scene.control.Alert;

/**
 * This class maintains errors.
 */
public class Errors {
    /**
    * Prints error output and shows an Alert.
    * @param error string with error message
    */
    public static void printErr(String error) {
        System.err.println("ERROR: " + error);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(error);
        errorAlert.showAndWait();
    }
}
