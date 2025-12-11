import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
/**
 * MainGUI class for Jaca Hotel application
 * Initializes and displays the main GUI window using JavaFX.
 * Loads the initial FXML layout and sets up the primary stage.
 * @version 1.0
 * @author Angel Cruz
 * Date: 11/11/2025
 */

//loads the first FXML file login.fxml and shows the window

public class MainGUI extends Application{
    /**
    * Starts the JavaFX application by loading the main GUI layout from an FXML file.
    * Initializes the primary stage with the loaded scene and displays it.
    * @param primaryStage the primary stage for this application
    * @throws Exception if an error occurs during loading the FXML file
    */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotels.fxml"));  //load the login UI from the class path
        Scene scene = new Scene(loader.load(), 900, 600);
        hotelviewController hc = loader.getController();
        hc.initForUser(null);
        primaryStage.setTitle("JACA Hotel");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    /**
     * Main method to launch the JavaFX application.
     * @param args coommand line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
