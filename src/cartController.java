import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

// shows all items that are in the customers cart
// they can see the price total and can remove
// when I finish (hopefully it satisfactory) the reservation stuff,  both the view of the cart with be passed to the reservtion page when clicking the reservation page button


public class cartController {
    @FXML private TableView<cartItem> cartTable;
    @FXML private TableColumn<cartItem, String> cItem;
    @FXML private TableColumn<cartItem, Number> cPrice;
    @FXML private TableColumn<cartItem, Void> cRemove;
    @FXML private Label totalLabel;

    private customer currentCustomer; // signed in user
    private cart myCart; // shared cart that can hopefully be used in the reservation page


    public void init(customer c, cart crt) // this is called by the hotelview controller
    {
        this.currentCustomer = c;
        this.myCart = crt;

        //binds table columns
        cItem.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().displayName()));
        cPrice.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(p.getValue().price()));

        cRemove.setCellFactory((TableColumn<cartItem, Void> col) -> new TableCell<>() //Builds the remove button for each row
        {
            private final Button btn = new Button("Remove");
            {
                btn.setOnAction(event -> {
                    cartItem ci = getTableView().getItems().get(getIndex());
                    myCart.remove(ci);
                    refreshpriceTotals();
                });
            }
            @Override
            public void updateItem(Void it, boolean empty) { // only shows the button on the rows with rooms
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });

        cartTable.setItems(myCart.getItems());
        refreshpriceTotals();
    }

    private void refreshpriceTotals() //refreshes the total and redisplays the price when a room is added
    {
        cartTable.refresh();
        totalLabel.setText(String.format("Total: $%.2f", myCart.total()));

    }

    @FXML private void onContinue() // if cart is empty it will then warn the user, otherwise it will load the resvetion page
    {
        if (myCart.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").showAndWait();
        }


        //gonna use this for the resvertion page

//       try {
//           FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
//           Scene scene = new Scene(loader.load(), 800, 500);
//           reservationController rc = loader.getController();
//           rc.loadCartForCustomer(currentCustomer, myCart);
//           Stage stage = new Stage();
//           stage.setScene(scene);
//           stage.show();
//           stage.setTitle("Reservation");
//
//           ((Stage) totalLabel.getScene().getWindow()).close();
//       }
//       catch (Exception ex)
//       {
//           ex.printStackTrace();
//           new Alert(Alert.AlertType.ERROR,"failed to load page").showAndWait();
//       }

    }
}
