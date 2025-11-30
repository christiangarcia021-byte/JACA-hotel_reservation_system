import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * dbUtil class provides utility methods to interact with the database for retrieving things based on IDs
 * because these methods may need to be accessed from various parts of the application, they are implemented as static methods.
 * It connects to a MySQL database to fetch the required information.
 * currently includes methods to get hotel names and room names based on their respective IDs.
 *  @author Andy Hernandez
 *  @version 1.0       Date: 11/18/2025
 */
public class dbUtil {



    /**
     * * getHotelName method retrieves the name of a hotel based on its ID.
     * It connects to the MySQL database, executes a query to fetch the hotel name,
     * and returns the name as a String.
     * @param hotelID The ID of the hotel for which the name is to be retrieved.
     * @return The name of the hotel as a String. If the hotel is not found, an empty string is returned.
     */
    public static String getHotelName(int hotelID) {
        String hotelName = "";
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement getHotel = null;
        ResultSet results = null;

        try {
            con = MyDB.getConnection();
            String sql = "SELECT HOTEL_NAME FROM hotel WHERE HOTEL_ID = ?";
            getHotel = con.prepareStatement(sql);
            getHotel.setInt(1, hotelID);
            results = getHotel.executeQuery();

            if (results.next()) {
                hotelName = results.getString("HOTEL_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotelName;
    }

    /**
     * * getRoomName method retrieves the name of a hotel room based on its ID.
     * It connects to the MySQL database, executes a query to fetch the room name,
     * and returns the name as a String.
     * @param roomID The ID of the room for which the name is to be retrieved.
     * @return The name of the room as a String. If the room is not found, an empty string is returned.
     */
    public static String getRoomName(int roomID) {
        String roomName = "";
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement getRoom = null;
        ResultSet results = null;

        try {
            con = MyDB.getConnection();
            String sql = "SELECT ROOM_NAME FROM hotel_rooms WHERE ROOM_ID = ?";
            getRoom = con.prepareStatement(sql);
            getRoom.setInt(1, roomID);
            results = getRoom.executeQuery();

            if (results.next()) {
                roomName = results.getString("ROOM_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return roomName;
    }


    public static String getCurrentTime() {
        String currentTime = "";
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet results = null;

        try {
            con = MyDB.getConnection();
            String sql = "SELECT DATE_FORMAT(NOW(), '%H:%i');";
            pstmt = con.prepareStatement(sql);
            results = pstmt.executeQuery();

            if (results.next()) {
                currentTime = results.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (results != null) results.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currentTime;
    }

    public static String getCurrentDate() {
        String currentDate = "";
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet results = null;

        try {
            con = MyDB.getConnection();
            String sql = "SELECT CURDATE();";
            pstmt = con.prepareStatement(sql);
            results = pstmt.executeQuery();

            if (results.next()) {
                currentDate = results.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (results != null) results.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currentDate;
    }




}

