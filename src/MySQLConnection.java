import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * MySQLConnection class manages the connection to a MySQL database and provides methods to interact with it.
 * It includes methods to print customer data, retrieve the current year, and get the current time from the database.
 * @author Christian Garcia
 * @version 1.0 Date: 10/22/2025
 */

public class MySQLConnection{
        public MySQLConnection(){

        }
    final String myURL = "jdbc:mysql://localhost:3306/hotel";
    final String myUSER = "root";
    final String myPASS = "20JacaDBHotel25";


        Connection con = null;
        Statement stat = null;

    /**
     * Prints all customer records from the database to the console.
     * Used for debugging to confirmthat database connection is working.
     */
    public void print() {
        try {
            con = DriverManager.getConnection(myURL, myUSER, myPASS);
            System.out.println("connected to the DB?");

            stat = con.createStatement();

            String sql = "SELECT * FROM customer";
            ResultSet result = stat.executeQuery(sql);

            while (result.next()) {
                int id = result.getInt("CUSTOMER_ID");
                String name = result.getString("CUSTOMER_EMAIL");
                System.out.println("ID: " + id + ", EMAIL: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) stat.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * Establishes and returns a connection to the MySQL database.
     * @return Connection object if successful, null otherwise
     */
    public Connection getConnection() {
        try {
            con = DriverManager.getConnection(myURL, myUSER, myPASS);
            return con;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Retrieves the current year from the database.
     * @return The current year as an integer
     */
    public int getYear() {
        int year = 0;
        try {
            con = DriverManager.getConnection(myURL, myUSER, myPASS);
            String sql = "SELECT YEAR(CURRENT_DATE()) AS current_year";
            stat = con.createStatement();
            ResultSet result = stat.executeQuery(sql);
            if (result.next()) {
                year = result.getInt("current_year");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) stat.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return year;
    }
    /**
     * Retrieves the current time from the database.
     * @return The current time as a String in HH:MM:SS format
     */
    public String getTime() {
        String time = "";
        try {
            con = DriverManager.getConnection(myURL, myUSER, myPASS);
            String sql = "SELECT CURRENT_TIME() AS current_time";
            stat = con.createStatement();
            ResultSet result = stat.executeQuery(sql);
            if (result.next()) {
                time = result.getString("current_time");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) stat.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
    }
}