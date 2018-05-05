/**
 * @author  Petr Marek
 * @author  Jakub Štefanišin
 */

package project;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.GUI.RootLayout;

/**
 * This application is used for creating and editing block schemes.
 * These schemes performs operations on data provided.
 */
public class Main extends Application {
    public Stage primaryStage;

    /**
     * Start method used for setting and loading the primary stage.
     * @param primaryStage primary window of the application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane root = new BorderPane();

        try {
            Scene scene = new Scene(root,1152,864);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        root.setCenter(new RootLayout(primaryStage));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
