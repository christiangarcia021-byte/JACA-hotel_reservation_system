import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Session;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.ServerSessionStateController;

import java.util.Properties;
import java.util.concurrent.locks.Lock;

public class JacaHotelApplication{
    public static void main(String[] args){

        MySQLConnection p = new MySQLConnection();
        p.print();


    }


}