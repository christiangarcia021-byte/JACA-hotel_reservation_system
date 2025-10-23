import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class customer {

    private int ID;
    private String name;
    private String lname;
    private String birthyear;
    private String address;
    private String email;
    private boolean SignedIn = false;

    MySQLConnection MyDB = new MySQLConnection();

    public customer(){

    }


    public boolean SignIn(String email_input, String password_input){
        Connection con = MyDB.getConnection();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        if(SignedIn == true){return true;}
        else {
            try {
                String query = "SELECT * FROM customer WHERE CUSTOMER_EMAIL = ? AND CUSTOMER_PASSWORD = ?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, email_input);
                pstmt.setString(2, password_input);
                result = pstmt.executeQuery();
                if(result.next()) {
                    ID = result.getInt("CUSTOMER_ID");
                    name = result.getString("CUSTOMER_NAME");
                    lname = result.getString("CUSTOMER_LNAME");
                    birthyear = result.getString("CUSTOMER_BIRTHYEAR");
                    address = result.getString("BILLING_ADDRESS");
                    email = result.getString("CUSTOMER_EMAIL");
                    SignedIn = true;
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (con != null) con.close();
                    if(result != null) result.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        return false;
    }

    public void showFile(){
        System.out.printf("ID: %d, name: %s, lname: %s, year:%s, addr: %s, email: %s", ID, name, lname, birthyear, address, email );

    }





}
