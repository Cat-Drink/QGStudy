package Model.Service.System.Systemimple;


import java.sql.Connection;
import java.util.Scanner;

public interface SystemInter {
        void loading(Connection conn, Scanner scanner);
        void register(Connection conn,Scanner scanner);
}
