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
        private orderDetails orderDetails;
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
            orderDetails = null;
        }

        /** Default constructor for an empty order.
         */
        public order(){

        }

        public void printOrder(){
            System.out.println("Order Code: " + orderCode);
            System.out.println("Order Date: " + orderDate);
            System.out.println("Order Time: " + orderTime);
            System.out.println("Total Paid: $" + totalPaid);
            System.out.println("Reservation Details:");
            orderDetails currentDetail = orderDetails;
            while(currentDetail != null){
                System.out.println("  Hotel Name: " + currentDetail.getHotelName());
                System.out.println("  Room Name: " + currentDetail.getRoomName());
                System.out.println("  Start Date: " + currentDetail.getStartDate());
                System.out.println("  End Date: " + currentDetail.getEndDate());
                System.out.println("  Total Days: " + currentDetail.getTotalDays());
                System.out.println("  Room Price Paid: $" + currentDetail.getRoomPricePaid());
                System.out.println("  Status: " + currentDetail.getStatus());
                System.out.println("---------------------------");
                currentDetail = currentDetail.getNext();
            }
        }




    /** Setter for the next order in the linked list.
         * @param next the next order to set
         */
        public void setNext(order next) {
            this.next = next;
        }
        /** Setter for the reservation details associated with the order.
         * @param orderDetails the reservation details to set
         */
        public void setOrderDetails(orderDetails orderDetails) {
            this.orderDetails = orderDetails;
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
        public orderDetails getOrderDetails() {
            return orderDetails;
        }
        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }
}

