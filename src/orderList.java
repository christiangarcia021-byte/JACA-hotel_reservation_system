import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class orderList {

    orderItem orderList = null;


    public orderList(customer customer) {
        getPastOrders(customer.getID());
    }

    public orderList(int custID) {
        getPastOrders(custID);
    }

    public orderList(){
        orderList = new orderItem();
    }


    public void refreshOrders(int custID) {
        orderList = null;
        getPastOrders(custID);
    }

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

           orderItem currentOrder = null;

           while (results.next()) {
               String oCode = results.getString("ORDER_CODE");
               String oDate = results.getString("ORDER_DATE");
               String oTime = results.getString("ORDER_TIME");
               double tPaid = results.getDouble("TOTAL_PAID");

               orderItem newOrder = new orderItem(oCode, oDate, oTime, tPaid);

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


    private void getReservationDetails() {
        MySQLConnection MyDB = new MySQLConnection();
        Connection con = null;
        PreparedStatement getDetails = null;
        ResultSet results = null;

        orderItem currentOrder = orderList;
        while (currentOrder != null) {
            try {
                con = MyDB.getConnection();
                String sql = "SELECT * FROM RESERVATIONS WHERE RES_ORDER_CODE = ?";
                getDetails = con.prepareStatement(sql);
                getDetails.setString(1, currentOrder.getOrderCode());
                results = getDetails.executeQuery();

                reservationDetails currentDetail = null;
                while (results.next()) {
                    String oCode = results.getString("RES_ORDER_CODE");
                    String hotelId = results.getString("HOTEL_ID");
                    String roomId = results.getString("ROOM_ID");
                    String sDate = results.getString("SCHEDULED_DATE");
                    String eDate = results.getString("SCHEDULED_END_DATE");
                    int tDays = results.getInt("TOTAL_DAYS");
                    double rPrice = results.getDouble("PRICE_PAID");
                    String status = results.getString("RESERVATION_STATUS");

                    reservationDetails newDetail = new reservationDetails(
                            oCode,
                            dbUtil.getHotelName(Integer.parseInt(hotelId)),
                            dbUtil.getRoomName(Integer.parseInt(roomId)),
                            sDate,
                            eDate,
                            tDays,
                            rPrice,
                            status);  //end of detail obj initialization

                    if (currentOrder.getReservationList() == null) {
                        currentOrder.setReservationList(newDetail);
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


    public void printOrderList() {
        orderItem currentOrder = orderList;

        while (currentOrder != null) {
            System.out.printf("Order Code: %s, Date: %s, Time: %s, Total Paid: %.2f\n",
                    currentOrder.getOrderCode(),
                    currentOrder.getOrderDate(),
                    currentOrder.getOrderTime(),
                    currentOrder.getTotalPaid());

            reservationDetails currentDetail = currentOrder.getReservationList();
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








class orderItem {



        private String orderCode;
        private String orderDate;
        private String orderTime;
        private double totalPaid;
        private reservationDetails reservationList;

        public orderItem getNext() {
            return next;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        private orderItem next;

        public orderItem(String oCode, String oDate, String oTime, double tPaid) {
            orderCode = oCode;
            orderDate = oDate;
            orderTime = oTime;
            totalPaid = tPaid;
            reservationList = null;
        }

        public orderItem(){

        }





        public void setNext(orderItem next) {
            this.next = next;
        }
        public void setReservationList(reservationDetails reservationList) {
            this.reservationList = reservationList;
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
        public reservationDetails getReservationList() {
            return reservationList;
        }

    }


    class reservationDetails {
       private String orderCode;
       private String hotelName;
       private String roomName;
       private String startDate;
       private String endDate;
       private int totalDays;
       private double roomPricePaid;
       private String status;
       private reservationDetails next;

         public reservationDetails(String oCode, String hName, String rName, String sDate, String eDate, int tDays, double rPrice, String status) {
             orderCode = oCode;
             hotelName = hName;
             roomName = rName;
             startDate = sDate;
             endDate = eDate;
             totalDays = tDays;
             roomPricePaid = rPrice;
             this.status = status;
         }

        public void setNext(reservationDetails next) {
            this.next = next;
        }
        public reservationDetails getNext() {
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

