package util;

import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final String TAG = "mySQL数据库连接调试";
    private static final String jdbcUrl = "jdbc:mysql://"
            +"hjf-2447129407.rwlb.rds.aliyuncs.com:3306"
            +"/iclub"
            +"?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8";
    private static final String dbUser= "super_admin";
    private static final String dbPwd = "200366Hjf";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws java.sql.SQLException {
        return java.sql.DriverManager.getConnection(jdbcUrl, dbUser, dbPwd);
    }

    public static void close(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
