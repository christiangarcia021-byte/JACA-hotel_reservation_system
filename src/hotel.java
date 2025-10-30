import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class hotel {

    private int ID;
    private String name;
    private String address;
    private int phone;
    private String email;
    private int zipcode;
    private room[] rooms = new room[100];
    private int totalRooms = 0;
    private int maxRooms = 100;


    public room getRoom(int index){
        if(index >= 0 && index < totalRooms){
            return rooms[index];
        } else {
            return null;
        }
    }





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
                    phone = resulth.getInt("HOTEL_PHONE");
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





    public int getID() { return ID; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getPhone() { return phone; }
    public  String getEmail() { return email; }
    public int getZipcode() { return zipcode; }
    public int  getTotalRooms() { return totalRooms; }
    public int getMaxRooms() { return maxRooms; }
    public room[] getRooms() { return rooms; }





}



