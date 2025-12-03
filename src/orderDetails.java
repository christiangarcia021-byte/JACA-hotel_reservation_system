/**
 * the purpose of the class is to represent reservation details associated with an order.
 * It contains information such as hotel name, room name, start and end dates,
 * total days of stay, room price paid, and reservation status.
 * It also includes a linked list pointer to the next reservation detail.
 */
    class orderDetails {
       private String orderCode;
       private String hotelName;
       private String roomName;
       private int roomFloor;
       private String startDate;
       private String endDate;
       private int totalDays;
       private double roomPricePaid;
       private String status;
       /** linked list pointer to the next reservation detail.
        */
       private orderDetails next;

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
         public orderDetails(String oCode, String hName, String rName, String sDate, String eDate, int tDays, double rPrice, String status) {
             orderCode = oCode;
             hotelName = hName;
             roomName = rName;
             startDate = sDate;
             endDate = eDate;
             totalDays = tDays;
             roomPricePaid = rPrice;
             this.status = status;
         }

         public orderDetails() {

         }

    /**
        * Setter for the next reservation detail in the linked list.
        * @param next the next reservation detail to set
     */
        public void setNext(orderDetails next) {
            this.next = next;
        }
    /**
        * Getter for the next reservation detail in the linked list.
        * @return the next reservation detail
     */
        public orderDetails getNext() {
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

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public void setRoomPricePaid(double roomPricePaid) {
        this.roomPricePaid = roomPricePaid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(int roomFloor) {
        this.roomFloor = roomFloor;
    }
}

