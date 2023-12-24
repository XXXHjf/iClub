package Beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BeanClubMember {
    private int memberIndexID;
    private int clubID;
    private String userID;
    private int memberRole;
    private Timestamp joinDate;
    private int ifPassed;
    private String WeChat;
    private String applyReason;

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String weChat) {
        WeChat = weChat;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public int getMemberIndexID() {
        return memberIndexID;
    }

    public void setMemberIndexID(int memberIndexID) {
        this.memberIndexID = memberIndexID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(int memberRole) {
        this.memberRole = memberRole;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public int getIfPassed() {
        return ifPassed;
    }

    public void setIfPassed(int ifPassed) {
        this.ifPassed = ifPassed;
    }

    public BeanClubMember() {
    }

    public BeanClubMember(int memberIndexID, int clubID, String userID, int memberRole, Timestamp joinDate, int ifPassed, String weChat, String applyReason) {
        this.memberIndexID = memberIndexID;
        this.clubID = clubID;
        this.userID = userID;
        this.memberRole = memberRole;
        this.joinDate = joinDate;
        this.ifPassed = ifPassed;
        WeChat = weChat;
        this.applyReason = applyReason;
    }

    /**
     * 封装将结果集快速转换为实体类
     * @param rs 结果集
     * @return 社团成员实体类
     */
    public static BeanClubMember resultSetToMember(ResultSet rs) {
        BeanClubMember beanClubMember = new BeanClubMember();
        try {
            int index = 1;
            beanClubMember.setMemberIndexID(rs.getInt(index++));
            beanClubMember.setClubID(rs.getInt(index++));
            beanClubMember.setUserID(rs.getString(index++));
            beanClubMember.setMemberRole(rs.getInt(index++));
            beanClubMember.setJoinDate(rs.getTimestamp(index++));
            beanClubMember.setIfPassed(rs.getInt(index++));
            beanClubMember.setWeChat(rs.getString(index++));
            beanClubMember.setApplyReason(rs.getString(index));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beanClubMember;
    }
}
