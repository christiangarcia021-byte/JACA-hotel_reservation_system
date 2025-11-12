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

//populates the UI with DB data
//pulls all the hotels from the hotelController
//shows hotels in ListView
//

//MAKE IT SO YOU ONLY DISPLAY THE ROOMS THAT ARE OPEN  add a check for "if room status = open" in toRoomRows method or something idk


public class hotelviewController {
    @FXML private Label userBadge; // top bar
    @FXML private ListView<hotel> hotelList; // list of hotels ( on the left)
    @FXML private Label hotelTitle, hId, hAddress, hPhone, hEmail, hZip, hRooms; // hotel details (top right)
    //table of rooms (bottom right)
    @FXML private TableView<RoomRow> roomsTable;
    @FXML private TableColumn<RoomRow, String> cName, cType, cStatus, cDescription;
    @FXML private TableColumn<RoomRow, Number> cFloor, cBeds, cPrice, cBedrooms, cBathrooms;

    private customer currentCustomer; // who is logges in
    private hotelController controller; // service that loads hotels from DB

    @FXML private TableColumn<RoomRow, Void> cAdd;

    private final cart myCart = new cart();

    //Calls the loginController after a successful sign in

    public void initForUser(customer c) {
        currentCustomer = c;
        userBadge.setText("Signed in: " + (c != null ? c.getEmail() : ""));

        controller = new hotelController(); //Query Db for hotels and each hotel room
        if (!controller.initHotels()) {
            new Alert(Alert.AlertType.ERROR, "Couldn't load hotel DB", ButtonType.OK).showAndWait();
            return;
        }
        populateHotels();  //fill the ListVIew with Hotel objects

        hotelList.getSelectionModel().selectedItemProperty().addListener((obs,old, h) -> showHotel(h));

        if (!hotelList.getItems().isEmpty()) hotelList.getSelectionModel().selectFirst();
    }

    private void populateHotels() { // Fill the ListView with hotels from the controller
        ObservableList<hotel> items = FXCollections.observableArrayList();

        //gets the array of hotels from the DB
        hotel[] arr = controller.MyHotels;
        for (int i = 0; i < controller.getTotalHotels(); i++) items.add(arr[i]);

        // how each hotel appears in the list
        hotelList.setCellFactory(lv -> new ListCell<>()  {
            @Override protected void updateItem(hotel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        hotelList.setItems(items);
    }

    private void showHotel(hotel h) { // shows a hotel's details and its room in the table
        if (h == null) return;
        hotelTitle.setText(h.getName());
        // hId.setText(String.valueOf(h.getID())); removed the hotel id
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
            // cStatus.setCellValueFactory(d -> d.getValue().status); removed status from gui
            cDescription.setCellValueFactory(d -> d.getValue().description);

        }
            cAdd.setCellFactory(col -> new TableCell<RoomRow, Void>() //each row gets a cart button
            {
                private final Button btn = new Button("Add to Cart");
                {
                    btn.setOnAction(e -> {

                        RoomRow rr = getTableView().getItems().get(getIndex());// gets data from the row

                        room matched = null; //finds the room object that matches the row by name
                        for (room r : h.getRooms()) {
                            if (r != null && safe(r.getName()).equals(rr.name.get())) {
                                matched = r;
                                break;
                            }
                        }
                        if (matched != null) // if it matches cart is added and prompts message
                        {
                            myCart.add(matched, h);
                            new Alert(Alert.AlertType.INFORMATION, "Added " + matched.getName() + " to cart").showAndWait();
                        }
                    });
                }
                @Override protected void updateItem(Void it, boolean empty) //shows button only to rows with data
                {
                    super.updateItem(it, empty);
                    setGraphic(empty ? null : btn);
                }
            });


        roomsTable.setItems(toRoomRows(h));
        roomsTable.refresh();
    }

    private ObservableList<RoomRow> toRoomRows(hotel h) { //helper that converst hotel's room[] into rows for the TableView
        ObservableList<RoomRow> rows = FXCollections.observableArrayList();
        room[] arr = h.getRooms();
        int n = h.getTotalRooms();
        if (arr == null) return rows;
        for (int i = 0; i < n && i < arr.length; i++) {
            room r = arr[i];
            if (r == null) continue;
                if(r.getStatus() != null && r.getStatus().equalsIgnoreCase("open")) { //only add rooms that are open
                    rows.add(new RoomRow(
                            safe(r.getName()), safe(r.getType()), r.getFloor(), r.getBeds(), r.getPrice(),
                            safe(r.getStatus()), r.getBedrooms(), r.getBathrooms(), safe(r.getDescription())
                    ));
                }
        }
        return rows;
    }
    private String safe(String s) { return s == null ? "" : s;}

    //Formats phone number into neat style
    // if it isnt a 10 digit number it will return the raw/original number fromm db
    private String phoneformat(String raw) {
        if (raw == null)
            return "";

        raw = raw.trim();
        String num = raw.replaceAll("\\D", ""); //keeps only digits
        if (num.length() == 10) { //formats number if its 10 digits
            return String.format("(%s) %s-%s", num.substring(0, 3), num.substring(3, 6), num.substring(6)); // (000) 000-0000 format
        }
        return raw;

    }

    // Log out to close window
    @FXML private void onLogout() {
        userBadge.getScene().getWindow().hide();
    }

    @FXML private void onViewCart() { // loads the cart GUI and controller
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

    // inner table row exposes JavaFX properties
    // TableColum read ObservableValue to update cells automatically
    public static class RoomRow {
        final SimpleStringProperty name, type, status, description;
        final SimpleIntegerProperty floor, beds, bedrooms, bathrooms;
        final SimpleDoubleProperty price;
        RoomRow(String name, String type, int floor, int beds, double price,  String status, int bedrooms, int bath, String desc) {
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


