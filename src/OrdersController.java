import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class OrdersController {

    @FXML private Label customerNameLabel;
    @FXML private Label customerEmailLabel;
    @FXML private ListView<String> ordersList;
    @FXML private VBox detailsBox;

    private customer currentCustomer;
    private orderList orders;
    private ObservableList<order> orderObjects = FXCollections.observableArrayList();

    public void init(customer cust) {
        this.currentCustomer = cust;
        if (cust != null) {
            customerNameLabel.setText("Customer: " + cust.getName() + " " + cust.getLname());customerEmailLabel.setText("Email: " + cust.getEmail());
        }
        orders = new orderList(cust);
        populateOrderList();
    }

    private void populateOrderList() {
        ordersList.getItems().clear();
        orderObjects.clear();

        order current = orders.orderList;

        while (current != null) {
            String display = String.format("%s  |  %s  %s  |  $%.2f", current.getOrderCode(), current.getOrderDate(), current.getOrderTime(), current.getTotalPaid());

            ordersList.getItems().add(display);
            orderObjects.add(current);
            current = current.getNext();
        }

        ordersList.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx == null || newIdx.intValue() < 0) return;
            showOrderDetails(orderObjects.get(newIdx.intValue()));
        });
    }

    private void showOrderDetails(order ord) {
        detailsBox.getChildren().clear();

        if (ord == null) return;

        detailsBox.getChildren().add(new Label("Order Code: " + ord.getOrderCode()));
        detailsBox.getChildren().add(new Label("Date: " + ord.getOrderDate()));
        detailsBox.getChildren().add(new Label("Time: " + ord.getOrderTime()));
        detailsBox.getChildren().add(new Label(String.format("Total Paid: $%.2f", ord.getTotalPaid())));
        detailsBox.getChildren().add(new Separator());
        detailsBox.getChildren().add(new Label("Reservations:"));

        orderDetails d = ord.getOrderDetails();
        while (d != null) {
            VBox block = new VBox(2);
            block.getChildren().add(new Label("Hotel: " + d.getHotelName()));
            block.getChildren().add(new Label("Room: " + d.getRoomName()));
            block.getChildren().add(new Label("Start: " + d.getStartDate()));
            block.getChildren().add(new Label("End: " + d.getEndDate()));
            block.getChildren().add(new Label("Total Days: " + d.getTotalDays()));
            block.getChildren().add(new Label(String.format("Room Price Paid: $%.2f", d.getRoomPricePaid())));
            block.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");
            detailsBox.getChildren().add(block);
            detailsBox.getChildren().add(new Separator());

            d = d.getNext();
        }
    }

    @FXML
    private void onClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
