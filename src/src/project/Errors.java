package project;

import javafx.scene.control.Alert;

public class Errors {
    public static void printErr(String error) {
        System.err.println("ERROR: " + error);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(error);
        errorAlert.showAndWait();
    }
}
