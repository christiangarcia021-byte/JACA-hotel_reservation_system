
import javafx.application.Application;


public class JacaHotelApplication{
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
        System.out.printf("order code: %s\n", resv.generateOrderCode(1001));


        Calendar cal = new Calendar(102);
        cal.showCalendar();
        cal.getEndDates(1,1,24);
        cal.printEndDates();
        cal.getEndDates(0,11,24);
        cal.printEndDates();
        cal.getEndDates(0,4,10);


        // Launching GUI
        System.out.println("Launching GUI...");
        Application.launch(MainGUI.class, args);
        System.out.println("closed GUI.");
    }





}