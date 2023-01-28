package ATM;

import java.util.*;
/**
 * 系统类
 * @author cwt15
 */
public class EditSystem {
    public static void main(String[] args) {
        //创建一个ArrayList数组去存放账户
        ArrayList<Accounts> accounts = new ArrayList<>();
        //创建扫描器对象
        Scanner scanner = new Scanner(System.in);
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
                    accountLoading(accounts,scanner);
                    break;
                }
                case "2":{
                    register(accounts,scanner);
                    break;
                }
                default:
                    System.out.println("您输入的命令不正确，请重新输入~~");
            }
        }
    }

    /**
     * 登录功能应该定义成一个方法，并传入账户集合
     * @param accounts 遍历的对象集合
     */
    private static void accountLoading(ArrayList<Accounts> accounts,Scanner scanner) {
        if (accounts.size() != 0) {
            //让用户输入卡号，根据卡号去账户集合中查询账户对象
            System.out.println("================ATM登录系统================");
            System.out.println("请您输入卡号");
            while (true) {
                String inputCardID = scanner.next();
                //查找账户对象
                Accounts ac = searchForAccounts(inputCardID,accounts);
                if(ac == null){
                    //如果没有找到账户对象，说明登录卡号不存在，提示继续输入卡号
                    System.out.println("该卡号不存在，请重新输入卡号");
                }else {
                    //如果找到了账户对象，说明卡号存在，继续输入密码
                    System.out.println("请您输入登录密码");
                    while (true) {
                        String inputPassWord = scanner.next();
                        if(inputPassWord.equals(ac.getCardPassWord())){
                            //账户密码正确进入功能界面
                            System.out.println("恭喜"+ac.getUserName()+"先生/女士，"+"您已成功进入系统");
                            systemMethod(ac,scanner,accounts);
                            //退出登录界面
                            return;
                        }else {
                            System.out.println("对不起，您输入的密码有误哦~~，请重新输入密码");
                        }
                    }
                }
            }
        }else{
            System.out.println("当前系统无账户，请先注册账户");
        }
    }

    /**
     * ATM具体功能实现的方法封装
     * @param ac 传入的账户对象
     */
    private static void systemMethod(Accounts ac,Scanner scanner,ArrayList<Accounts> accounts) {
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
                    moveMoneyToAnother(ac,accounts,scanner);
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
                    boolean deleteChoice = deleteAccount(ac,accounts,scanner);
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
     * 用户销户功能方法实现封装
     * @param ac 当前账户对象
     * @param accounts 传入集合
     * @param scanner 扫描器对象
     */
    private static boolean deleteAccount(Accounts ac, ArrayList<Accounts> accounts, Scanner scanner) {
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
                        accounts.remove(ac);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("您的当前账户销户已完成");
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

    /**
     * 修改密码的方法封装
     * @param ac 当前对象
     * @param scanner 扫描器对象
     */
    private static void changePassword(Accounts ac, Scanner scanner) {
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
                    ac.setCardPassWord(newPassword);
                    return;
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
     *转账的方法封装
     * @param ac 当前登录的账户
     * @param accounts 需要遍历的账户
     * @param scanner 扫描器对象
     */
    private static void moveMoneyToAnother(Accounts ac, ArrayList<Accounts> accounts, Scanner scanner) {
        if(accounts.size()<2){
            System.out.println("当前系统账户不足2个，无法进行转账操作，请先去开户吧~~");
            return;
        }else if(ac.getLastMoney()==0){
            System.out.println("您的账户现无余额，无法进行转账操作");
            return;
        }
        while (true) {
            System.out.println("请输入您要转账的对象卡号：");
            String anotherAccountCardID = scanner.next();
            if(anotherAccountCardID.equals(ac.getCardID()))
            {
                System.out.println("请不要输入自身的账户卡号~~,请重新输入正确的卡号");
                //跳出本次循环
                continue;
            }
            Accounts anotherAccount = searchForAccounts(anotherAccountCardID,accounts);
            if(anotherAccount == null){
                System.out.println("对不起，您输入的"+anotherAccountCardID+"卡号不存在~~");
                System.out.println("是否要重新输入");
                while (true) {
                    System.out.println("1，重新输入");
                    System.out.println("2，退出转账");
                    int command = scanner.nextInt();
                    switch (command){
                        case 1:{
                            //重新输入
                            break;
                        }
                        case 2:{
                            //退出转账
                            return;
                        }
                        default:{
                            System.out.println("您输入的命令有误，请重新输入");
                            break;
                        }
                    }
                }
            }else {
                //账户存在
                String coverUserName = "*".concat(anotherAccount.getUserName().substring(1));
                System.out.println("该转账对象为："+coverUserName);
                System.out.println("请正确输入该转账对象的姓氏");
                String inputFirstName = scanner.next();
                String makeSureUserName = inputFirstName.concat(anotherAccount.getUserName().substring(1));
                if(makeSureUserName.equals(anotherAccount.getUserName()))
                {
                    //执行转账操作
                    System.out.println("请输入您要转账的金额");
                    while (true) {
                        double moveMoney = scanner.nextDouble();
                        if(moveMoney>ac.getLastMoney()){
                            System.out.println("您的账户余额不足转账金额,您的最多可转账金额为"+ac.getLastMoney());
                            break;
                        }else {
                            System.out.println("您要转账的账户为"+anotherAccount.getCardID());
                            System.out.println("您要转账的金额为"+moveMoney);
                            anotherAccount.setLastMoney(anotherAccount.getLastMoney()+moveMoney);
                            ac.setLastMoney(ac.getLastMoney()-moveMoney);
                            //转账等待 5s
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("转账操作已完成");
                            showAccount(ac);
                            for (int i = 3; i > 0; i--) {
                                System.out.println("感谢您本次操作，"+i+"s 后将退出转账界面");
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
                }else{
                    System.out.println("抱歉，您输入的姓氏有误，请重新输入卡号并确认姓氏");
                    //返回转账卡号输入
                    break;
                }
            }
        }
    }

    /**
     * 取钱功能实现方法封装
     * @param ac 当前对象
     * @param scanner 扫描器
     */
    private static void getMoney(Accounts ac, Scanner scanner) {
        if(ac.getLastMoney()<100)
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
                    ac.setLastMoney(ac.getLastMoney()-getMoneyCase);
                    System.out.println("恭喜您，取"+getMoneyCase+"元成功");
                    showAccount(ac);
                    for (int i = 3; i > 0; i--) {
                        System.out.println("感谢您本次操作，"+i+"s 后将退出取款界面");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return;
                    //退出取款
                }
                case 2:{
                    for (int i = 3; i > 0; i--) {
                        System.out.println("感谢您本次操作，"+i+"s 后将退出取款界面");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
     * 存钱功能的方法实现封装
     * @param ac 存钱额对象
     * @param scanner 扫描器
     */
    private static void storeMoney(Accounts ac, Scanner scanner) {
        System.out.println("============用户存钱界面============");
        System.out.println("请您输入存款金额");
        double inputMoney = scanner.nextDouble();

        //更新账户余额
        ac.setLastMoney(ac.getLastMoney()+inputMoney);
        //显示存款成功信息
        System.out.println("当前账户信息如下：");
        showAccount(ac);
    }

    /**
     * 展示当前账户信息
     * @param ac 当前账户
     */
    private static void showAccount(Accounts ac) {
        System.out.println("=========当前账户信息如下=========");
        System.out.println("卡号："+ac.getCardID());
        System.out.println("账户："+ac.getUserName());
        System.out.println("余额："+ac.getLastMoney()+"元");
        System.out.println("限额："+ac.getGetMoneyLimitation()+"元");
    }

    /**
     * 用户开户功能的实现
     * @param accounts 接收的账户集合
     */
    public static void register(ArrayList<Accounts> accounts,Scanner scanner){
        System.out.println("===========ATM开户系统============");
        //创建一个Accounts类对象封装信息
        Accounts account = new Accounts();

        //键盘录入信息
        //录入账号
        System.out.println("请输入您的账号");
        String userName = scanner.next();
        account.setUserName(userName);

        //录入取款限额
        System.out.println("请输入当次取款限额");
        double limitation = scanner.nextDouble();
        account.setGetMoneyLimitation(limitation);
        //录入密码
        while (true) {
            System.out.println("请输入您的密码");
            String password = scanner.next();
            System.out.println("请再次输入您的密码");
            String passwordMakeSure = scanner.next();
            if(passwordMakeSure.equals(password)){
                //存入密码
                account.setCardPassWord(password);
                //退出循环
                break;
            }else{
                System.out.println("您2次输入的密码不一致，请重新输入~~");
            }
        }

        //获取卡号
        String accountID = getAccountID(accounts);
            //将卡号存入对象
        account.setCardID(accountID);

        //将对象存入集合中
        accounts.add(account);
        System.out.println("恭喜您,"+userName+"先生/女生,"+"您的账户开户已经成功,"+"您账户的卡号为:"+accountID+"请妥善保管");
    }

    /**
     * 为账户生成卡号的方法封装
     * @param accounts 传入的集合
     * @return 返回值为String类型的卡号
     */
    private static String getAccountID(ArrayList<Accounts> accounts) {
        //生成卡号信息，系统自动生成8位数字必须保证卡号的唯一
        String id = "";
        Random random = new Random();
        //生成八位数字并成为字符串
        while (true) {
            for (int i = 0; i < 8; i++) {
                //八位随机数字[0,9]
                id+=random.nextInt(10);
            }
            //判断卡号是否重复
            if(searchForAccounts(id,accounts)!=null){
                //清空生成的id
                id = "";
            }else {
                //退出循环
                break;
            }
        }
        return id;
    }

    /**
     * 根据卡号查询账户
     * @param id 获取的卡号
     * @param accounts 遍历的账户集合
     * @return 返回账户Accounts对象
     */
    private static Accounts searchForAccounts(String id,ArrayList<Accounts> accounts) {
        for (Accounts ac:accounts) {
            if(id.equals(ac.getCardID()))
            {
                //卡号存在则返回该卡号
                return ac;
            }
        }
        //卡号不存在则返回null
        return null;
    }
}
