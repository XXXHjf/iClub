package Beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BeanClubActivity {
    private int actID;
    private int clubID;
    private String actTitle;
    private String actDescription;
    private String actCover;
    private String actLocation;
    private int maxNum;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp closeTime;
    private int ifPassed;

    public int getActID() {
        return actID;
    }

    public void setActID(int actID) {
        this.actID = actID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getActDescription() {
        return actDescription;
    }

    public void setActDescription(String actDescription) {
        this.actDescription = actDescription;
    }

    public String getActCover() {
        return actCover;
    }

    public void setActCover(String actCover) {
        this.actCover = actCover;
    }

    public String getActLocation() {
        return actLocation;
    }

    public void setActLocation(String actLocation) {
        this.actLocation = actLocation;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public int getIfPassed() {
        return ifPassed;
    }

    public void setIfPassed(int isPassed) {
        this.ifPassed = isPassed;
    }

    public BeanClubActivity() {
    }

    public BeanClubActivity(int actID, int clubID, String actTitle, String actDescription, String actCover, String actLocation, int maxNum, Timestamp startTime, Timestamp endTime, Timestamp closeTime, int ifPassed) {
        this.actID = actID;
        this.clubID = clubID;
        this.actTitle = actTitle;
        this.actDescription = actDescription;
        this.actCover = actCover;
        this.actLocation = actLocation;
        this.maxNum = maxNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closeTime = closeTime;
        this.ifPassed = ifPassed;
    }

    /**
     * 封装根据sql查询的ResultSet快速构建实体的方法
     * @param rs 结果集ResultSet
     * @return  社团活动实体类
     */
    public static BeanClubActivity resultSetToActivity(ResultSet rs) {
        BeanClubActivity beanClubActivity = new BeanClubActivity();
        int index = 1;
        try {
            beanClubActivity.setActID(rs.getInt(index++));
            beanClubActivity.setClubID(rs.getInt(index++));
            beanClubActivity.setActTitle(rs.getString(index++));
            beanClubActivity.setActDescription(rs.getString(index++));
            beanClubActivity.setActCover(rs.getString(index++));
            beanClubActivity.setActLocation(rs.getString(index++));
            beanClubActivity.setMaxNum(rs.getInt(index++));
            beanClubActivity.setStartTime(rs.getTimestamp(index++));
            beanClubActivity.setEndTime(rs.getTimestamp(index++));
            beanClubActivity.setCloseTime(rs.getTimestamp(index++));
            beanClubActivity.setIfPassed(rs.getInt(index));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  beanClubActivity;
    }
}
