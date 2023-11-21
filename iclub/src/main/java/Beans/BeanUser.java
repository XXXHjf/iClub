package Beans;

import java.time.LocalDate;
import java.util.Date;

public class BeanUser {
    public String userID;
    public String userName;
    public String password;
    public String profileURL;
    public String college;
    public String majorClass;
    public String nickName;
    public String sex;
    public String phoneNum;
    public LocalDate birthDate;
    public int isPresident;

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
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

    public BeanUser(String userID, String userName, String password, String profileURL, String college, String majorClass, String nickName, String sex, String phoneNum, LocalDate birthDate, int isPresident) {
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
}
