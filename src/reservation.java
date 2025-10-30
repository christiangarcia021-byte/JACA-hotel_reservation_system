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

    public double reservationCost(room selectedRoom, int total_days) {//method to return price for a reservation(1 room)
        return selectedRoom.getPrice() * total_days;
    }


}
