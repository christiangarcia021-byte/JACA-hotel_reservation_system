
import javafx.application.Application;

/**
 * Main application class for Jaca Hotel system.
 * Initializes database connection, tests various components,
 * and launches the GUI.
 * @version 1.4
 * @author Andy Hernandez, Angel Cruz, Christian Garcia
 * Date: 11/11/2025
 */

public class JacaHotelApplication{
    /**
     * Main method to run the application.
     * @param args command line arguments
     */
    public static void main(String[] args){

        MySQLConnection MyDB = new MySQLConnection();
        MyDB.print();
        customer customer = new customer();
        String user = "alicej@gmail.com";
        String pass = "passAlice1";
        customer.SignIn(user, pass);
        customer.showFile();
        hotel hotel = new hotel(1);
        hotel.initRooms();
        hotel.hotelInfo();
        System.out.printf("\n\n\nTesting Hotel(s) initializer==========\n\n");
        hotelController hotelCtrl = new hotelController();
        hotelCtrl.initHotels();
        hotelCtrl.printAllHotels();


        reservation resv = new reservation();
        System.out.println("\n\n\nTesting findDays and Price methods==========\n\n");
        System.out.println("Days between 2024-07-01 and 2024-07-05: " + resv.calcDays("2024-07-01", "2024-07-05"));
        int days = resv.calcDays("2024-07-01", "2024-07-05");
        room tmp = hotelCtrl.MyHotels[0].getRoom(0);
        System.out.printf("Price:  %f ", resv.reservationCost(tmp, days));
        System.out.print("\n\n\nTesting createReservation method==========\n\n");
        System.out.printf("order code: %s\n", cartUtility.generateOrderCode(1001));


        Calendar cal = new Calendar(102);
        cal.showCalendar();
        cal.getEndDates(1,1,24);
        cal.printEndDates();
        cal.getEndDates(0,11,24);
        cal.printEndDates();
        cal.getEndDates(0,4,10);


        orderList oList = new orderList(10000);
        oList.printOrderList();
        oList.refreshOrders(10001);
        oList.printOrderList();
        oList.refreshOrders(10002);
        oList.printOrderList();
        oList.refreshOrders(10003);
        oList.printOrderList();


        //testing checkout method
        customer testCustomer = new customer();
        testCustomer.SignIn("flopez@gmail.com", "frank88");
        paymentInfo pi = new paymentInfo();
        pi.setCardNumber("598349329934123");
        pi.setCardHolderName("Test Payment");
        pi.setExpiryMonth(12);
        pi.setExpiryYear(2020);
        pi.setCvv("123");
        reservation[] reservations = new reservation[2];
        reservations[0] = new reservation();
        reservations[0].setSelectedRoom(hotelCtrl.MyHotels[0].getRoom(0));
        reservations[0].setStartDate("2024-07-01");
        reservations[0].setEndDate("2024-07-05");
        reservations[0].setTotal_days(4);
        reservations[0].setTotal_cost(reservations[0].reservationCost(reservations[0].getSelectedRoom(), 4));
        reservations[1] = new reservation();
        reservations[1].setSelectedRoom(hotelCtrl.MyHotels[0].getRoom(1));
        reservations[1].setStartDate("2024-08-10");
        reservations[1].setEndDate("2024-08-15");
        reservations[1].setTotal_days(5);
        reservations[1].setTotal_cost(reservations[1].reservationCost(reservations[1].getSelectedRoom(), 5));
        String checkoutResult = cartUtility.checkout(reservations, testCustomer, pi);
        System.out.println("\n\n\nTesting checkout method==========\n\n");
        System.out.println(checkoutResult);





        // Launching GUI
        System.out.println("Launching GUI...");
        Application.launch(MainGUI.class, args);
        System.out.println("closed GUI.");





    }





}