import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class managerReportController implements Initializable{

    @FXML private TableView<ReservationRow> reservationTable;
    @FXML private TableColumn<ReservationRow, String> cOrderCode;
    @FXML private TableColumn<ReservationRow, String> cHotelName;
    @FXML private TableColumn<ReservationRow, String> cRoomName;
    @FXML private TableColumn<ReservationRow, Integer> cRoomFloor;
    @FXML private TableColumn<ReservationRow, String> cStartDate;
    @FXML private TableColumn<ReservationRow, String> cEndDate;
    @FXML private TableColumn<ReservationRow, Integer> cTotalDays;
    @FXML private TableColumn<ReservationRow, String> cRoomPricePaid;
    @FXML private TableColumn<ReservationRow, String> cStatus;
    @FXML private Label summaryLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    //@FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> statusFilterCombo;

    private final ObservableList<ReservationRow> reservationData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cOrderCode.setCellValueFactory(new PropertyValueFactory<>("orderCode"));
        cHotelName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        cRoomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        cRoomFloor.setCellValueFactory(new PropertyValueFactory<>("roomFloor"));
        cStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        cEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        cTotalDays.setCellValueFactory(new PropertyValueFactory<>("totalDays"));
        cRoomPricePaid.setCellValueFactory(new PropertyValueFactory<>("roomPricePaid"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //if(statusFilterCombo != null)
            statusFilterCombo.getItems().addAll(
                    "All",
                         "pending",
                         "checked in",
                         "checked out",
                         "cancelled",
                         "confirmed");
            statusFilterCombo.setValue("All");


        loadReservationData();

    }

    private void loadReservationData(){
        MySQLConnection myDB = new MySQLConnection();
        Connection con = myDB.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        reservationData.clear();

        try{
            con = myDB.getConnection();

            StringBuilder sql= new StringBuilder(
                    "SELECT r.RES_ORDER_CODE   AS orderCode," +
                    " h.HOTEL_NAME                  AS hotelName," +
                    " hr.ROOM_NAME                  AS roomName," +
                    " hr.ROOM_FLOOR                 AS roomFloor," +
                    " r.SCHEDULED_DATE              AS startDate," +
                    " r.SCHEDULED_END_DATE          AS endDate," +
                    " r.TOTAL_DAYS                  AS totalDays," +
                    " r.PRICE_PAID                  AS roomPricePaid," +
                    " r.RESERVATION_STATUS          AS status " +
                    "FROM RESERVATIONS r " +
                    "JOIN HOTEL_ROOMS hr ON r.ROOM_ID = hr.ROOM_ID " +
                    "JOIN HOTEL h ON r.HOTEL_ID = h.HOTEL_ID "+
                    "WHERE 1=1 ");

            LocalDate start = (startDatePicker != null) ? startDatePicker.getValue(): null;
            LocalDate end = (endDatePicker != null) ? endDatePicker.getValue(): null;
            String status = (statusFilterCombo != null) ? statusFilterCombo.getValue() : null;

            if (start != null) {
                sql.append(" AND r.SCHEDULED_DATE >= ? ");
            }
            if (end != null) {
                sql.append(" AND r.SCHEDULED_END_DATE <= ? ");
            }
            if (status != null && !status.equals("All")) {
                sql.append(" AND r.RESERVATION_STATUS = ? ");
            }
            sql.append(" ORDER BY r.SCHEDULED_DATE");


            pstmt = con.prepareStatement(sql.toString());
            int index = 1;
            if (start != null) {
                pstmt.setDate(index++, java.sql.Date.valueOf(start));
            }
            if (end != null) {
                pstmt.setDate(index++, java.sql.Date.valueOf(end));
            }
            if (status != null && !status.equals("All")) {
                pstmt.setString(index++, status);
            }
            rs = pstmt.executeQuery();

            int count = 0;
            double totalRevenue = 0.0;

            System.out.println("=== Manager Report: rows from DB ===");

            while(rs.next()){
//                rs.getString("orderCode");
//                rs.getString("hotelName");
//                rs.getString("roomName");
//                rs.getInt("roomFloor");
//                rs.getString("startDate");
//                rs.getString("endDate");
//                rs.getInt("totalDays");
//                String.format("%.2f", rs.getDouble("roomPricedPaid"));
//                rs.getString("status");
                String orderCode        = rs.getString("orderCode");
                String hotelName        = rs.getString("hotelName");
                String roomName         = rs.getString("roomName");
                int roomFloor           = rs.getInt("roomFloor");
                String startDate        = rs.getString("startDate");
                String endDate          = rs.getString("endDate");
                int totalDays           = rs.getInt("totalDays");
                double pricePaidVal     = rs.getDouble("roomPricePaid");
                String roomPricePaid    = rs.getString("roomPricePaid");
                String statusStr           = rs.getString("status");

                ReservationRow row = new ReservationRow(orderCode, hotelName, roomName, roomFloor,
                        startDate, endDate, totalDays,
                        roomPricePaid, statusStr);

                reservationData.add(row);
                count++;
                totalRevenue += pricePaidVal;
            }
            System.out.println("=== Total rows loaded: " + count + " ===");
            reservationTable.setItems(reservationData);

            if (summaryLabel != null){
                summaryLabel.setText(
                        String.format("Total Reservations: %d | Total revenue: $%.2f",
                                count, totalRevenue)
                );
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load reservation data").showAndWait();

        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { /* ignored */ }
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { /* ignored */ }
            try { if (con != null) con.close(); } catch (Exception e) { /* ignored */ }
        }

    }
    public static class ReservationRow {
        private final StringProperty orderCode;
        private final StringProperty hotelName;
        private final StringProperty roomName;
        private final IntegerProperty roomFloor;
        private final StringProperty startDate;
        private final StringProperty endDate;
        private final IntegerProperty totalDays;
        private final StringProperty roomPricePaid;
        private final StringProperty status;

        public ReservationRow(String orderCode, String hotelName, String roomName, int roomFloor,
                              String startDate, String endDate, int totalDays,
                              String roomPricePaid, String status) {
            this.orderCode = new SimpleStringProperty(orderCode);
            this.hotelName = new SimpleStringProperty(hotelName);
            this.roomName = new SimpleStringProperty(roomName);
            this.roomFloor = new SimpleIntegerProperty(roomFloor);
            this.startDate = new SimpleStringProperty(startDate);
            this.endDate = new SimpleStringProperty(endDate);
            this.totalDays = new SimpleIntegerProperty(totalDays);
            this.roomPricePaid = new SimpleStringProperty(roomPricePaid);
            this.status = new SimpleStringProperty(status);
        }

        public StringProperty orderCodeProperty() { return orderCode; }
        public StringProperty hotelNameProperty() { return hotelName; }
        public StringProperty roomNameProperty() { return roomName; }
        public IntegerProperty roomFloorProperty() { return roomFloor; }
        public StringProperty startDateProperty() { return startDate; }
        public StringProperty endDateProperty() { return endDate; }
        public IntegerProperty totalDaysProperty() { return totalDays; }
        public StringProperty roomPricePaidProperty() { return roomPricePaid; }
        public StringProperty statusProperty() { return status; }
    }
    @FXML
    private void applyFilters() {
        loadReservationData();
    }
    @FXML
    private void resetFilters() {
        if (startDatePicker != null) {
            startDatePicker.setValue(null);
        }
        if (endDatePicker != null) {
            endDatePicker.setValue(null);
        }
        if (statusFilterCombo != null) {
            statusFilterCombo.setValue("All");
        }
        loadReservationData();
    }



}
