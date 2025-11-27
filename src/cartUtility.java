import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cartUtility {

    public static String checkout(reservation[] reservations, customer cust, paymentInfo payment){

        int customerID = cust.getID();
        int paymentID = processPayment(payment, customerID);
        double totalPaid = 0.0;
        for(reservation resv : reservations) {
            totalPaid += resv.getTotal_cost();
        }
        generateOrder(customerID, totalPaid, paymentID);
        makeReservations(reservations, customerID , paymentID);
        return "Order Completed Successfully";

    }

    private static int processPayment(paymentInfo payment, int customerID){
        int paymentID = -1;

        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement insertPayment = null;
        ResultSet generatedKeys = null;

        try {
            con = MyDB.getConnection();
            String sql = "INSERT INTO PAYMENT_INFO (CUSTOMER_ID, CARD_NAME, CARD_NUMBER, CVV, EXPIRATION_MONTH, EXPIRATION_YEAR) VALUES (?, ?, ?, ?, ?, ?)";
            insertPayment = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertPayment.setInt(1, customerID);
            insertPayment.setString(2, payment.getCardName());
            insertPayment.setString(3, payment.getCardNumber());
            insertPayment.setString(4, payment.getCvv());
            insertPayment.setInt(5, payment.getExpMonth());
            insertPayment.setInt(6, payment.getExpYear());
            insertPayment.executeUpdate();
            generatedKeys = insertPayment.getGeneratedKeys();
            if (generatedKeys.next()) {
                paymentID = generatedKeys.getInt(1);
            }
            return paymentID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static String generateOrder(int customerID, double totalPaid, int paymentID){
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement newOrder = null;
        ResultSet results = null;
        String code = generateOrderCode(customerID);

        try{
            con = MyDB.getConnection();
            String sql = "iNSERT INTO orders (ORDER_CODE, CUSTOMER_ID, TOTAL_PAID, PAYMENT_ID) VALUES (?, ?, ?, ?)";
            newOrder = con.prepareStatement(sql);
            newOrder.setString(1, code);
            newOrder.setInt(2, customerID);
            newOrder.setDouble(3, totalPaid);
            newOrder.setInt(4, paymentID);
            newOrder.executeUpdate();
            return code;
         }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(newOrder != null){newOrder.close();}
                if(con != null){con.close();}
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private static void makeReservations(reservation[] reservations, int customerID, int paymentID){
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement newResv = null;

        try{
            con = MyDB.getConnection();
            String sql = "INSERT INTO reservation (CUSTOMER_ID, ROOM_ID, RESERVATION_STARTDATE, RESERVATION_ENDDATE, PAYMENT_ID) VALUES (?, ?, ?, ?, ?)";
            newResv = con.prepareStatement(sql);
            for(reservation resv : reservations) {
                newResv.setInt(1, customerID);
                newResv.setInt(2, resv.getRoom().getID());
                newResv.setString(3, resv.getStartDate());
                newResv.setString(4, resv.getEndDate());
                newResv.setInt(5, paymentID);
                newResv.executeUpdate();
            }
         }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(newResv != null){newResv.close();}
                if(con != null){con.close();}
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }


    }

    public static String generateOrderCode(int customerID) {
        return  "" + customerID + "" + System.currentTimeMillis();
    }






}
