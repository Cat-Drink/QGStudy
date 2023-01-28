package ATM;

/**
 * 账户类
 * @author cwt15
 */
public class Accounts {
    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardPassWord() {
        return cardPassWord;
    }

    public void setCardPassWord(String cardPassWord) {
        this.cardPassWord = cardPassWord;
    }

    public double getGetMoneyLimitation() {
        return getMoneyLimitation;
    }

    public void setGetMoneyLimitation(double getMoneyLimitation) {
        this.getMoneyLimitation = getMoneyLimitation;
    }

    public double getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(double lastMoney) {
        this.lastMoney = lastMoney;
    }

    private String cardID;
    private String userName;
    private String cardPassWord;
    private double getMoneyLimitation;
    private double lastMoney;
}
