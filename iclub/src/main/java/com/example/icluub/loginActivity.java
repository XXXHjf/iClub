package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.sql.Connection;
import java.sql.SQLException;

import Beans.BeanUser;
import tools.OperationPromptTool;
import tools.StatusTool;
import util.DBUtil;
import SPTools.LoginStatusSP;
import SPTools.userSP;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout rootView_login;
    private EditText ev_login_account;
    private EditText ev_login_password;
    private ImageView iv_login_eyeSelector;
    private boolean ifPwdVisible = false;
    private CheckBox cb_auto_login;
    private boolean autoLoginFlag = false; //自动登录标志
    private CheckBox cb_remember_pwd;
    private boolean rmbPwdFlag = false;  //记住密码标志
    private Button button_login;
    private String userID;
    private String Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());
        // 初始化控件
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("myData", MODE_PRIVATE);
        if (sp != null) {
            String userAccount = sp.getString("userID", "");
            ev_login_account.setText(userAccount);
            rmbPwdFlag = sp.getBoolean("rmbPwdFlag", false);
            autoLoginFlag = sp.getBoolean("autoLoginFlag", false);
        }
        if (rmbPwdFlag) {
            cb_remember_pwd.setChecked(true);
            ev_login_password.setText(sp.getString("password", ""));
        }
        if (autoLoginFlag) {
            cb_auto_login.setChecked(true);
            button_login.performClick();
        }
    }

    /**
     * 封装所有初始化控件的操作
     */
    private void initViews() {
        rootView_login = findViewById(R.id.rootView_login);
        ev_login_account = findViewById(R.id.ev_login_account);
        ev_login_password = findViewById(R.id.ev_login_password);
        cb_auto_login = findViewById(R.id.cb_auto_login);
        cb_remember_pwd = findViewById(R.id.cb_remember_pwd);
        button_login = findViewById(R.id.button_login);
        iv_login_eyeSelector = findViewById(R.id.iv_login_eyeSelector);
        button_login.setOnClickListener(this);
        iv_login_eyeSelector.setOnClickListener(this);
    }

    /**
     * 重写onClick方法
     * @param view 当前的视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_login) {
            userID = ev_login_account.getText().toString();
            Pwd = ev_login_password.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = null;
                    BeanUser beanUser = null;
                    try {
                        conn = DBUtil.getConnection();
                        String sql = "SELECT * FROM user WHERE userID=? AND password=?";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1, userID);
                        pst.setString(2, Pwd);
                        java.sql.ResultSet rs = pst.executeQuery();
                        if (rs.next()) {
                            beanUser = BeanUser.resultSetToUser(rs);
                            userSP.saveUserLoginInfo(getApplicationContext(), beanUser);
                            userSP.updateRmb_Auto(getApplicationContext(), cb_remember_pwd.isChecked(), cb_auto_login.isChecked());
                            LoginStatusSP.setHasLogin(loginActivity.this, true);
                            Intent intent = new Intent(loginActivity.this, homeActivity.class);
                            // 清除任务栈并创建一个新的任务栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OperationPromptTool.SnackEasyMsg(rootView_login, "账号名或密码有误，请重试");
                                }
                            });
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn);
                    }
                }
            }).start();
        }
        else if (view.getId() == R.id.iv_login_eyeSelector) {
            if (ifPwdVisible) {
                ifPwdVisible = false;
                iv_login_eyeSelector.setSelected(false);
                ev_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                ifPwdVisible = true;
                iv_login_eyeSelector.setSelected(true);
                ev_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }
}