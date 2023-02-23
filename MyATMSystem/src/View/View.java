package View;

import Model.Service.Log.Log;
import Model.Service.System.Systemimple.SystemImpl;
import Model.Service.System.Systemimple.SystemInter;
import Utils.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

@SuppressWarnings("all")
public class View {

    public static void main(String[] args) throws SQLException {
        //创建扫描器对象
        Scanner scanner = new Scanner(System.in);
        //创建登录注册对象
        SystemInter sys= new SystemImpl();
        //数据库连接
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
        } catch (SQLException e) {
            Log.LOGER().error("数据库请求异常");
            conn = null;
        }
        System.out.println(conn);
        //界面输出
        while(true){
            System.out.println("===============ATM系统=================");
            System.out.println("1、账户登录");
            System.out.println("2、账户注册");
            //获取指令
            String command = scanner.next();
            //判断指令
            switch (command){
                case "1":
                {
                    try {
                        if(conn!=null){
                            sys.loading(conn,scanner);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "2":{
                    try {
                        if(conn!=null){
                            sys.register(conn,scanner);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
                    System.out.println("您输入的命令不正确，请重新输入~~");
            }
        }
    }
}
