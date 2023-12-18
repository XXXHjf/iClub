package tools;

import android.content.Context;
import android.widget.Toast;

public class OperationPromptTool {

    // Toast提示
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
