
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
    @FXML private Label reservationSummarylabel;
    /** FXML TextField for entering the card number. */
    @FXML private TextField cardNumberTextField;
    /** FXML TextField for entering the card expiry date. */
    @FXML private TextField expiryField;
    /** FXML PasswordField for entering the card CVV code. */
    @FXML private PasswordField cvvField;
    /**Instance variables to hold selected room, customer ID, and reservation dates.
     */
    private room selectRoom;
    /**
     * The ID of the customer making the reservation.
     */
    private int customerID;
    /**
     * The start date of the reservation.
     */
    private String startDate;
    /**
     * The end date of the reservation.
     */
    private String endDate;
    /** Reservation service instance for handling reservation logic. */
    private final reservation reservationService = new reservation();
    /**
     * Initialize payment screen data using the selected roomm,
     * customer ID and reservation dates. Calculates total cost and displays summary.
     * @param selectRoom the rooom selected for reservation
     * @param customerID the ID of the customer making the reservation
     * @param startDate the start date of the reservation
     * @param endDate the end date of the reservation
     */
    public void initData(room selectRoom, int customerID, String startDate, String endDate){
        this.selectRoom = selectRoom;
        this.customerID = customerID;
        this.startDate = startDate;
        this.endDate = endDate;

        int days = reservationService.calcDays(startDate, endDate);
        double cost = reservationService.reservationCost(selectRoom, days);

        String summary = "Room: " + selectRoom.getID()+
                "\nStart: " +startDate+
                "\nEnd: " +endDate+
                "\nNights: " +days+
                "\nTotal: " +cost;
        reservationSummarylabel.setText(summary);

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
            showAlert(Alert.AlertType.ERROR, "Invalid card Info", error);
            return;

        }
        if (!reservationService.isAvailable(selectRoom, startDate, endDate)){
            showAlert(Alert.AlertType.ERROR, "Room unavailable", "Sorry, this room is no longer available for the selected dates.");
            return;
        }
        boolean paymentOk = true;
        if(!paymentOk){
            showAlert(Alert.AlertType.ERROR, "Payment Failed", "Your card was declined. Please try another card.");
            return;

        }
        boolean reserved = reservationService.makeReservation(customerID, selectRoom, startDate, endDate);
        if(!reserved){
            showAlert(Alert.AlertType.ERROR, "Error", "There was a problem saving your reservation.");
            return;

        }

        showAlert(Alert.AlertType.INFORMATION, "Payment successful", "Your payment was processed and the reservation is confirmed");

        //TODO: navigate to a confirmation screen or back to main menu


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
