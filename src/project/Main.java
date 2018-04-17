package project;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project.GUI.UserInterface;
import project.TryConnection;


/**
 * Created by petr on 3/28/18.
 */
public class Main extends Application {


    public static void main(final String[] args) {
        //TryConnection tryConn = new TryConnection();
        //tryConn.run();
        launch(args);


    }

    @Override
    public void start(Stage window) throws Exception {
        UserInterface gui = new UserInterface(window);
        gui.init();

    }
}
