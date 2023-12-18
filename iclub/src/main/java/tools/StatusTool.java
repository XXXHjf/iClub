package tools;

import android.graphics.Color;
import android.view.View;
import android.view.Window;

import androidx.core.view.WindowCompat;

public class StatusTool {
    public static void setStatusBar(Window window) {
        //设置状态栏和底部导航栏为沉浸式(xml文件里设置了android:fitsSystemWindows="true"所以不会完全嵌入状态栏)
        WindowCompat.setDecorFitsSystemWindows(window, false);
        //设置顶部状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置顶部状态栏的图标、字体为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}
