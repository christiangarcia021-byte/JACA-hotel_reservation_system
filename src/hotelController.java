import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class hotelController {
    hotel MyHotels[] = new hotel[10];
    private int totalHotels = 0;
    private int maxHotels = 10;

    public int getTotalHotels() {
        return totalHotels;
    }

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



    public void printAllHotels(){
        for(int i = 0; i < totalHotels; i++){
            MyHotels[i].hotelInfo();
            System.out.println("\n\n===============================\n\n");
        }




    }




}


