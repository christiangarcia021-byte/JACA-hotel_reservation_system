import javafx.fxml.FXML;
import javafx.scene.control.*;


public class PaymentController {
    @FXML private Label reservationSummarylabel;
    @FXML private TextField cardNumberTextField;
    @FXML private TextField expiryField;
    @FXML private PasswordField cvvField;

    private room selectRoom;
    private int customerID;
    private String startDate;
    private String endDate;

    private final reservation reservationService = new reservation();

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
       private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

       }

}
