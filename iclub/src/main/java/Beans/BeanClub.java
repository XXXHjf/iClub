package Beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanClub implements Parcelable, Serializable {
    private int clubID;
    private String clubName;
    private String userID;
    private String guidingUnit;
    private String logo;
    private String clubDescription;

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

    /**
     * 封装根据sql查询的ResultSet快速构建实体的方法
     * @param rs 结果集ResultSet
     * @return  社团实体类
     */
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(clubID);
        parcel.writeString(clubName);
        parcel.writeString(userID);
        parcel.writeString(guidingUnit);
        parcel.writeString(logo);
        parcel.writeString(clubDescription);
    }

    protected BeanClub(Parcel in) {
        clubID = in.readInt();
        clubName = in.readString();
        userID = in.readString();
        guidingUnit = in.readString();
        logo = in.readString();
        clubDescription = in.readString();
    }

    public static final Creator<BeanClub> CREATOR = new Creator<BeanClub>() {
        @Override
        public BeanClub createFromParcel(Parcel in) {
            return new BeanClub(in);
        }

        @Override
        public BeanClub[] newArray(int size) {
            return new BeanClub[size];
        }
    };
}
