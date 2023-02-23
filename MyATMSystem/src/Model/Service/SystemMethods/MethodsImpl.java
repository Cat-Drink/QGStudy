package Model.Service.SystemMethods;

import Model.POJO.Accounts;
import Model.Service.Log.Log;
import Model.Service.SystemMethods.MethodsInter.Methods;
import Utils.AccountsUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@SuppressWarnings("all")
public class MethodsImpl implements Methods {
    Connection conn = null;
    @Override
    public void systemMethod(Connection conn,Accounts ac, Scanner scanner) {
        //将连接联系过来
        this.conn = conn;
        while (true) {
            System.out.println("=======系统功能操作======");
            System.out.println("1,查询账户");
            System.out.println("2,存款");
            System.out.println("3,取款");
            System.out.println("4,转账");
            System.out.println("5,修改密码");
            System.out.println("6,退出");
            System.out.println("7,注销账户");
            System.out.println("请选择：");
            int command = scanner.nextInt();
            //选择方法执行
            switch (command){
                //查询账户
                case 1: {
                    //展示账户信息
                    showAccount(ac);
                    break;
                }
                //存款
                case 2: {
                    //存钱功能
                    storeMoney(ac,scanner);
                    break;
                }
                //取款
                case 3: {
                    getMoney(ac,scanner);
                    break;
                }
                //转账
                case 4: {
                    //转账的方法封装
                    moveMoneyToAnother(ac,scanner);
                    break;
                }
                //修改密码
                case 5: {
                    //修改密码的方法封装
                    changePassword(ac,scanner);
                    break;
                }
                //退出
                case 6: {
                    System.out.println("感谢您的使用，欢迎下次光临");
                    return;
                    //退出当前方法的执行
                }
                //注销账户
                case 7: {
                    //注销账户的方法封装与判定
                    boolean deleteChoice = deleteAccount(ac,scanner);
                    if(deleteChoice == true){
                        //清除当前账户后应该退出功能界面
                        return;
                    }else {
                        //如果未进行销户操作则返回功能界面
                        break;
                    }
                }
                //命令错误，进入循环
                default:{
                    System.out.println("您输入的操作命令不正确，请重新输入~~");
                }
            }
        }
    }

    /**
     * 展示用户信息
     * @param ac
     */
    @Override
    public void showAccount(Accounts ac) {
        System.out.println("=========当前账户信息如下=========");
        System.out.println("卡号："+ac.getCardID());
        System.out.println("账户："+ac.getUserName());
        System.out.println("余额："+ac.getLastMoney()+"元");
        System.out.println("限额："+ac.getGetMoneyLimitation()+"元");
    }

    /**
     * 存钱方法
     * @param ac
     * @param scanner
     */
    @Override
    public void storeMoney(Accounts ac, Scanner scanner) {
        System.out.println("============用户存钱界面============");
        System.out.println("请您输入存款金额");
        double inputMoney = scanner.nextDouble();

        //临时账户更新
        ac.setLastMoney(ac.getLastMoney()+inputMoney);
        //数据库更新
        String sql = "update accounts set lastMoney = ? where cardID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1,ac.getLastMoney()+inputMoney);
            ps.setString(2, ac.getCardID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //等待时长
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //显示存款成功信息
        showAccount(ac);
    }

    /**
     * 取钱功能方法
     * @param ac
     * @param scanner
     */
    @Override
    public void getMoney(Accounts ac, Scanner scanner) {
        int leastLine = 100;
        if(ac.getLastMoney()<leastLine)
        {
            System.out.println("您的账户余额不足100元，无法进行取款");
            return;
        }
        System.out.println("=============用户取款界面============");
        System.out.println("欢迎进入取款界面，请输入您要取款的金额：");
        double getMoneyCase = 0;
        while (true) {
            getMoneyCase = scanner.nextDouble();
            System.out.println("您要取款的金额为："+getMoneyCase);
            //不得超出当次取款上限
            if(getMoneyCase>ac.getGetMoneyLimitation())
            {
                System.out.println("您的本次取款金额过大，"+"每次最多可取"+ac.getGetMoneyLimitation()+",请重新输入取款金额");
            }
            else if(getMoneyCase>ac.getLastMoney()){
                //不得超出账户余额
                System.out.println("您的当前账户余额不足，无法进行取款操作，请重新输入取款金额");
                System.out.println("您当前的账户余额为:"+ac.getLastMoney()+"元");
                //让线程睡眠一下，可以看的到
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                break;
            }
        }
        //取款行为安全性判断确认
        while (true) {
            System.out.println("是否确认取款");
            System.out.println("1,确认");
            System.out.println("2,取消");
            int command = scanner.nextInt();
            switch(command){
                case 1:{
                    //余额 = 原余额 - 取款金额
                    double finalLastMoney = ac.getLastMoney()-getMoneyCase;
                    //数据库更新
                    try {
                        String sql = "update accounts set lastMoney = ?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setDouble(1,finalLastMoney);
                        ac.setLastMoney(finalLastMoney);
                        ps.executeUpdate();
                        System.out.println("恭喜您，取"+getMoneyCase+"元成功");
                    } catch (SQLException e) {
                        Log.LOGER().error(e.getMessage()+"取钱失败，异常！");
                        return;
                    }
                    showAccount(ac);
                    AccountsUtils.exit();
                    return;
                    //退出取款
                }
                case 2:{
                    AccountsUtils.exit();
                    return;
                    //退出取款
                }
                default:{
                    System.out.println("您输入的命令不正确，请重新输入");
                }
            }
        }
    }

    /**
     * 转账功能方法封装
     * @param conn
     * @param ac
     * @param scanner
     */
    @Override
    public void moveMoneyToAnother(Accounts ac, Scanner scanner) {
        PreparedStatement ps =  null;
        ResultSet rs = null;
        String sql;

        try {
            //查找ID主键，判断成员数量
            sql = "select count(*) from accounts";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);

            int min = 2;
            int size = 0;
            while (rs.next()){
                size = rs.getInt(1);
                //循环获取ID
            }
            if(size < min) {
                System.out.println("当前系统账户不足2个，无法进行转账操作，请先去开户吧~~");
                return;
            } else if (ac.getLastMoney() == 0) {
                System.out.println("您的账户现无余额，无法进行转账操作");
                return;
            }
        } catch (SQLException e) {
            Log.LOGER().error(e.getMessage()+"主键查找异常！");
        }

        //转账操作
        Accounts anotherAc = null;
        String anotherCardId = null;
        boolean flag = true;
        try {
            sql = "select *from accounts";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("请输入您要转账的对象卡号：");
             anotherCardId = scanner.next();
            if (anotherCardId.equals(ac.getCardID())) {
                System.out.println("请不要输入自身的账户卡号~~,请重新输入正确的卡号");
                //跳出本次循环
                continue;
            }
            try {
                while (rs.next()) {
                    if (anotherCardId.equals(rs.getString("cardID"))) {
                        flag = false;
                        break;
                    }
                }
            } catch (SQLException e) {
                Log.LOGER().error(e.getMessage()+"账户比对异常！");
                return;
            }

            if (flag) {
                System.out.println("对不起，您输入的" + anotherCardId + "卡号不存在~~");
                System.out.println("是否要重新输入");
                while (true) {
                    System.out.println("1，重新输入");
                    System.out.println("2，退出转账");
                    int command = scanner.nextInt();
                    switch (command) {
                        case 1: {
                            //重新输入
                            break;
                        }
                        case 2: {
                            //退出转账
                            return;
                        }
                        default: {
                            System.out.println("您输入的命令有误，请重新输入");
                            break;
                        }
                    }
                    break;
                }
            }
            else {
                //填充对象
                try {
                    sql = "select *from accounts where cardID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1,anotherCardId);
                    rs = ps.executeQuery();
                    String userName = null;
                    String cardPassword = null;
                    String cardID = null;
                    double lastMoney = 0;
                    double getMoneyLimitation = 0;
                    if(rs.next()){
                         userName = rs.getString("userName");
                         cardPassword = rs.getString("cardPassword");
                         cardID = rs.getString("cardID");
                         lastMoney = rs.getDouble("lastMoney");
                         getMoneyLimitation = rs.getDouble("getMoneyLimitation");
                    }
                    anotherAc = AccountsUtils.setAccount(userName,cardPassword,cardID,lastMoney,getMoneyLimitation);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.LOGER().error("对象注入异常");
                }


                String coverUserName = "*".concat(anotherAc.getUserName().substring(1));
                System.out.println("该转账对象为：" + coverUserName);
                System.out.println("请正确输入该转账对象的姓氏");
                String inputFirstName = scanner.next();
                String makeSureUserName = inputFirstName.concat(anotherAc.getUserName().substring(1));
                if (makeSureUserName.equals(anotherAc.getUserName())) {
                    //执行转账操作
                    System.out.println("请输入您要转账的金额");
                    while (true) {
                        double moveMoney = scanner.nextDouble();
                        if (moveMoney > ac.getLastMoney()) {
                            System.out.println("您的账户余额不足转账金额,您的最多可转账金额为" + ac.getLastMoney());
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        } else {
                            System.out.println("您要转账的账户为" + anotherAc.getCardID());
                            System.out.println("您要转账的金额为" + moveMoney);
                            try {
                                //数据库更新
                                sql = "update accounts set lastMoney = ? where cardID = ?";
                                ps = conn.prepareStatement(sql);
                                ps.setDouble(1,(anotherAc.getLastMoney() + moveMoney));
                                ps.setString(2,anotherCardId);
                                ps.executeUpdate();
                                ps.setDouble(1,ac.getLastMoney()-moveMoney);
                                ps.setString(2,ac.getCardID());
                                ps.executeUpdate();

                                //虚拟对象
                                anotherAc.setLastMoney(anotherAc.getLastMoney() + moveMoney);
                                ac.setLastMoney(ac.getLastMoney() - moveMoney);
                            } catch (SQLException e) {
                                Log.LOGER().error(e.getMessage()+"转账失败，系统异常！");
                            }
                            //转账等待 5s
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("转账操作已完成");
                            showAccount(ac);
                            int time = 3;
                            for (int i = time; i > 0; i--) {
                                System.out.println("感谢您本次操作，" + i + "s 后将退出转账界面");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            //退出转账操作
                            return;
                        }
                    }
                } else {
                    System.out.println("抱歉，您输入的姓氏有误，请重新输入卡号并确认姓氏");
                    //返回转账卡号输入
                   break;
                }
            }
        }
    }

    /**
     * 密码修改方法
     * @param ac
     * @param scanner
     */
    @Override
    public void changePassword(Accounts ac, Scanner scanner) {
        String sql = null;
        PreparedStatement ps = null;

        System.out.println("========用户密码修改界面========");
        System.out.println("请您输入当前密码");
        String oldPassword = scanner.next();
        if (oldPassword.equals(ac.getCardPassWord())) {
            while (true) {
                System.out.println("请您输入新密码：");
                String newPassword = scanner.next();
                System.out.println("请再次输入新密码");
                String newPasswordMakSure = scanner.next();
                if(newPasswordMakSure.equals(newPassword)){
                    try {
                        //数据库更新
                        sql = "update accounts set cardPassWord = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1,newPassword);
                        ps.executeUpdate();

                        //虚拟对象
                        ac.setCardPassWord(newPassword);

                    } catch (SQLException e) {
                        Log.LOGER().error(e.getMessage()+"密码修改异常");
                    }finally {
                        return;
                    }
                }else {
                    System.out.println("您输入的2次密码不一致，请重新输入");
                    //循环输入
                    break;
                }
            }
        } else {
            System.out.println("您输入的密码错误，请重新输入");
            //不必进行循环，直接退出修改密码操作
        }
    }

    /**
     * 账号删除功能方法
     * @param ac
     * @param scanner
     * @return
     */
    @Override
    public  boolean deleteAccount(Accounts ac,Scanner scanner) {
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs;

        if(ac.getLastMoney()>0){
            System.out.println("您账户目前余额"+ac.getLastMoney()+"元，不允许销户");
            return false;
        }
        System.out.println("是否确定销户  y/n");
        String yn = scanner.next();
        switch (yn){
            case "y" :{
                System.out.println("请输入当前账户密码");
                while (true) {
                    String deleteMakeSurePassword = scanner.next();
                    if(deleteMakeSurePassword.equals(ac.getCardPassWord())){
                        try {
                            //获取原来数据库总数据数量
                            sql = "select count(*) from accounts";
                            ps = conn.prepareStatement(sql);
                            rs = ps.executeQuery();
                            int count =0;
                            while (rs.next()){
                                count = rs.getInt(1);
                            }
                            int max = count;

                            //销户的同时修改ID主键为重新的自动增长
                            sql = "delete from accounts where cardID = ?";
                            ps = conn.prepareStatement(sql);
                            ps.setString(1,ac.getCardID());
                            ps.executeUpdate();
                            sql = "alter table accounts drop ID";
                            ps = conn.prepareStatement(sql);
                            ps.executeUpdate();
                            sql = "alter table accounts add ID int not null first ";
                            ps = conn.prepareStatement(sql);
                            ps.executeUpdate();
                            sql = "alter table accounts modify ID int primary key auto_increment";
                            ps = conn.prepareStatement(sql);
                            ps.executeUpdate();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        //销户操作缓冲3s
                        try {
                            Thread.sleep(3000);
                            System.out.println("您的当前账户销户已完成");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //销户成功
                        return true;
                    }else {
                        System.out.println("您输入的密码错误，请重新输入");
                    }
                }
            }
            default:{
                //取消销户
                System.out.println("好的，当前账户将继续为您保留");
                return false;
            }
        }

    }
}
