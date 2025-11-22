import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
/**
 * The hotelviewController class is the JavaFx controller for the hotel.fxml display screen. It loads hotels from the DB
 * Displays a list of hotels, its details, and a table with that hotels specific hotel rooms
 * Only rooms with the status open are shown
 * Users can add rooms to their shared cart, view the cart, however, must be logged in to continue
 * @author Angel Cruz
 * @version 1.0     Date: 10/29/2025
 */
public class hotelviewController {
    /**
     * Badge at the top showing the current users email or displays them as guest defined in hotel.fxml
     */
    @FXML private Label userBadge;
    /**
     * List view of all hotels defined in hotel.fxml
     */
    @FXML private ListView<hotel> hotelList;
    /**
     * Hotel details labels shown for the selected hotel defined in hotel.fxml
     */
    @FXML private Label hotelTitle, hId, hAddress, hPhone, hEmail, hZip, hRooms;
    /**
     * Table list displaying detailed information about each room for the selected hotel in a tableview defined in hotel.fxml
     */
    @FXML private TableView<RoomRow> roomsTable;
    /**
     * Columns that displays the text details for a room defined in hotel.fxml
     */
    @FXML private TableColumn<RoomRow, String> cName, cType, cStatus, cDescription;
    /**
     * Columns that displays the number based details for each room defined in hotel.fxml
     */
    @FXML private TableColumn<RoomRow, Number> cFloor, cBeds, cPrice, cBedrooms, cBathrooms;
    /**
     * The currently signed in customer viewing the hotels
     */
    private customer currentCustomer;
    /**
     * Controller that loads the hotels from the DB
     */
    private hotelController controller;
    /**
     * Column that displays an add to cart button to each room
     */
    @FXML private TableColumn<RoomRow, Void> cAdd;
    /**
     * Shared cart object that can be used between displays
     */
    private final cart myCart = new cart();
    /**
     * Initializes the hotel view for the user
     * Loads hotel data from the DB and populates the hotel list
     * Automatically selects the first available hotel
     * @param c the current customer that can be either signed in or a null guest
     */
    public void initForUser(customer c) {
        currentCustomer = c;
        userBadge.setText("Signed in: " + (c != null && c.isSignedIn() ? c.getEmail() : "Guest"));

        controller = new hotelController();
        if (!controller.initHotels()) {
            new Alert(Alert.AlertType.ERROR, "Couldn't load hotel DB", ButtonType.OK).showAndWait();
            return;
        }
        populateHotels();

        hotelList.getSelectionModel().selectedItemProperty().addListener((obs,old, h) -> showHotel(h));

        if (!hotelList.getItems().isEmpty()) hotelList.getSelectionModel().selectFirst();
    }
    /**
     * Populates the hotel list view with all the available hotels retrieved from the hotelcontroller
     */
    private void populateHotels() {
        ObservableList<hotel> items = FXCollections.observableArrayList();

        hotel[] arr = controller.MyHotels;
        for (int i = 0; i < controller.getTotalHotels(); i++) items.add(arr[i]);

        hotelList.setCellFactory(lv -> new ListCell<>()  {
            @Override protected void updateItem(hotel item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        hotelList.setItems(items);
    }
    /**
     * Displays the details for the selected hotel and refreshes the rooms table.
     * Creates the add to cart button for each room and clicking it adds that rooms to myCart
     * @param h the selected hotel to display
     */
    private void showHotel(hotel h) {
        if (h == null) return;
        hotelTitle.setText(h.getName());
        //hId.setText(String.valueOf(h.getID())); removed the hotel id from GUI view
        hAddress.setText(h.getAddress());
        hPhone.setText(phoneformat(String.valueOf(h.getPhone())));
        hEmail.setText(h.getEmail());
        hZip.setText(String.valueOf(h.getZipcode()));
        hRooms.setText(String.valueOf(h.getTotalRooms()));

        if (cName.getCellValueFactory() == null) {
            cName.setCellValueFactory(d -> d.getValue().name);
            cType.setCellValueFactory(d -> d.getValue().type);
            cFloor.setCellValueFactory(d -> d.getValue().floor);
            cBeds.setCellValueFactory(d -> d.getValue().beds);
            cBedrooms.setCellValueFactory(d -> d.getValue().bedrooms);
            cBathrooms.setCellValueFactory(d -> d.getValue().bathrooms);
            cPrice.setCellValueFactory(d -> d.getValue().price);
            // cStatus.setCellValueFactory(d -> d.getValue().status); removed the GUI status view
            cDescription.setCellValueFactory(d -> d.getValue().description);

        }
            cAdd.setCellFactory(col -> new TableCell<RoomRow, Void>()
            {
                private final Button btn = new Button("Add to Cart");
                {
                    btn.setOnAction(e -> {
                        if(!ensureSignedIn())
                            return;
                        RoomRow rr = getTableView().getItems().get(getIndex());

                        room matched = null;
                        for (room r : h.getRooms()) {
                            if (r != null && safe(r.getName()).equals(rr.name.get())) {
                                matched = r;
                                break;
                            }
                        }
                        if (matched != null)
                        {
                            myCart.add(matched, h);
                            new Alert(Alert.AlertType.INFORMATION, "Added " + matched.getName() + " to cart").showAndWait();
                        }
                    });
                }
                @Override protected void updateItem(Void it, boolean empty)
                {
                    super.updateItem(it, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        roomsTable.setItems(toRoomRows(h));
        roomsTable.refresh();
    }
    /**
     * Builds a list of RoomRow objects for display in the table view
     * Only displays rooms with open status
     * @param h the hotel with the rooms that will be displayed
     * @return an ObservableList containing table rows for open rooms only
     */
    private ObservableList<RoomRow> toRoomRows(hotel h) {
        ObservableList<RoomRow> rows = FXCollections.observableArrayList();
        room[] arr = h.getRooms();
        int n = h.getTotalRooms();
        if (arr == null) return rows;
        for (int i = 0; i < n && i < arr.length; i++) {
            room r = arr[i];
            if (r == null)
                continue;
            if(r.getStatus() != null && r.getStatus().equalsIgnoreCase("open")) {rows.add(new RoomRow(safe(r.getName()), safe(r.getType()), r.getFloor(), r.getBeds(), r.getPrice(), safe(r.getStatus()), r.getBedrooms(), r.getBathrooms(), safe(r.getDescription())));
                }
        }
        return rows;
    }
    /**
     * Makes sure that the given string is not null
     * @param s the input string that may be null
     * @return the original string if not null otherwise an empty string
     */
    private String safe(String s) { return s == null ? "" : s;}
    /**
     * Formats a raw/original phone string as a (000) 000-0000 when it contains exactly 10 digits
     * @param raw the raw/original input phone text
     * @return a phone number in the proper format or the original phone number text
     */
    private String phoneformat(String raw) {
        if (raw == null)
            return "";

        raw = raw.trim();
        String num = raw.replaceAll("\\D", "");
        if (num.length() == 10) {
            return String.format("(%s) %s-%s", num.substring(0, 3), num.substring(3, 6), num.substring(6));
        }
        return raw;
    }
    /**
     * Logs out the current user and updates the badge to display guest
     */
    @FXML private void onLogout() {
        currentCustomer = null;
        userBadge.setText("Signed in: Guest");
    }

    /**
     * Loads the cart.fxml display window
     * If the user is not signed in, the user is prompted to ensureSignedIn
     */
    @FXML private void onViewCart() {
        if (!ensureSignedIn()) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart.fxml"));
            Scene scene = new Scene(loader.load(), 700, 480);

            cartController cc = loader.getController();
            cc.init(currentCustomer, myCart);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Your Cart");
            stage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "failed to open cart").showAndWait();

        }
    }
    /**
     * Ensures whether a customer is currently signed in
     * If not the user will be prompted to log in
     * @return true if the user is signed in; otherwise return false
     */
    private boolean ensureSignedIn() {
        if (currentCustomer != null && currentCustomer.isSignedIn()) return true;

        Alert pre = new Alert(Alert.AlertType.INFORMATION);
        pre.setTitle("Login Required");
        pre.setHeaderText(null);
        pre.setContentText("Please login before continuing");
        pre.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (pre.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            customer result = showLoginPopup();
            if (result != null && result.isSignedIn()) {
                currentCustomer = result;
                userBadge.setText("Signed in: " + currentCustomer.getEmail());
                return true;
            }
            return false;
        }

        return false;
    }
    /**
     * Opens a login modal window and returns the signed in customer if the login is successful
     * @return the signed in customer; otherwise return null
     */
    private customer showLoginPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            loginController lc = loader.getController();
            lc.initForPopup();
            Stage dialog = new Stage();
            dialog.setTitle("Log In");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(userBadge.getScene().getWindow());
            dialog.setScene(scene);
            dialog.showAndWait();
            return lc.getSignedInCustomer();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "failed to open login popup").showAndWait();
            return null;
        }
    }
    /**
     * Represents a single row in the rooms table
     * JavaFx property types make it so the Tableview updates automatically on value changes
     */
    public static class RoomRow {
        /**
         * String properties for room and hotel details used by the Tableview
         */
        final SimpleStringProperty name, type, status, description;
        /**
         * Integer properties for room and hotel details used by the Tableview
         */
        final SimpleIntegerProperty floor, beds, bedrooms, bathrooms;
        /**
         * Price property for the room used by the Tableview
         */
        final SimpleDoubleProperty price;
        /**
         * Creates a new RoomRow with given room details
         * @param name the room name
         * @param type the room type
         * @param floor the floor number
         * @param beds the number of beds
         * @param price the room price for one day
         * @param status the rooms current status
         * @param bedrooms the number of bedrooms
         * @param bath the number of bathrooms
         * @param desc the room description
         */
        RoomRow(String name, String type, int floor, int beds, double price,  String status, int bedrooms, int bath, String desc)
        {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.floor = new SimpleIntegerProperty(floor);
            this.beds = new SimpleIntegerProperty(beds);
            this.price = new SimpleDoubleProperty(price);
            this.status = new SimpleStringProperty(status);
            this.bedrooms = new SimpleIntegerProperty(bedrooms);
            this.bathrooms = new SimpleIntegerProperty(bath);
            this.description = new SimpleStringProperty(desc);
        }

    }


}


