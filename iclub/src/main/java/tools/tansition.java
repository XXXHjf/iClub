package tools;

import android.content.Context;

import java.util.Date;

public class tansition {

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
        Date date = new Date(TimeStamp);
        String sTime = date.toString();
        return sTime;
    }
}
