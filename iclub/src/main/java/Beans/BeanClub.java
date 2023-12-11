package Beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanClub {
    public int clubID;
    public String clubName;
    public String userID;
    public String guidingUnit;
    public String logo;
    public String clubDescription;

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGuidingUnit() {
        return guidingUnit;
    }

    public void setGuidingUnit(String guidingUnit) {
        this.guidingUnit = guidingUnit;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public BeanClub() {
    }

    public BeanClub(int clubID, String clubName, String userID, String guidingUnit, String logo, String clubDescription) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.userID = userID;
        this.guidingUnit = guidingUnit;
        this.logo = logo;
        this.clubDescription = clubDescription;
    }

    public static BeanClub resultSetToClub(ResultSet rs) {
        BeanClub beanClub = new BeanClub();
        try {
            int index = 1;
            beanClub.setClubID(rs.getInt(index++));
            beanClub.setClubName(rs.getString(index++));
            beanClub.setUserID(rs.getString(index++));
            beanClub.setGuidingUnit(rs.getString(index++));
            beanClub.setLogo(rs.getString(index++));
            beanClub.setClubDescription(rs.getString(index));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beanClub;
    }


}
