import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//handles the login screen interactions
//controller reads email and password and verifies with DB using customer.Signin

public class loginController {
    // These are implmented from login.fxml through the matching fx id values
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Label status;

    private customer currentCustomer; // holds the signed-in user

    @FXML private void onSignIn(ActionEvent event) {
        // try to sign in using DB credentials
        currentCustomer = new customer(); // talks to the DB for login
        boolean ok = currentCustomer.SignIn(emailField.getText(), passField.getText());
        if (!ok) {
            status.setText("Invalid Password or Email");
            return;
        }
            // if login passes through it will load the hotel view UI and pass the user to the hotel view controller
            try {
                //loads the hotel screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotels.fxml"));
                Scene hotelsScene = new Scene(loader.load(), 960, 600);
                // gets the controller for the hotel view so it can pass it to the currentCustomer
                hotelviewController hc = loader.getController();
                hc.initForUser(currentCustomer);

                // swaps the current window to show the hot view GUI
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(hotelsScene);
                stage.setTitle("Hotel - Search");
            } catch (Exception ex) {
                ex.printStackTrace();
                status.setText("Failed to load hotels view");
            }
        }
    }
