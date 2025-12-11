import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;


public class OrderReceiptController {

    @FXML private Label orderCodeLabel;
    @FXML private Label orderDateLabel;
    @FXML private Label orderTimeLabel;
    @FXML private Label totalPaidLabel;
    @FXML private Label customerLabel;
    @FXML private Label emailLabel;
    @FXML private VBox  detailsBox;
    @FXML private Button backButton;

    private customer currentCustomer;
    private order currentOrder;

    public void initData(customer cust, order ord) {
        this.currentCustomer = cust;
        this.currentOrder = ord;

        orderCodeLabel.setText("Order Code: " + ord.getOrderCode());
        orderDateLabel.setText("Date: " + ord.getOrderDate());
        orderTimeLabel.setText("Time: " + ord.getOrderTime());
        totalPaidLabel.setText(String.format("Total Paid: $%.2f", ord.getTotalPaid()));

        if (cust != null) {
            customerLabel.setText("Customer: " + cust.getName() + " " + cust.getLname()); emailLabel.setText("Email: " + cust.getEmail());
        } else {
            customerLabel.setText("Customer: Guest");
        }

        populateReservationDetails(ord.getOrderDetails());
    }

    private void populateReservationDetails(orderDetails detailHead) {
        detailsBox.getChildren().clear();

        orderDetails current = detailHead;
        while (current != null) {
            VBox block = new VBox(2);
            block.getChildren().add(new Label("Hotel: " + current.getHotelName()));
            block.getChildren().add(new Label("Room: " + current.getRoomName()));
            block.getChildren().add(new Label("Start: " + current.getStartDate()));
            block.getChildren().add(new Label("End: " + current.getEndDate()));
            block.getChildren().add(new Label("Total Days: " + current.getTotalDays()));
            block.getChildren().add(new Label(String.format("Room Price Paid: $%.2f", current.getRoomPricePaid())));

            block.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            detailsBox.getChildren().add(block);
            detailsBox.getChildren().add(new Separator());

            current = current.getNext();
        }
    }
    @FXML
    private void onBackToHotels() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotels.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            hotelviewController hc = loader.getController();
            hc.initForUser(currentCustomer);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("JACA Hotel");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}