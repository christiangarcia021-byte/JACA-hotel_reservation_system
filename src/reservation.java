import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 * reservation class represents a reservation entity with its details and methods to manage reservations.
 * It provides methods to check room availability, make reservations, calculate total days, and compute reservation costs.
 * @author Christian Garcia
 * @version 1.0
 * Date: 10/22/2025
 */
public class reservation {
    int customerID;
    int roomID;

    public room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    room selectedRoom;
    String startDate;
    String endDate;
    int total_days;
    double total_cost;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }



    /**
     * Default constructor for the reservation class.
     * Initializes a new reservation helper object.
     */
    public reservation(){

    }
    /**
     * Checks if a selected room is available for reservation between the specified start and end dates.
     * @param selectedRoom The room to check availability for.
     * @param startDate The start date of the desired reservation period (format: YYYY-MM-DD).
     * @param endDate The end date of the desired reservation period (format: YYYY-MM-DD).
     * @return true if the room is available; false otherwise.
     */
    public boolean isAvailable(room selectedRoom, String startDate, String endDate) {
        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement pstmt = null;
        Connection con = null;
        try{
            con = MyDB.getConnection();
            String sql = "SELECT * FROM reservation WHERE ROOM_ID = ? AND (RESERVATION_STARTDATE <= ? AND RESERVATION_ENDDATE >= ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, selectedRoom.getID());
            pstmt.setString(2, endDate);
            pstmt.setString(3, startDate);
            var result = pstmt.executeQuery();
            if(result.next()){
                return false; // Room is not available
            }
        }
        catch(Exception e){
            return false;
        }
        finally{
            if(pstmt != null){
                try{
                    pstmt.close();
                }
                catch(Exception e){

                }
            }
            if(con != null){
                try{
                    con.close();
                }
                catch(Exception e){

                }
            }
            MyDB = null;
        }
        return true; // Room is available
    }
    /**
     * Makes a reservation for a customer in a selected room between the specified start and end dates.
     * @param customerID The ID of the customer making the reservation.
     * @param selectedRoom The room to be reserved.
     * @param startDate The start date of the reservation (format: YYYY-MM-DD).
     * @param endDate The end date of the reservation (format: YYYY-MM-DD).
     * @return true if the reservation is successful; false otherwise.
     */
    public boolean makeReservation(int customerID, room selectedRoom, String startDate, String endDate) {
        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement pstmt = null;
        Connection con = null;

        try{
            con = MyDB.getConnection();
            String sql = "INSERT INTO reservation (CUSTOMER_ID, ROOM_ID, RESERVATION_STARTDATE, RESERVATION_ENDDATE) VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, selectedRoom.getID());
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);
            pstmt.executeUpdate();

        }
        catch(Exception e){
            return false;

        }
        finally{
            if(pstmt != null){
                try{
                    pstmt.close();
                }
                catch(Exception e){

                }
            }
            if(con != null){
                try{
                    con.close();
                }
                catch(Exception e){

                }
            }
            MyDB = null;
        }
        return true; // Return true if reservation is successful
    }




    /**
     * Calculates the total number of days between two dates using MySQL's DATEDIFF function.
     * @param startDate The start date (format: YYYY-MM-DD).
     * @param endDate The end date (format: YYYY-MM-DD).
     * @return The total number of days between the two dates; -1 if an error occurs; -2 for exceptions.
     */
    public int calcDays(String startDate, String endDate) { //Method to calculate total days between 2 dates (uses MySQL DATEDIFF function)
        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement pstmt = null;
        Connection con = null;
        try{
            con = MyDB.getConnection();
            String sql = "SELECT DATEDIFF(?, ?) AS total_days";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, endDate);
            pstmt.setString(2, startDate);
            var result = pstmt.executeQuery();
            if(result.next()){
                return result.getInt("total_days");
            }
        }
        catch(Exception e){
            return -2;
        }
        finally{//Final code to close connections and delete MyDB object
            if(pstmt != null){
                try{
                    pstmt.close();
                }
                catch(Exception e){

                }
            }
            if(con != null){
                try{
                    con.close();
                }
                catch(Exception e){

                }
            }
            MyDB = null;
        }
        return -1;
    }
    /**
     * Calculates the total cost of a reservation for a selected room over a specified number of days.
     * @param selectedRoom The room for which the reservation cost is to be calculated.
     * @param total_days The total number of days for the reservation.
     * @return The total cost of the reservation.
     */
    public double reservationCost(room selectedRoom, int total_days) {//method to return price for a reservation(1 room)
        return selectedRoom.getPrice() * total_days;
    }
    /**
     * Generates a unique order code for a reservation based on the customer ID and the current system time in milliseconds.
     * @param customerID The ID of the customer making the reservation.
     * @return A unique order code as a String.
     */




}
