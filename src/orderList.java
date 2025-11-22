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

                details currentDetail = null;
                while (results.next()) {
                    String oCode = results.getString("RES_ORDER_CODE");
                    String hotelId = results.getString("HOTEL_ID");
                    String roomId = results.getString("ROOM_ID");
                    String sDate = results.getString("SCHEDULED_DATE");
                    String eDate = results.getString("SCHEDULED_END_DATE");
                    int tDays = results.getInt("TOTAL_DAYS");
                    double rPrice = results.getDouble("PRICE_PAID");
                    String status = results.getString("RESERVATION_STATUS");

                    details newDetail = new details(
                            oCode,
                            dbUtil.getHotelName(Integer.parseInt(hotelId)),
                            dbUtil.getRoomName(Integer.parseInt(roomId)),
                            sDate,
                            eDate,
                            tDays,
                            rPrice,
                            status);  //end of detail obj initialization

                    if (currentOrder.getDetails() == null) {
                        currentOrder.setDetails(newDetail);
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

            details currentDetail = currentOrder.getDetails();
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







/**
 * order class represents a single order made by a customer.
 * It contains order details such as order code, date, time, total paid amount,
 * and a linked list of reservation details associated with the order.
 */
class order {



        private String orderCode;
        private String orderDate;
        private String orderTime;
        private double totalPaid;
        /** pointer to a list of reservation details associated with the order.
         */
        private details details;
        /** linked list pointer to the next order in the list.
         */
        private order next;

        /** Getter for the next order in the linked list.
         * @return the next order
         */
        public order getNext() {
            return next;
        }

        /** Constructor to initialize an order with given details.
         * @param oCode the order code
         * @param oDate the order date
         * @param oTime the order time
         * @param tPaid the total amount paid
         */
        public order(String oCode, String oDate, String oTime, double tPaid) {
            orderCode = oCode;
            orderDate = oDate;
            orderTime = oTime;
            totalPaid = tPaid;
            details = null;
        }

        /** Default constructor for an empty order.
         */
        public order(){

        }

    /** Setter for the next order in the linked list.
         * @param next the next order to set
         */
        public void setNext(order next) {
            this.next = next;
        }
        /** Setter for the reservation details associated with the order.
         * @param details the reservation details to set
         */
        public void setDetails(details details) {
            this.details = details;
        }

        public String getOrderCode() {
            return orderCode;
        }
        public String getOrderDate() {
            return orderDate;
        }
        public String getOrderTime() {
            return orderTime;
        }
        public double getTotalPaid() {
            return totalPaid;
        }
        /** Getter for the reservation details associated with the order.
         * @return the reservation details
         */
        public details getDetails() {
            return details;
        }
        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

    }

/**
 * the purpose of the class is to represent reservation details associated with an order.
 * It contains information such as hotel name, room name, start and end dates,
 * total days of stay, room price paid, and reservation status.
 * It also includes a linked list pointer to the next reservation detail.
 */
    class details {
       private String orderCode;
       private String hotelName;
       private String roomName;
       private String startDate;
       private String endDate;
       private int totalDays;
       private double roomPricePaid;
       private String status;
       /** linked list pointer to the next reservation detail.
        */
       private details next;

    /**
            * Constructor to initialize reservation details with given information.
            * @param oCode the order code
            * @param hName the hotel name
            * @param rName the room name
            * @param sDate the start date of the reservation
            * @param eDate the end date of the reservation
            * @param tDays the total days of stay
            * @param rPrice the room price paid
            * @param status the reservation status
     */
         public details(String oCode, String hName, String rName, String sDate, String eDate, int tDays, double rPrice, String status) {
             orderCode = oCode;
             hotelName = hName;
             roomName = rName;
             startDate = sDate;
             endDate = eDate;
             totalDays = tDays;
             roomPricePaid = rPrice;
             this.status = status;
         }
    /**
        * Setter for the next reservation detail in the linked list.
        * @param next the next reservation detail to set
     */
        public void setNext(details next) {
            this.next = next;
        }
    /**
        * Getter for the next reservation detail in the linked list.
        * @return the next reservation detail
     */
        public details getNext() {
            return next;
        }


        public String getOrderCode() {
            return orderCode;
        }
        public String getHotelName() {
            return hotelName;
        }
        public String getRoomName() {
            return roomName;
        }
        public String getStartDate() {
            return startDate;
        }
        public String getEndDate() {
            return endDate;
        }
        public int getTotalDays() {
            return totalDays;
        }
        public double getRoomPricePaid() {
            return roomPricePaid;
        }
        public String getStatus() {
            return status;
        }
}

