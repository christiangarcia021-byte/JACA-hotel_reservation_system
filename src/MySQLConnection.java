import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnection{
    private static final String URL =  "jdbc:mysql://localhost:3306/world";
    private static final String USER = "root";
    private static final String PASSWORD = "20JacaDBHotel25";

    public static void main(String[] args){
        Connection con = null;
        Statement stat = null;

        try{
            con = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("connected to the DB?");

            stat = con.createStatement();

            String sql = "SELECT * FROM city";
            ResultSet result = stat.executeQuery(sql);

            while(result.next()){
                int id = result.getInt("ID");
                String name = result.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        } finally{
            try {
                if (stat != null) stat.close();
                if (con != null) con.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}