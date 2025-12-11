import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
/**
 * Controller class for handling payment processing in the hotel reservation system.
 * Manages user input for payment details, validates the information,
 * checks room availability, processes payment, and confirms reservations.
 * @version 1.0
 * @author Christian Garcia
 * Date: 11/11/2025
 */
public class PaymentController {
    /**
     * FXML UI components for displaying reservation summary and collecting payment details.
     */
    @FXML private VBox reservationBox;
    /** FXML TextField for entering the card number. */
    @FXML private TextField cardNumberTextField;
    /** FXML TextField for entering the card expiry date. */
    @FXML private TextField expiryField;
    /** FXML PasswordField for entering the card CVV code. */
    @FXML private PasswordField cvvField;

    @FXML private Button confirmButton;

    private customer currentCustomer;
    private reservation[] reservations;

    public void initData(customer cust, reservation[] reservations, double totalCost) {
        this.currentCustomer = cust;
        this.reservations = reservations;

        reservationBox.getChildren().clear();

        if (cust != null) {
            Label custLabel = new Label("Customer: " + cust.getName() + " " + cust.getLname() + "    Email: " + cust.getEmail());
            custLabel.setStyle("-fx-font-weight: bold;");
            reservationBox.getChildren().add(custLabel);
        }

        reservationBox.getChildren().add(new Separator());

        for (int i = 0; i < reservations.length; i++) {
            reservation r = reservations[i];
            room rm = r.getSelectedRoom();

            String hotelName = dbUtil.getHotelName(rm.getHOTEL_ID());

            VBox block = new VBox(2);
            block.getChildren().add(new Label("Hotel: " + hotelName));
            block.getChildren().add(new Label("Room: " + rm.getName()));
            block.getChildren().add(new Label("Start: " + r.getStartDate()));
            block.getChildren().add(new Label("End: " + r.getEndDate()));
            block.getChildren().add(new Label("Total Days: " + r.getTotal_days()));
            block.getChildren().add(new Label(String.format("Room Price Paid: $%.2f", r.getTotal_cost())));

            block.setStyle("-fx-padding: 5;" + "-fx-border-color: lightgray;" + "-fx-border-width: 1;");

            reservationBox.getChildren().add(block);
            reservationBox.getChildren().add(new Separator());
        }

        Label totalLabel = new Label(String.format("Total: $%.2f", totalCost));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");
        reservationBox.getChildren().add(totalLabel);
    }
    /**
     * Handles the confirmation of payment.
     * Validates card information, checks room availability,
     * processes payment, and confirms the reservation.
     * Displays appropriate alerts for errors or success.
     */
    @FXML private void onConfirmPayment(){
        String cardNumber = cardNumberTextField.getText().replaceAll("\\s", "");
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        String error = validate(cardNumber, expiry, cvv);
        if(error != null){
            showAlert(Alert.AlertType.ERROR, "Invalid payment details", error);
            return;
        }
        paymentInfo pi = new paymentInfo();
        pi.setCardNumber(cardNumber);
        pi.setCvv(cvv);

        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int yearShort = Integer.parseInt(parts[1]);
        int yearFull  = 2000 + yearShort;
        pi.setExpiryMonth(month);
        pi.setExpiryYear(yearFull);
        pi.setCardHolderName(
                currentCustomer.getName() + " " + currentCustomer.getLname()
        );
        try {
            order checkoutResult = cartUtility.checkout(reservations, currentCustomer, pi);

            if (checkoutResult == null) {
                showAlert(Alert.AlertType.ERROR, "Checkout failed","There was a problem completing your order.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Payment successful", "Your payment was processed and the reservation is confirmed" + " Click OK to view receipt");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/receipt.fxml"));
            Scene receiptScene = new Scene(loader.load(), 450, 650);

            OrderReceiptController rc = loader.getController();
            rc.initData(currentCustomer, checkoutResult);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.setScene(receiptScene);
            stage.centerOnScreen();
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Checkout failed", "There was a problem completing your order:\n" + e.getMessage()
            );
        }
    }
    /**
     * Validates the credit card information entered by the user.
     * @param cardNumber 16-digit card number
     * @param expiry in MM/YY format
     * @param cvv 3 or 4-digit CVV code
     * @return null if valid, otherwise an error message
     */
    private String validate(String cardNumber, String expiry, String cvv){
        if(cardNumber.isEmpty() || expiry.isEmpty() ||cvv.isEmpty())
            return "All fields are required.";
        if(!cardNumber.matches("\\d{16}"))
            return "Card number must be 16 digits.";
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}"))
            return "Expiry must be MM/YY.";
        if (!cvv.matches("\\d{3,4}"))
            return "CVV must be 3 or 4 digits.";
        return null;

    }
    /**
     * Displays an alert dialog with the specified type, title, and message.
     * @param type the type of alert (ERROR, INFORMATION, etc.)
     * @param title the title of the alert dialog
     * @param message the content message of the alert
     */
       private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

       }

}
