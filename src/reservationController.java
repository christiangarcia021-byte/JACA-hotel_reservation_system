import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The reservationController class is the JavaFx controller for the reservation.fxml display screen
 * Displays each cart item/rooms
 * Lets users pick a valid start and end date per room based on the item/rooms availability using a calendar
 * Calculates the total cost per day and allows user to continue to payment screen
 * @author Angel Cruz
 * @version 1.0     Date: 11/17/2025
 */
public class reservationController {
    /**
     * Table showing one row for each cart item
     */
    @FXML private TableView<Row> roomsTable;
    /**
     * Hotel name column
     */
    @FXML private TableColumn<Row, String> cHotel;
    /**
     * Room name column
     */
    @FXML private TableColumn<Row, String> cRoom;
    /**
     * Price column showing the price for each day
     */
    @FXML private TableColumn<Row, Number> cPrice;
    /**
     * Start date column using dropdown menu
     */
    @FXML private TableColumn<Row, String> cStart;
    /**
     * End date column using dropdown menu
     */
    @FXML private TableColumn<Row, String> cEnd;
    /**
     * Label that shows the total price of all reservations
     */
    @FXML private Label cartTotalLabel;
    /**
     * The Iso date format used (YYYY-MM-DD)
     */
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE;
    /**
     * The current signed in customer
     */

    private customer currentCustomer;
    /**
     * The users current shopping cart
     */

    private cart currentCart;
    /**
     * Map of each rooms calendar availability
     */

    private final Map<Integer, int[][][][]> roomCalendarMap = new HashMap<>();
    /**
     * The current year in which is used as a base to check date availability
     */
    private int baseYear;
    /**
     * Represents a single table row bound to a cartItem
     * Holds display properties for hotel,room,pricing,and observable values
     * Lists are used by the start and end Comboboxes
     */
    public static class Row {
        /**
         * The room and hotel this row belongs to
         */
        final cartItem cartItem;
        /**
         * hotel name property
         */
        final StringProperty hotelName = new SimpleStringProperty();
        /**
         * Room name property
         */
        final StringProperty roomName  = new SimpleStringProperty();
        /**
         * Room price property
         */
        final DoubleProperty roomPrice = new SimpleDoubleProperty();
        /**
         * List of possible start dates
         */
        final ObservableList<String> startList = FXCollections.observableArrayList();
        /**
         * List of possible end dates
         */
        final ObservableList<String> endList   = FXCollections.observableArrayList();
        /**
         * Selected start date
         */
        final StringProperty startValue = new SimpleStringProperty();
        /**
         * Selected end date
         */
        final StringProperty endValue   = new SimpleStringProperty();
        /**
         * Builds a row for one cartitem
         * @param ci The cart item
         */
        Row(cartItem ci) {
            this.cartItem = ci;
            hotelName.set(ci.selectedHotel.getName());
            roomName.set(ci.selectedRoom.getName());
            roomPrice.set(ci.selectedRoom.getPrice());
        }
    }

    /**
     * Sets up the reservation screen for the given customer and cart
     * Loads all rooms and sets up the Comboboxes for start dates and end dates
     * Calculates the total cost
     * @param cust The signed in customer
     * @param crt The users cart containing selected rooms
     */
    public void loadCartForCustomer(customer cust, cart crt) {
        currentCustomer = cust;
        currentCart = crt;

        roomCalendarMap.clear();
        for (cartItem item : currentCart.getItems()) {
            Calendar cal = new Calendar(item.selectedRoom.getID());
            roomCalendarMap.put(item.selectedRoom.getID(), cal.getCalendar());
        }

        baseYear = LocalDate.now().getYear();

        cHotel.setCellValueFactory(p -> p.getValue().hotelName);
        cRoom.setCellValueFactory(p -> p.getValue().roomName);
        cPrice.setCellValueFactory(p -> p.getValue().roomPrice);

        cStart.setCellFactory(col -> new TableCell<Row, String>() {
            private final ComboBox<String> startBox = new ComboBox<>();
            private Row boundRow;
            { setContentDisplay(ContentDisplay.GRAPHIC_ONLY); }
            /**
             * Connects the Comboboxs to this rows start date
             * Updates the total cost and end dates when a new date is chosen
             * @param row the current table row and if null clears the cell graphic
             */
            private void bindToRow(Row row) {
                if (row == null) { setGraphic(null); return; }

                if (boundRow == row) {
                    startBox.setItems(row.startList);
                    setGraphic(startBox);
                    return;
                }

                if (boundRow != null) {
                    startBox.valueProperty().unbindBidirectional(boundRow.startValue);
                }
                boundRow = row;
                startBox.setItems(row.startList);
                startBox.valueProperty().bindBidirectional(row.startValue);
                startBox.setOnAction(e -> {
                    buildEndList(row);
                    updateTotal();
                });
                setGraphic(startBox);
            }
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                Row row = (getTableRow() != null) ? getTableRow().getItem() : null;

                if (empty || row == null) {
                    if (boundRow != null) {
                        startBox.valueProperty().unbindBidirectional(boundRow.startValue);
                        boundRow = null;
                    }
                    setGraphic(null);
                }
                else
                {
                    bindToRow(row);
                }
            }

        });
        cStart.setCellValueFactory(p -> p.getValue().startValue);

        cEnd.setCellFactory(col -> new TableCell<Row, String>() {
            private final ComboBox<String> endBox = new ComboBox<>();
            private Row boundRow;
            { setContentDisplay(ContentDisplay.GRAPHIC_ONLY); }
            /**
             * Connects the Comboboxs to this rows end date
             * Updates the total cost when a new end date is chosen
             * @param row the current table row and if null clears the cell graphic
             */
            private void bindToRow(Row row) {
                if (row == null) { setGraphic(null); return; }

                if (boundRow == row) {
                    endBox.setItems(row.endList);
                    endBox.setDisable(row.endList.isEmpty());
                    setGraphic(endBox);
                    return;
                }
                if (boundRow != null) {
                    endBox.valueProperty().unbindBidirectional(boundRow.endValue);
                }
                boundRow = row;
                endBox.setItems(row.endList);
                endBox.setDisable(row.endList.isEmpty());
                endBox.valueProperty().bindBidirectional(row.endValue);
                endBox.setOnAction(e -> updateTotal());
                setGraphic(endBox);
            }
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                Row row = (getTableRow() != null) ? getTableRow().getItem() : null;
                if (empty || row == null) {
                    if (boundRow != null) {
                        endBox.valueProperty().unbindBidirectional(boundRow.endValue);
                        boundRow = null;
                    }
                    setGraphic(null);
                } else {
                    bindToRow(row);
                }
            }
        });

        cEnd.setCellValueFactory(p -> p.getValue().endValue);

        ObservableList<Row> rows = FXCollections.observableArrayList();
        for (cartItem item : currentCart.getItems()) {
            Row row = new Row(item);
            row.startList.setAll(findStartDates(item.selectedRoom.getID()));
            if (!row.startList.isEmpty()) {
                row.startValue.set(row.startList.get(0));
                buildEndList(row);
            }
            rows.add(row);
            row.startValue.addListener((o, ov, nv) -> { buildEndList(row); updateTotal(); });
            row.endValue.addListener((o, ov, nv) -> updateTotal());
        }
        roomsTable.setItems(rows);

        updateTotal();
    }
    /**
     * Finds all available start dates for the selected room given the baseYear
     * @param roomId The ID of the room
     * @return A list of start dates in iso format (YYYY-MM-DD)
     */
    private List<String> findStartDates(int roomId) {
        int[][][][] grid = roomCalendarMap.get(roomId);
        if (grid == null) return List.of();
        List<String> out = new ArrayList<>();
        for (int y = 0; y <= 1; y++) {
            int year = baseYear + y;
            for (int m = 0; m < 12; m++) {
                int dim = LocalDate.of(year, m + 1, 1).lengthOfMonth();
                for (int d = 0; d < dim; d++) {
                    if (grid[y][m][d][0] == 1) {
                        out.add(LocalDate.of(year, m + 1, d + 1).format(ISO));
                    }
                }
            }
        }
        return out.stream().distinct().sorted().toList();
    }
    /**
     * Builds the list of valid end dates based on the users selected start date
     * @param row The current table row where there end date options should be recalculated
     */
    private void buildEndList(Row row) {
        row.endList.clear();
        row.endValue.set(null);
        String startStr = row.startValue.get();
        if (startStr == null || startStr.isBlank()) return;
        LocalDate start = LocalDate.parse(startStr, ISO);
        int yIdx = start.getYear() - baseYear;
        int mIdx = start.getMonthValue() - 1;
        int dIdx = start.getDayOfMonth() - 1;
        if (yIdx < 0 || yIdx > 1) return;
        Calendar cal = new Calendar(row.cartItem.selectedRoom.getID());
        String[] raw = cal.getEndDates(yIdx, mIdx, dIdx);
        List<String> ends = new ArrayList<>();
        for (String s : raw) {
            if (s == null) break;
            LocalDate d = LocalDate.parse(s, ISO);
            if (d.isAfter(start)) ends.add(d.format(ISO));
        }
        row.endList.setAll(ends);
        if (!row.endList.isEmpty()) row.endValue.set(row.endList.get(0));
    }
    /**
     * Calculates the total price of all the rooms based on the selected start and end dates
     * @return The total price as a double
     */
    private double updateTotal() {
        double total = 0.0;
        reservation res = new reservation();

        if (roomsTable.getItems() != null) {
            for (Row row : roomsTable.getItems()) {
                String start = row.startValue.get();
                String end   = row.endValue.get();
                double price = row.roomPrice.get();

                int days = 1;
                if (start != null && end != null) {
                    try {
                        days = res.calcDays(start, end);
                        if (days < 1) days = 1;
                    } catch (Exception ex) {
                        days = 1;
                    }
                }
                total += price * days;
            }
        }

        if (cartTotalLabel != null) {
            cartTotalLabel.setText(String.format("$%.2f", total));
        }
        return total;
    }
    /**
     * Continues to the payment page when the user clicks the continue button
     */
    @FXML private void onContinueToPayment() {
        try {
            ObservableList<Row> rows = roomsTable.getItems();
            if (rows == null || rows.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Your cart is empty. Please add rooms first.").showAndWait();
                return;
            }

            reservation helper = new reservation();
            reservation[] reservations = new reservation[rows.size()];

            int i = 0;
            double grandTotal = 0.0;

            for (Row row : rows) {
                String start = row.startValue.get();
                String end   = row.endValue.get();

                int days = helper.calcDays(start, end);
                if (days < 1) days = 1;

                reservation r = new reservation();
                r.setCustomerID(currentCustomer.getID());
                r.setSelectedRoom(row.cartItem.selectedRoom);
                r.setStartDate(start);
                r.setEndDate(end);
                r.setTotal_days(days);
                r.setTotal_cost(helper.reservationCost(row.cartItem.selectedRoom, days));

                reservations[i++] = r;
                grandTotal += r.getTotal_cost();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            PaymentController controller = loader.getController();
            controller.initData(currentCustomer, reservations, grandTotal);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Payment");
            stage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "failed to open payment page").showAndWait();
        }
    }
}