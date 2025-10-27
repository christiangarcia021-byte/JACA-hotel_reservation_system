import java.sql.Connection;
import java.sql.PreparedStatement;

public class reservation {
    int customerID;
    int roomID;
    String startDate;
    String endDate;
    String total_days;

    public reservation(){

    }


    public boolean makeReservation(int customerID, room selectedRoom, String startDate, String endDate) {






        return true; // Return true if reservation is successful
    }

    public int calcDays(String startDate, String endDate) {
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

    public double reservationCost(room selectedRoom, int total_days) {
        return selectedRoom.getPrice() * total_days;
    }


}
