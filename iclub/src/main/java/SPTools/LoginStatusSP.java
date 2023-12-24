package SPTools;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginStatusSP {
    public static final String fileName = "loginStatus";

    public static Boolean getHasLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean("hasLogin", false);
    }

    public static void setHasLogin(Context context, Boolean flag) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("hasLogin", flag);
        if ( flag ) {
            long loginTime = System.currentTimeMillis();
            editor.putLong("loginTime", loginTime);
        }
        editor.apply();
    }

    public static void clearFile(Context context) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
