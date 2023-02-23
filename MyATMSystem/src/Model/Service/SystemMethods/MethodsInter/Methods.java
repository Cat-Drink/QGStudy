package Model.Service.SystemMethods.MethodsInter;

import Model.POJO.Accounts;

import java.sql.Connection;
import java.util.Scanner;

@SuppressWarnings("all")
public interface Methods {
    void systemMethod(Connection conn, Accounts ac, Scanner scanner);
     void showAccount(Accounts ac);
     void storeMoney(Accounts ac, Scanner scanner);
    void getMoney(Accounts ac, Scanner scanner);
    void moveMoneyToAnother(Accounts ac, Scanner scanner);
    void changePassword(Accounts ac, Scanner scanner);
    boolean deleteAccount(Accounts ac,Scanner scanner);
}
