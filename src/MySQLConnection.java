import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class MySQLConnection{
        public MySQLConnection(){

        }
    final String myURL = "jdbc:mysql://localhost:3306/hotel";
    final String myUSER = "root";
    final String myPASS = "20JacaDBHotel25";


        Connection con = null;
        Statement stat = null;
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

    public Connection getConnection() {
        try {
            con = DriverManager.getConnection(myURL, myUSER, myPASS);
            return con;
        } catch (Exception e) {
            return null;
        }
    }


}