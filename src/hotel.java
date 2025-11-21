import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
    * hotel class represents a hotel entity with its details and associated rooms.
     * It provides methods to initialize room data from a database and display hotel information.
 *   @author Andy Hernandez
    *  @version 1.0       Date: 10/22/2025
 */
public class hotel {

    /**
     ID - an int representing the unique identifier for the hotel. Refers to a hotel record in the database's HOTEL table.
     */
    private int ID;
    /**
     * name - a String representing the name of the hotel.
     */
    private String name;
    /**
     * address - a String representing the address of the hotel.
     */
    private String address;
    /**
     * phone - a String representing the phone number of the hotel.
     */
    private String phone;
    /**
     * email - a String representing the email address of the hotel.
     */
    private String email;
    /**
     * zipcode - an int representing the zipcode of the hotel.
     */
    private int zipcode;
    /**
     * rooms - an array[100] of room objects representing the rooms associated with the hotel.
     */
    private room[] rooms = new room[100];
    /**
     * totalRooms - an int representing the total number of rooms currently associated with the hotel.
     */
    private int totalRooms = 0;
    /**
     * maxRooms - an int representing the maximum number of rooms that can be associated with the hotel (set to 100).
     */
    private int maxRooms = 100;


    /**
     * getRoom method retrieves a room object from the hotel's rooms array based on the provided index.
     * @param index The index of the room to be retrieved from the rooms array.
     * @return The room object at the specified index if the index is valid; null otherwise
     */
    public room getRoom(int index){
        if(index >= 0 && index < totalRooms){
            return rooms[index];
        } else {
            return null;
        }
    }

    /**
     * hotel constructor initializes a new hotel object by fetching its details from the database using the provided hotel ID.
     * It connects to the MySQL database, executes a query to retrieve the hotel information,
     * and populates the hotel's attributes with the retrieved data.
     * @param id The unique identifier of the hotel to be fetched from the database.
     */
    public hotel(int id){
        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement pstmt = null;
        try{
            Connection con = MyDB.getConnection();

            String sql = "SELECT * FROM hotel WHERE HOTEL_ID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet resulth =  pstmt.executeQuery();
                if(resulth.next()){
                    ID = resulth.getInt("HOTEL_ID");
                    name = resulth.getString("HOTEL_NAME");
                    address = resulth.getString("HOTEL_ADDRESS");
                    phone = resulth.getString("HOTEL_PHONE");
                    email = resulth.getString("HOTEL_EMAIL");
                    zipcode = resulth.getInt("HOTEL_ZIPCODE");
                }
            if(con != null){ con.close(); }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            MyDB = null;
            try {
               if(pstmt != null) pstmt.close();
            } catch (Exception e) {

            }

        }

    }
    /**
     * hotelInfo method displays the hotel's information, including its ID, name, address, phone number, email, zipcode,
     * and details of each room associated with the hotel by iterating through the rooms array and calling the printRoomInfo method for each room.
     */

    public void hotelInfo(){
        System.out.println("Hotel ID: " + ID);
        System.out.println("Hotel Name: " + name);
        System.out.println("Hotel Address: " + address);
        System.out.println("Hotel Phone: " + phone);
        System.out.println("Hotel Email: " + email);
        System.out.println("Hotel Zipcode: " + zipcode);
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("\nRooms Information for Hotel with ID: " + ID + "\n");

        for(int i = 0; i < totalRooms; i++){
            rooms[i].printRoomInfo();
            System.out.println("-----------------------");
        }


    }

    /**
     * initRooms method initializes the rooms array of the hotel by fetching room data from the database.
     * It connects to the MySQL database, executes a query to retrieve room information for the hotel,
     * and populates the rooms array with room objects created from the retrieved data.
     * @return true if the rooms are successfully initialized; false otherwise.
     */

    public boolean initRooms() {

        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement pstmt = null;
        try {
            Connection con = MyDB.getConnection();

            String sql = "SELECT * FROM HOTEL_ROOMS WHERE HOTEL_ID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ID);
            ResultSet resultr = pstmt.executeQuery();
            int index = 0;
            while (resultr.next()) {
                    room r = new room();
                    r.setHOTEL_ID(resultr.getInt("HOTEL_ID"));
                    r.setID(resultr.getInt("ROOM_ID"));
                    r.setName(resultr.getString("ROOM_NAME"));
                    r.setFloor(resultr.getInt("ROOM_FLOOR"));
                    r.setBeds(resultr.getInt("ROOM_BEDS"));
                    r.setBathrooms(resultr.getInt("ROOM_BATHROOMS"));
                    r.setBedrooms(resultr.getInt("ROOM_BEDROOMS"));
                    r.setType(resultr.getString("ROOM_TYPE"));
                    r.setPrice(resultr.getDouble("ROOM_PRICE"));
                    r.setDescription(resultr.getString("ROOM_DESCRIPTION"));
                    r.setStatus(resultr.getString("ROOM_STATUS"));
                    rooms[index] = r;
                    totalRooms++;
                    index++;
            }
            if(con != null) con.close();
            return true;
        } catch (Exception e) {

        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if(MyDB != null) MyDB = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }




    /**
     * getID method retrieves the unique identifier of the hotel.
     * @return The unique identifier (ID) of the hotel as an int.
     */
    public int getID() { return ID; }
    /**
     * getName method retrieves the name of the hotel.
     * @return The name of the hotel as a String.
     */
    public String getName() { return name; }
    /**
     * getAddress method retrieves the address of the hotel.
     * @return The address of the hotel as a String.
     */
    public String getAddress() { return address; }
    /**
     * getPhone method retrieves the phone number of the hotel.
     * @return The phone number of the hotel as a String.
     */
    public String getPhone() { return phone; }
    /**
     * getEmail method retrieves the email address of the hotel.
     * @return The email address of the hotel as a String.
     */
    public  String getEmail() { return email; }
    /**
     * getZipcode method retrieves the zipcode of the hotel.
     * @return The zipcode of the hotel as an int.
     */
    public int getZipcode() { return zipcode; }
    /**
     * getTotalRooms method retrieves the total number of rooms associated with the hotel.
     * @return The total number of rooms as an int.
     */
    public int  getTotalRooms() { return totalRooms; }
    /**
     * getMaxRooms method retrieves the maximum number of rooms that can be associated with the hotel.
     * @return The maximum number of rooms as an int.
     */
    public int getMaxRooms() { return maxRooms; }
    /**
     * getRooms method retrieves the array of room objects associated with the hotel.
     * @return An array of room objects representing the rooms of the hotel.
     */
    public room[] getRooms() { return rooms; }





}



