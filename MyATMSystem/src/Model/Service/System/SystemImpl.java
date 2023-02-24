package Model.Service.System;

import Model.POJO.Accounts;
import Model.Service.System.Systemimple.SystemInter;
import Model.Service.SystemMethods.MethodsInter.Methods;
import Model.Service.SystemMethods.MethodsImpl;
import Utils.AccountsUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SystemImpl implements SystemInter {
    public  void loading(Connection conn , Scanner scanner) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = null;

        int isNull = 0;
        try {
            sql = "select ID,COUNT(ID) from accounts  group by ID ";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if(rs.next()){
                isNull = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //是否有数据进行判断
        if (isNull!=0) {
            //让用户输入卡号，根据卡号去数据库中查询账户对象
            System.out.println("================ATM登录系统================");
            System.out.println("请您输入卡号或账号");
            while (true) {
                String inputCardIdOrName = scanner.next();
                System.out.println("请您输入登录密码");
                String password = scanner.next();
                //读取出一个账户对象
                Accounts ac = AccountsUtils.searchForAccounts(conn, inputCardIdOrName, password);

                if (ac == null) {
                    //如果没有找到账户对象，说明登录卡号不存在，提示继续输入卡号
                    System.out.println("该用户不存在，请重新输入卡号或密码");
                } else {
                    //账户密码正确进入功能界面
                    System.out.println("恭喜" + ac.getUserName() + "先生/女士，" + "您已成功进入系统");
                    Methods me= new MethodsImpl();
                    me.systemMethod(conn,ac,scanner);
                    //退出登录界面
                    return;
                }
            }
        } else {
            System.out.println("当前系统无账户，请先注册账户");
        }
    }
    public  void register(Connection conn,Scanner scanner){
        PreparedStatement ps = null;
        String sql = null;

        System.out.println("===========ATM开户系统============");
        //键盘录入信息
        //录入账号
        System.out.println("请输入您的账号名");
        String userName = scanner.next();

        //录入取款限额
        System.out.println("请输入当次取款限额");
        double limitation = scanner.nextDouble();

        //录入密码
        String password;
        while (true) {
            System.out.println("请输入设置您的密码");
            password = scanner.next();
            System.out.println("请再次输入确认您的密码");
            String passwordMakeSure = scanner.next();
            if(passwordMakeSure.equals(password)){
                break;
            }else{
                System.out.println("您2次输入的密码不一致，请重新输入~~");
            }
        }

        //获取卡号
        String accountID = AccountsUtils.getAccountID(conn);

        //将数据存入
        try {
            sql = "insert into accounts(cardID, userName, cardPassWord, getMoneyLimitation) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,accountID);
            ps.setString(2,userName);
            ps.setString(3,password);
            ps.setDouble(4,limitation);
            ps.executeUpdate();

            System.out.println("恭喜您," +
                    userName+"先生/女生,"+
                    "您的账户开户已经成功,"+
                    "您账户的卡号为:"+accountID+
                    "请妥善保管");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
