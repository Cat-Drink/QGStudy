package Model.POJO;

import java.util.Objects;

/**
 * 账户类
 * @author cwt15
 */
@SuppressWarnings("all")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accounts accounts = (Accounts) o;

        if (Double.compare(accounts.getMoneyLimitation, getMoneyLimitation) != 0) return false;
        if (Double.compare(accounts.lastMoney, lastMoney) != 0) return false;
        if (!Objects.equals(cardID, accounts.cardID)) return false;
        if (!Objects.equals(userName, accounts.userName)) return false;
        return Objects.equals(cardPassWord, accounts.cardPassWord);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cardID != null ? cardID.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (cardPassWord != null ? cardPassWord.hashCode() : 0);
        temp = Double.doubleToLongBits(getMoneyLimitation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lastMoney);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "cardID='" + cardID + '\'' +
                ", userName='" + userName + '\'' +
                ", cardPassWord='" + cardPassWord + '\'' +
                ", getMoneyLimitation=" + getMoneyLimitation +
                ", lastMoney=" + lastMoney +
                '}';
    }
}
