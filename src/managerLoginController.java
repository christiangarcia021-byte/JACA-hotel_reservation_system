import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import javafx.stage.Stage;



public class managerLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private boolean validateCredentials(String username, String password) {
        // For demonstration purposes, we use hardcoded credentials.
        // In a real application, this method would query a database or another secure source.
        return "manager".equals(username) && "managerPass123".equals(password);
    }
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateCredentials(username, password)) {
            errorLabel.setVisible(false);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/managerDashboard.fxml"));
                    Parent root = loader.load();


                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 650));
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    new Alert(AlertType.ERROR, "Failed to load manager dashboard").showAndWait();
                }


        } else {
            errorLabel.setText("Invalid username or password.");
            errorLabel.setVisible(true);
        }
    }
    @FXML
    private void handleCancel(ActionEvent event) {
      try{
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Object controller = loader.getController();
            if(controller instanceof loginController Ic){
                Ic.initForPopup();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene loginScene = new Scene(root,
            stage.getScene().getWidth(),
            stage.getScene().getHeight());
            stage.setScene(loginScene);
            stage.setTitle("Login");
      }catch(IOException e){
          e.printStackTrace();
          new Alert(AlertType.ERROR, "Failed to load user login view").showAndWait();
      }

    }


}

