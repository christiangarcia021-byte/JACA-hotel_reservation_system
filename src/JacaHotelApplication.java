import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Session;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.ServerSessionStateController;

import java.util.Properties;
import java.util.concurrent.locks.Lock;

public class JacaHotelApplication{
    public static void main(String[] args){

        MySQLConnection MyDB = new MySQLConnection();
        MyDB.print();
        customer cust = new customer();
        String user = "alicej@gmail.com";
        String pass = "passAlice1";
        cust.SignIn(user, pass);
        cust.showFile();

    }

}