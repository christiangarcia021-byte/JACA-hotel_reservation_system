import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
/**
 * The cartController class is the JavaFx controller for the cart.fxml display screen
 * Displays cart items, their prices, and the progressive price total
 * Lets users remove button for each item in the cart
 * Lets users continue to reservation button to navigate the user to the reservation page
 * @author Angel Cruz
 * @version 1.0     Date: 11/11/2025
 */
public class cartController {
    /**
     * Table that shows the rows of items currently in the cart defined in cart.fxml
     */
    @FXML private TableView<cartItem> cartTable;
    /**
     * Column that displays the items hotel and room name defined in cart.fxml
     */
    @FXML private TableColumn<cartItem, String> cItem;
    /**
     * Column that displays the room price for each row defined in cart.fxml
     */
    @FXML private TableColumn<cartItem, Number> cPrice;
    /**
     * Column that display a remove button for each row defined in cart.fxml
     */
    @FXML private TableColumn<cartItem, Void> cRemove;
    /**
     * Label that shows the progressive total for all the items in the cart defined in cart.fxml
     */
    @FXML private Label totalLabel;
    /**
     * The currently sign in user that is tied with the current cart session
    */
    private customer currentCustomer;
    /**
     * Shared cart object that can be used between displays
     */
    private cart myCart;
    /**
     * Initializes cell factories for the cart table and the shared cart items into the view
     * @param c current signed in customer
     * @param crt the shared cart instance to display and change
     */
    public void init(customer c, cart crt)
    {
        this.currentCustomer = c;
        this.myCart = crt;

        cItem.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().displayName()));
        cPrice.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(p.getValue().price()));

        cRemove.setCellFactory((TableColumn<cartItem, Void> col) -> new TableCell<>()
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
            public void updateItem(Void it, boolean empty)
            {
                super.updateItem(it, empty);
                setGraphic(empty ? null : btn);
            }
        });

        cartTable.setItems(myCart.getItems());
        refreshpriceTotals();
    }
    /**
     * Refreshes the cart table and updates the total prices label based on the current cart items
     */
    private void refreshpriceTotals()
    {
        cartTable.refresh();
        totalLabel.setText(String.format("Total: $%.2f", myCart.total()));

    }
    /**
     * Continues to the reservation page after checking if the cart is not empty
     * If the cart is empty a warning will display; otherwise it loads the reservation.fxml display
     * Navigates the current customer and cart to the reservationController
     */
    @FXML private void onContinue()
    {
        if (myCart.isEmpty())
        {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").showAndWait();
        }
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
           Scene scene = new Scene(loader.load(), 800, 500);
           reservationController rc = loader.getController();
           rc.loadCartForCustomer(currentCustomer, myCart);
           Stage stage = new Stage();
           stage.setScene(scene);
           stage.show();
           stage.setTitle("Reservation");

           ((Stage) totalLabel.getScene().getWindow()).close();
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
           new Alert(Alert.AlertType.ERROR,"failed to load page").showAndWait();
       }

    }
}
