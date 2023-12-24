package SPTools;

import android.content.Context;
import android.content.SharedPreferences;

public class appStatusSP {
    public static final String fileName = "appStatusSP";

    public static Boolean getHasCameraPermissions(Context context) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return  sp.getBoolean("hasPermissions", false);
    }

    public static void setCameraPermissionsTrue(Context context, Boolean flag) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("hasPermissions", flag);
        editor.apply();
    }

    public static void clearFile(Context context) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }




}
