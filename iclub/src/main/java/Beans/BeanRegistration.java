package Beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BeanRegistration {
    private int regIndexID;
    private String userID;
    private int actID;
    private Timestamp registrationDate;
    private int ifPassed;

    public int getRegIndexID() {
        return regIndexID;
    }

    public void setRegIndexID(int regIndexID) {
        this.regIndexID = regIndexID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getActID() {
        return actID;
    }

    public void setActID(int actID) {
        this.actID = actID;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getIfPassed() {
        return ifPassed;
    }

    public void setIfPassed(int ifPassed) {
        this.ifPassed = ifPassed;
    }

    public BeanRegistration() {
    }

    public BeanRegistration(int regIndexID, String userID, int actID, Timestamp registrationDate, int ifPassed) {
        this.regIndexID = regIndexID;
        this.userID = userID;
        this.actID = actID;
        this.registrationDate = registrationDate;
        this.ifPassed = ifPassed;
    }

    /**
     * 封装将结果集快速转换为报名实体类的方法
     * @param rs 结果集
     * @return 报名信息实体类
     */
    public static BeanRegistration resultSetToReg(ResultSet rs) {
        BeanRegistration bean = new BeanRegistration();
        int index=1;
        try {
            bean.setRegIndexID(rs.getInt(index++));
            bean.setUserID(rs.getString(index++));
            bean.setActID(rs.getInt(index++));
            bean.setRegistrationDate(rs.getTimestamp(index++));
            bean.setIfPassed(rs.getInt(index));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
