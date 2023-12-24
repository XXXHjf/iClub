package tools;

import android.content.Context;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class TransitionTool {

    //根据手机的分辨率从dp单位转成为px单位
    public static int dipTopx (Context context, float dpValue) {
        //获取当前手机的像素密度（1dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        //四舍五入取整
        return  (int)(dpValue * scale + 0.5f);
    }

    //获取当前时间，以字符串格式返回
    public static String getCurrentTime_String() {
        long TimeStamp = System.currentTimeMillis();
        java.util.Date date = new Date(TimeStamp);
        String sTime = date.toString();
        return sTime;
    }

    /**
     *将数据库的Date类型转换为字符串类型
     * @param sqlDate 数据库的sql.Date类型
     * @return yyyy-MM-dd格式的字符串时间
     */
    public static String sqlDateToString(java.sql.Date sqlDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(sqlDate);
    }

    /**
     * 将yyyy-MM-dd格式的字符串时间转换为sql.Date类型
     * @param dateString 字符串类型的时间
     * @return sql.Date时间
     */
    public static java.sql.Date StringToSqlDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = null;
        try {
            utilDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new Date(utilDate.getTime());
        return sqlDate;
    }

    /**
     * 将TimeStamp时间转换为yyyy-MM-dd HH:mm格式的字符串
     * @param timestamp  Timestamp时间
     * @return  字符串时间
     */
    public static String TimestampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(timestamp);
    }

    /**
     * yyyy-MM-dd HH:mm格式的字符串转换为Timestamp
     * @param string  yyyy-MM-dd HH:mm格式的字符串
     * @return  Timestamp类型的时间
     */
    public static Timestamp StringToTimestamp(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            java.util.Date parsedDate = dateFormat.parse(string);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将TimeMillis类型转换为yyyy-MM-dd HH:mm格式的字符串
     * @param timeMillis  TimeMillis类型数据
     * @return  yyyy-MM-dd HH:mm格式的字符串
     */
    public static String TimeMillisToString(long timeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date date = new java.util.Date(timeMillis);
        String rs = dateFormat.format(date);
        return rs;
    }

    /**
     * 获取array中某个选项在其中的位置
     * @param userCollege 选项的值
     * @param collegeArray array数组
     * @return array中item的下标
     */
    public static int getPositionForArray(String userCollege, String[] collegeArray) {
        // 将数组collegeArray转换为列表
        List<String> collegeList = Arrays.asList(collegeArray);

        // 检查数组是否包含 null 值，处理包含 null 值的情况，返回第一位
        if (collegeList.contains(null))
            return 0;

        // 如果在列表中找到就返回position，否则返回0
        int position = collegeList.indexOf(userCollege);
        return position != -1 ? position : 0;
    }
}
