package tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.example.icluub.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class OperationPromptTool {

    // Toast提示
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void SnackEasyMsg(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 确认操作操作框
     * @param context  上下文
     * @param message  提示文本
     * @param positiveAction  确认操作的逻辑处理
     */
    public static void showConfirmDialog(Context context, String message, Runnable positiveAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认操作")
                .setMessage(message)
                .setIcon(R.drawable.ic_warning_green);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行确定操作
                if ( positiveAction != null ) {
                    positiveAction.run();
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
