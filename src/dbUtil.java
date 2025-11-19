import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class dbUtil {




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















}

