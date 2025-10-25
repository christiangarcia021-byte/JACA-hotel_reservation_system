import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;




public class MainGUI extends Application{

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jaca Hotel");
        Label label = new Label("Welcome to Jaca Hotel");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }






}
