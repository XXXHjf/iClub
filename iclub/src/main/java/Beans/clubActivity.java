package Beans;

import java.sql.Timestamp;

public class clubActivity {
    public int actID;
    public int clubID;
    public String actTitle;
    public String actDescription;
    public String actCover;
    public String actLocation;
    public int maxNum;
    public Timestamp startTime;
    public Timestamp endTime;
    public Timestamp closeTime;
    public int isPassed;

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

    public int getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(int isPassed) {
        this.isPassed = isPassed;
    }

    public clubActivity() {
    }

    public clubActivity(int actID, int clubID, String actTitle, String actDescription, String actCover, String actLocation, int maxNum, Timestamp startTime, Timestamp endTime, Timestamp closeTime, int isPassed) {
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
        this.isPassed = isPassed;
    }
}
