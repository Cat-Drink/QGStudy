package Utils;

import Model.POJO.Accounts;
import Model.Service.Log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

@SuppressWarnings("all")
public class AccountsUtils {
    /**
     * 拷贝对象数据，创建新对象
     * @param username
     * @param password
     * @param cardId
     * @param LastMoney
     * @param GetMoneyLimitation
     * @return
     */
    public static Accounts setAccount(String username, String password, String cardId, double LastMoney, double GetMoneyLimitation){
        Accounts ac = null;
        try {
            ac = new Accounts();
            ac.setUserName(username);
            ac.setCardPassWord(password);
            ac.setCardID(cardId);
            ac.setLastMoney(LastMoney);
            ac.setGetMoneyLimitation(GetMoneyLimitation);
        } catch (Exception e) {
            Log.LOGER().error("对象创建失败");
        }
        return  ac;
    }

    /**
     * 需要密码的指定查询：根据卡号和密码获取账户
     * @param conn
     * @param idOrName 用户名或者ID
     * @param password 密码
     * @return
     */
    public static Accounts searchForAccounts(Connection conn, String idOrName,String password) {
        PreparedStatement ps =  null;
        ResultSet rs = null;
        String sql = null;

        //通过账号和密码或ID和密码才可以查找
        try {
            sql = "select *from accounts where cardID = ? and cardPassWord = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,idOrName);
            ps.setString(2,password);
            rs = ps.executeQuery();



            while (rs.next()){
                String cardID = rs.getString("cardID");
                String username = rs.getString("username");
                String cardPassWord = rs.getString("cardPassWord");
                double getMoneyLimitation = rs.getDouble("getMoneyLimitation");
                double lastMoney = rs.getDouble("lastMoney");
                if(idOrName.equals(cardID)&&password.equals(cardPassWord))
                {
                    //卡号存在则返回该卡号
                    return setAccount(username,cardPassWord,cardID,lastMoney,getMoneyLimitation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //用户不存在则返回null
        return null;
    }

    /**
     * 随机构建用户卡ID
     * @param conn
     * @return String id对象
     */
    public static String getAccountID(Connection conn) {
        String id = null;
        try {
            String sql = "select * from accounts";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //生成卡号信息，系统自动生成8位数字且必须保证卡号的唯一
            id = "";
            Random random = new Random();
            boolean flag = true;

            while (true) {
                //生成八位数字并成为字符串
                for (int i = 0; i < 8; i++) {
                    //八位随机数字[0,9]
                    id+=random.nextInt(10);
                }
                //判断是否有重复
                while (rs.next()){
                    if((id.equals(rs.getString("cardID")))){
                        flag = false;
                        break;
                    }
                }
                //匹配符合，则退出循环
                if(flag){
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 退出界面
     */
    public static void exit(){
        for (int i = 3; i > 0; i--) {
            System.out.println("感谢您本次操作，"+i+"s 后将退出当前界面");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
