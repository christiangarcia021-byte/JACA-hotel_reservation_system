import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * customer class handles retrieving and temporarily storing customer information in a session after sign-in.
 * It connects to a MySQL database to verify credentials and retrieves customer details.
 * It also has a method to print the customers information: ID, name, last name, birth year, address, and email.
 *  @author Andy Hernandez
 *  @version 1.0       Date: 10/21/2025
 */

public class customer {
    /**
     * ID - an int representing the unique identifier for the customer. Refers to a customer record in the database's CUSTOMER table.
     */
    private int ID;
    /**
     * name - a String representing the first name of the customer.
     */
    private String name;
    /**
     * lname - a String representing the last name of the customer.
     */
    private String lname;
    /**
     *  birthyear - a String representing the birth year of the customer.
     */
    private String birthyear;
    /**
     * address - a String representing the billing address of the customer.
     */
    private String address;
    /**
     * email - a String representing the email of the customer.
     */
    private String email;
    /**
     * SignedIn - a boolean indicating whether the customer is signed in or not.
     */
    private boolean SignedIn = false;

    MySQLConnection MyDB = new MySQLConnection();
    /**
     * customer constructor initializes a new customer object with default values (java).
     */
    public customer(){

    }

    /**
     * SignIn method attempts to sign in a customer using the provided email and password.
     * It connects to the MySQL database, executes a query to verify the credentials,
     * and retrieves the customer's information if the credentials are valid.
     * The customer object is then populated with the retrieved data, and the SignedIn flag is set to true.
     * @param email_input The email address provided by the customer for sign-in.
     * @param password_input The password provided by the customer for sign-in.
     * @return true if the sign-in is successful and the customer is authenticated; false otherwise
     */
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

    /**
     * showFile method prints the customer's information including ID, name, last name, birth year, address, and email to the console.
     */
    public void showFile(){
        System.out.printf("ID: %d, name: %s, lname: %s, year:%s, addr: %s, email: %s", ID, name, lname, birthyear, address, email );

    }
    /**
     * isSignedIn method returns the sign-in status of the customer.
     */
    public boolean isSignedIn() { return SignedIn; }
    /**
     * getName method returns the name of the customer.
     */
    public String getName() { return name; }
    /**
     * getEmail method returns the email of the customer.
     */
    public String getEmail() { return email; }
    /**
     *  getID method returns the unique identifier (ID) of the customer.
     */
    public int getID() { return ID; }




}
