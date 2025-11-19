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

public class reservationController {

    @FXML private TableView<Row> roomsTable;
    @FXML private TableColumn<Row, String> cHotel;
    @FXML private TableColumn<Row, String> cRoom;
    @FXML private TableColumn<Row, Number> cPrice;

    @FXML private TableColumn<Row, String> cStart;
    @FXML private TableColumn<Row, String> cEnd;
    @FXML private Label cartTotalLabel;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE;

    private customer currentCustomer;
    private cart currentCart;

    private final Map<Integer, int[][][][]> roomCalendarMap = new HashMap<>();
    private int baseYear;


    public static class Row {
        final cartItem cartItem;
        final StringProperty hotelName = new SimpleStringProperty();
        final StringProperty roomName  = new SimpleStringProperty();
        final DoubleProperty roomPrice = new SimpleDoubleProperty();
        final ObservableList<String> startList = FXCollections.observableArrayList();
        final ObservableList<String> endList   = FXCollections.observableArrayList();
        final StringProperty startValue = new SimpleStringProperty();
        final StringProperty endValue   = new SimpleStringProperty();

        Row(cartItem ci) {
            this.cartItem = ci;
            hotelName.set(ci.selectedHotel.getName());
            roomName.set(ci.selectedRoom.getName());
            roomPrice.set(ci.selectedRoom.getPrice());
        }
    }

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
                startBox.setOnAction(e -> buildEndList(row));
                startBox.setOnAction(e -> System.out.println("# of runs: "));
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
        }
        roomsTable.setItems(rows);

        if (cartTotalLabel != null) {
            cartTotalLabel.setText(String.format("$%.2f", currentCart.total()));
        }
    }

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
        System.out.println("im messing things up :(");
        if (!row.endList.isEmpty()) row.endValue.set(row.endList.get(0));
    }

    @FXML private void onContinueToPayment()
    {
        new Alert(Alert.AlertType.INFORMATION, "payemnt page coming soon ").showAndWait();
    }
}
