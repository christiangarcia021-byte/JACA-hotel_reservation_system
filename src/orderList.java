import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * orderList class holds a list of orders. to manage and retrieve past orders for a customer.
 * It connects to a MySQL database to fetch order and reservation details. When given a customer object or customer ID,
 * it retrieves all past orders associated with that customer, along with their reservation details.
 * The class provides methods to refresh the order list and print the details of all orders and their reservations.
 * @version 1.0     11/18/2025
 * @author Andy Hernandez
 */
public class orderList {
    /**
     * head of the linked list of orders.
     */
    order orderList = null;

    /**
     * Constructor that initializes the order list for a given customer.
     * @param customer the customer whose past orders are to be retrieved.
     */
    public orderList(customer customer) {
        getPastOrders(customer.getID());
    }

    /**
     * Constructor that initializes the order list for a given customer ID.
     * @param custID the ID of the customer whose past orders are to be retrieved.
     */
    public orderList(int custID) {
        getPastOrders(custID);
    }

    /**
     * Default constructor initializing an empty order list. For manual manipulation in say a CART
     */
    public orderList(){
        orderList = new order();
    }

    /**
     * Refreshes the order list for a given customer ID by clearing the current list and fetching the latest orders from the database.
     * @param custID the ID of the customer whose past orders are to be refreshed.
     */
    public void refreshOrders(int custID) {
        orderList = null;
        getPastOrders(custID);
    }

    /**
     * Retrieves past orders for a given customer ID from the database and populates the order list.
     * Initializes the linked list of orders. At the end calls getReservationDetails to populate reservation details for each order.
     * @param custID the ID of the customer whose past orders are to be retrieved.
     */
   private void getPastOrders(int custID) {
       MySQLConnection MyDB = new MySQLConnection();
       Connection con = null;
       PreparedStatement getOrders = null;
       ResultSet results = null;

       try {
           con = MyDB.getConnection();
           String sql = "SELECT * FROM orders WHERE CUSTOMER_ID = ?";
           getOrders = con.prepareStatement(sql);
           getOrders.setInt(1, custID);
           results = getOrders.executeQuery();

           order currentOrder = null;

           while (results.next()) {
               String oCode = results.getString("ORDER_CODE");
               String oDate = results.getString("ORDER_DATE");
               String oTime = results.getString("ORDER_TIME");
               double tPaid = results.getDouble("TOTAL_PAID");

               order newOrder = new order(oCode, oDate, oTime, tPaid);

               if (orderList == null) {
                   orderList = newOrder;
               } else {
                     currentOrder.setNext(newOrder);
               }
               currentOrder = newOrder;
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {
               if (results != null) results.close();
               if (getOrders != null) getOrders.close();
               if (con != null) con.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       getReservationDetails();
   }

    /**
     * Retrieves reservation details for each order in the order list from the database and populates the details list for each order.
     * Iterates through each order and fetches associated reservation details, linking them to the respective order.
     * Uses dbUtil to get hotel and room names based on their IDs.
     */
    private void getReservationDetails() {
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement getDetails = null;
        ResultSet results = null;

        order currentOrder = orderList;
        while (currentOrder != null) {
            try {
                con = MyDB.getConnection();
                String sql = "SELECT * FROM RESERVATIONS WHERE RES_ORDER_CODE = ?";
                getDetails = con.prepareStatement(sql);
                getDetails.setString(1, currentOrder.getOrderCode());
                results = getDetails.executeQuery();

                orderDetails currentDetail = null;
                while (results.next()) {
                    String oCode = results.getString("RES_ORDER_CODE");
                    String hotelId = results.getString("HOTEL_ID");
                    String roomId = results.getString("ROOM_ID");
                    String sDate = results.getString("SCHEDULED_DATE");
                    String eDate = results.getString("SCHEDULED_END_DATE");
                    int tDays = results.getInt("TOTAL_DAYS");
                    double rPrice = results.getDouble("PRICE_PAID");
                    String status = results.getString("RESERVATION_STATUS");

                    orderDetails newDetail = new orderDetails(
                            oCode,
                            dbUtil.getHotelName(Integer.parseInt(hotelId)),
                            dbUtil.getRoomName(Integer.parseInt(roomId)),
                            sDate,
                            eDate,
                            tDays,
                            rPrice,
                            status);  //end of detail obj initialization

                    if (currentOrder.getOrderDetails() == null) {
                        currentOrder.setOrderDetails(newDetail);
                    } else {
                        currentDetail.setNext(newDetail);
                    }
                    currentDetail = newDetail;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (results != null) results.close();
                    if (getDetails != null) getDetails.close();
                    if (con != null) con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            currentOrder = currentOrder.getNext();
        }
    }

    /**
     * For an orderList: Prints the details of all orders and their associated reservation details to the console.
     */
    public void printOrderList() {
        order currentOrder = orderList;

        while (currentOrder != null) {
            System.out.printf("Order Code: %s, Date: %s, Time: %s, Total Paid: %.2f\n",
                    currentOrder.getOrderCode(),
                    currentOrder.getOrderDate(),
                    currentOrder.getOrderTime(),
                    currentOrder.getTotalPaid());

            orderDetails currentDetail = currentOrder.getOrderDetails();
            while (currentDetail != null) {
                System.out.printf("\tHotel: %s, Room: %s, Start Date: %s, End Date: %s, Total Days: %d, Room Price Paid: %.2f, Status: %s\n",
                        currentDetail.getHotelName(),
                        currentDetail.getRoomName(),
                        currentDetail.getStartDate(),
                        currentDetail.getEndDate(),
                        currentDetail.getTotalDays(),
                        currentDetail.getRoomPricePaid(),
                        currentDetail.getStatus());
                currentDetail = currentDetail.getNext();
            }
            currentOrder = currentOrder.getNext();
        }
    }

}






