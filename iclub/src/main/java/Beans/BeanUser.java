package Beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class BeanUser {
    private String userID;
    private String userName;
    private String password;
    private String profileURL;
    private String college;
    private String majorClass;
    private String nickName;
    private String sex;
    private String phoneNum;
    private Date birthDate;
    private int isPresident;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajorClass() {
        return majorClass;
    }

    public void setMajorClass(String majorClass) {
        this.majorClass = majorClass;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getIsPresident() {
        return isPresident;
    }

    public void setIsPresident(int isPresident) {
        this.isPresident = isPresident;
    }

    public BeanUser() {
    }

    public BeanUser(String userID, String userName, String password, String profileURL, String college, String majorClass, String nickName, String sex, String phoneNum, Date birthDate, int isPresident) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.profileURL = profileURL;
        this.college = college;
        this.majorClass = majorClass;
        this.nickName = nickName;
        this.sex = sex;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
        this.isPresident = isPresident;
    }

    /**
     * 封装根据sql查询的ResultSet快速构建实体的方法
     * @param rs 结果集ResultSet
     * @return  用户实体类
     */
    public static BeanUser resultSetToUser(ResultSet rs) {
        BeanUser beanUser = new BeanUser();
        try {
            int index = 1;
            beanUser.setUserID(rs.getString(index++));
            beanUser.setUserName(rs.getString(index++));
            beanUser.setPassword(rs.getString(index++));
            beanUser.setProfileURL(rs.getString(index++));
            beanUser.setCollege(rs.getString(index++));
            beanUser.setMajorClass(rs.getString(index++));
            beanUser.setNickName(rs.getString(index++));
            beanUser.setSex(rs.getString(index++));
            beanUser.setPhoneNum(rs.getString(index++));
            beanUser.setBirthDate(rs.getDate(index++));
            beanUser.setIsPresident(rs.getInt(index));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beanUser;
    }
}
