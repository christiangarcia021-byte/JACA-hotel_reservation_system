import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * The loginController class is the JavaFx controller for the login.fxml display screen
 * It validates users email and password based on the DB
 * Displays login results
 * If login successful the popup window login window closes or navigates the user back to the hotel screens hotel.fxml
 * @author Angel Cruz
 * @version 1.0     Date: 10/29/2025
 */
public class loginController {
    /**
     * Email input text field defined in login.fxml
     */
    @FXML private TextField emailField;
    /**
     * Password input text field defined in login.fxml
     */
    @FXML private PasswordField passField;
    /**
     * Label that displays whether login was a success or error
     */
    @FXML private Label status;
    /**
     * The customer object that represents the current user trying to sign in
     */
    private customer currentCustomer;
    /**
     * Shows whether this controller is being used as a modal popup login window
     */
    private boolean openedAsPopup = false;
    /**
     * Holds the customer who logged in successfully otherwise null
     */
    private customer signedInCustomer = null;
    /**
     * Activates popup mode for this controller
     * A successful login will close this window
     */
    public void initForPopup() {
        this.openedAsPopup = true;
    }
    /**
     * Retrieves the customer who successfully logged in
     * @return the signed in customer if login is successful; otherwise null
     */
    public customer getSignedInCustomer() {
        return signedInCustomer;
    }
    /**
     * Handles the sign in button
     * Validates the user based on their entered password and email
     * @param event the action even triggered when the sign in button is clicked
     */
    @FXML private void onSignIn(ActionEvent event) {
        currentCustomer = new customer();
        boolean ok = currentCustomer.SignIn(emailField.getText(), passField.getText());
        if (!ok) {
            status.setText("Invalid Password or Email");
            return;
        }

        if (openedAsPopup) {
            signedInCustomer = currentCustomer;
            ((Stage) status.getScene().getWindow()).close();
            return;
        }

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotels.fxml"));
                Scene hotelsScene = new Scene(loader.load(), 960, 600);
                hotelviewController hc = loader.getController();
                hc.initForUser(currentCustomer);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(hotelsScene);
                stage.setTitle("Hotel - Search");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                status.setText("Failed to load hotels view");
            }
        }
    }
