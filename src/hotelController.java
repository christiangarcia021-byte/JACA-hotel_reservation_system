import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * hotelController class manages a collection of hotel objects.
 * It provides methods to initialize hotels from a database and display their information.
 * @author Andy Hernandez
 * @version 1.0       Date: 10/22/2025
 */

public class hotelController {
    /**
     * MyHotels - an array[10] of hotel objects representing the hotels managed by the controller.
     */
    hotel MyHotels[] = new hotel[10];
    /**
     * totalHotels - an int representing the total number of hotels currently managed by the controller.
     */
    private int totalHotels = 0;
    /**
     * maxHotels - an int representing the maximum number of hotels that can be managed by the controller (set to 10).
     */
    private int maxHotels = 10;

    /**
     * getTotalHotels method retrieves the total number of hotels currently managed by the controller.
     * @return The total number of hotels as an int.
     */
    public int getTotalHotels() {
        return totalHotels;
    }

    /**
     * initHotels method initializes the MyHotels array by fetching hotel data from a MySQL database.
     * It connects to the database, executes a query to retrieve hotel IDs, and populates the MyHotels array with hotel objects.
     * Each hotel object is also initialized with its associated rooms.
     * @return true if the initialization is successful; false otherwise
     */
    public boolean initHotels(){
        try{
            MySQLConnection MyDB = new MySQLConnection();
            Connection con = MyDB.getConnection();
            PreparedStatement pstmt = null;
            String sql = "SELECT HOTEL_ID from hotel";
            pstmt = con.prepareStatement(sql);
            ResultSet resulth =  pstmt.executeQuery();
            int hotelCount = 0;
            int index = 0;
            while(resulth.next()){
                hotel h = new hotel(resulth.getInt("HOTEL_ID"));
                h.initRooms();
                MyHotels[index] = h;
                index++;
                hotelCount++;


            }
            totalHotels = hotelCount;
            if(con != null){ con.close(); }
            MyDB = null;
            if(pstmt != null){ pstmt.close(); }
            return true;
        } catch (Exception e) {

        }


        return false;
    }


    /**
     * printAllHotels method displays information for all hotels managed by the controller.
     * It iterates through the MyHotels array and calls the hotelInfo method for each hotel object.
     */
    public void printAllHotels(){
        for(int i = 0; i < totalHotels; i++){
            MyHotels[i].hotelInfo();
            System.out.println("\n\n===============================\n\n");
        }




    }
    /**
     * getMyHotels method retrieves the array of hotel objects managed by the controller.
     * @return An array of hotel objects.
     */
    public hotel[] getMyHotels() {
        return MyHotels;
    }
}


