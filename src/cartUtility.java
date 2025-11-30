import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cartUtility {

    public static order checkout(reservation[] reservations, customer cust, paymentInfo payment){

        order reciept = new order();
        int customerID = cust.getID();
        int paymentID = processPayment(payment, customerID);
        if(paymentID < 0){
            return null;
        }
        double totalPaid = 0.0;
        for(reservation resv : reservations) {
            totalPaid += resv.getTotal_cost();
        }
        String generatedCode = generateOrder(customerID, totalPaid, paymentID);
        makeReservations(reservations, customerID , paymentID, generatedCode, reciept);

        reciept.setOrderCode(generatedCode);
        reciept.setTotalPaid(totalPaid);
        reciept.setOrderDate(dbUtil.getCurrentDate());
        reciept.setOrderTime(dbUtil.getCurrentTime());

        return reciept;

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
            insertPayment.setString(2, payment.getCardHolderName());
            insertPayment.setString(3, payment.getCardNumber());
            insertPayment.setString(4, payment.getCvv());
            insertPayment.setInt(5, payment.getExpiryMonth());
            insertPayment.setInt(6, payment.getExpiryYear());
            insertPayment.executeUpdate();
            generatedKeys = insertPayment.getGeneratedKeys();
            if (generatedKeys.next()) {
                paymentID = generatedKeys.getInt(1);
            }
            System.out.println("Process payment successful" + "Payment ID: " + paymentID);
            return paymentID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -2;
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
            System.out.println("Order Generated Successfully");
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
        return("generating order code failed");
    }

    private static void makeReservations(reservation[] reservations, int customerID, int paymentID, String code, order reciept){
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement newResv = null;

        try{
            con = MyDB.getConnection();
            String sql = "INSERT INTO reservations (ROOM_ID, CUSTOMER_ID, HOTEL_ID, RES_ORDER_CODE, PRICE_PAID, SCHEDULED_DATE, SCHEDULED_END_DATE, TOTAL_DAYS, PAYMENT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            newResv = con.prepareStatement(sql);
            orderDetails firstDetail = null;
            orderDetails currentDetail = null;
            for(reservation resv : reservations) {
                if(firstDetail == null) {
                    firstDetail = new orderDetails();
                    currentDetail = firstDetail;
                    System.out.println("Created new order First detail node");
                }
                else{
                    currentDetail.setNext(new orderDetails());
                    currentDetail = currentDetail.getNext();
                    System.out.println("Created new order detail node");
                }

                newResv.setInt(1, resv.getSelectedRoom().getID());
                newResv.setInt(2, customerID);
                newResv.setInt(3, resv.getSelectedRoom().getHOTEL_ID());
                newResv.setString(4, code);
                newResv.setDouble(5, resv.getTotal_cost());
                newResv.setString(6, resv.getStartDate());
                newResv.setString(7, resv.getEndDate());
                newResv.setInt(8, resv.getTotal_days());
                newResv.setInt(9, paymentID);
                newResv.executeUpdate();
                System.out.println("Reservation made for room ID: " + resv.getSelectedRoom().getID());

                // Add reservation details to the order receipt
                currentDetail.setOrderCode(code);
                currentDetail.setHotelName(dbUtil.getHotelName(resv.getSelectedRoom().getHOTEL_ID()));
                currentDetail.setRoomName(dbUtil.getRoomName(resv.getSelectedRoom().getID()));
                currentDetail.setStartDate(resv.getStartDate());
                currentDetail.setEndDate(resv.getEndDate());
                currentDetail.setTotalDays(resv.calcDays(resv.getStartDate(), resv.getEndDate()));
                currentDetail.setRoomPricePaid(resv.getTotal_cost());
                currentDetail.setStatus("Confirmed");
            }
            reciept.setOrderDetails(firstDetail);
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
