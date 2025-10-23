import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Session;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.ServerSessionStateController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.Properties;
import java.util.concurrent.locks.Lock;

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
        System.out.printf("\n\n\nNEW STUFF==========\n\n");
        hotelController hotelCtrl = new hotelController();
        hotelCtrl.initHotels();
        hotelCtrl.printAllHotels();

        MainGUI gui = new MainGUI();
        Application.launch(MainGUI.class, args);
    }





}