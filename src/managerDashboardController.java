import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class managerDashboardController {

    @FXML private Label mainContent;

@FXML private void showReservations()
    {
        mainContent.setText("Reservations View");
    }
    @FXML private void showRooms(){
        mainContent.setText("Rooms View comming soon..." );
    }

    @FXML private void showCustomers(){
        mainContent.setText("Customers View comming soon..." );
    }

    @FXML private void logout(ActionEvent event){
    try{
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));

        stage .setTitle("Jaca Hotel - Login");
        stage.show();


    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }
}

