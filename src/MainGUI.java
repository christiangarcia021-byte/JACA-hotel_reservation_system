import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

//loads the first FXML file login.fxml and shows the window

public class MainGUI extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotels.fxml"));  //load the login UI from the class path
        Scene scene = new Scene(loader.load(), 900, 600);
        hotelviewController hc = loader.getController();
        hc.initForUser(null);
        primaryStage.setTitle("JACA Hotel - Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
