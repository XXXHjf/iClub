package com.example.icluub;

import static tools.TransitionTool.getPositionForArray;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Beans.BeanUser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tools.OperationPromptTool;
import tools.StatusTool;
import tools.TransitionTool;
import util.DBUtil;
import SPTools.userSP;

public class personalDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_name;
    private TextView tv_personalData_stuID;
    private Spinner spinner_college;
    private EditText et_personalData_major;
    private EditText et_nickname;
    private Spinner spinner_sex;
    private EditText et_phone;
    private TextView tv_birthday;
    private ImageView iv_personalData_back;
    private View view_personalData_savingChanges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPersonalInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_birthday.setOnClickListener(this);
        iv_personalData_back.setOnClickListener(this);
        view_personalData_savingChanges.setOnClickListener(this);
    }

    /**
     * 初始化个人信息（从登陆时存储到sharedPreferences里的信息获取）
     */
    private void loadPersonalInfo() {
        BeanUser beanUser = userSP.getUserInfo(getApplicationContext());
        // 获取array的college数组
        String[] colleges = getResources().getStringArray(R.array.college);
        // 找到用户的学院在数组中的位置
        int positionC = getPositionForArray(beanUser.getCollege(), colleges);

        // 获取array的sex数组
        String[] sex = getResources().getStringArray(R.array.sex);
        // 找到用户的性别在数组中的位置
        int positionS = getPositionForArray(beanUser.getSex(), sex);

        // 将登陆的用户信息填入UI界面中
        tv_name.setText(beanUser.getUserName());
        tv_personalData_stuID.setText(beanUser.getUserID());
        spinner_college.setSelection(positionC);
        et_personalData_major.setText(beanUser.getMajorClass());
        et_nickname.setText(beanUser.getNickName());
        spinner_sex.setSelection(positionS);
        et_phone.setText(beanUser.getPhoneNum());
        java.sql.Date sqlDate = beanUser.getBirthDate();
        tv_birthday.setText(TransitionTool.sqlDateToString(sqlDate));
    }

    /**
     * 初始化绑定组件
     */
    private void initViews() {
        tv_name = findViewById(R.id.tv_name);
        tv_personalData_stuID = findViewById(R.id.tv_personalData_stuID);
        spinner_college = findViewById(R.id.spinner_college);
        et_personalData_major = findViewById(R.id.et_personalData_major);
        et_nickname = findViewById(R.id.et_nickname);
        spinner_sex = findViewById(R.id.spinner_sex);
        et_phone = findViewById(R.id.et_phone);
        tv_birthday = findViewById(R.id.tv_birthday);
        iv_personalData_back = findViewById(R.id.iv_personalData_back);
        view_personalData_savingChanges = findViewById(R.id.view_personalData_savingChanges);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundleCode = new Bundle();
            // 保存修改
            if (msg.what == 1) {
                bundleCode = msg.getData();
                String s_et_personalData_major = bundleCode.getString("et_personalData_major");
                et_personalData_major.setText(s_et_personalData_major);
                Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            }
            // 读取用户的个人数据
            else if (msg.what == 2) {
                bundleCode = msg.getData();

                // 获取array的college数组
                String[] colleges = getResources().getStringArray(R.array.college);
                // 找到用户的学院在数组中的位置
                int positionC = getPositionForArray(bundleCode.getString("spinner_personalData_college"), colleges);

                // 获取array的sex数组
                String[] sex = getResources().getStringArray(R.array.sex);
                // 找到用户的性别在数组中的位置
                int positionS = getPositionForArray(bundleCode.getString("spinner_personalData_sex"), sex);

                // 将数据库中的信息填入UI界面中
                tv_name.setText(bundleCode.getString("tv_personalData_userName"));
                tv_personalData_stuID.setText(bundleCode.getString("tv_personalData_userID"));
                spinner_college.setSelection(positionC);
                et_personalData_major.setText(bundleCode.getString("et_personalData_major"));
                et_nickname.setText(bundleCode.getString("et_personalData_nickName"));
                spinner_sex.setSelection(positionS);
                et_phone.setText(bundleCode.getString("et_personalData_phone"));
                tv_birthday.setText(bundleCode.getString("tv_personalData_birthDate"));
            }
        }
    };

    /**
     * 重写onClick方法
     * @param view
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        // 设置出生日期选择框
        if (view.getId() == R.id.tv_birthday) {
            new CardDatePickerDialog.Builder(this)
                    .setTitle("出生日期")
                    .showBackNow(false)
                    .showFocusDateInfo(false)   //是否显示选中的日期
                    .setBackGroundModel(CardDatePickerDialog.STACK) //显示模式--顶部圆角
                    .setMaxTime(System.currentTimeMillis())
                    .setWrapSelectorWheel(false)    //是否循环滚动
                    .showDateLabel(true)    //是否显示单位
                    .setThemeColor(R.color.blue)
                    .setDisplayType(0, 1, 2)    //设置显示的标签
                    .setLabelText("年","月","日","时","分","秒")
                    .setOnChoose("确定", new Function1<Long, Unit>() {
                        @Override
                        public Unit invoke(Long timeInMillis) {
                            Date date = new Date(timeInMillis);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = dateFormat.format(date);
                            tv_birthday.setText(formattedDate);
                            return null;
                        }
                    }).build().show();
        }
        //左上角返回按钮返回上一页
        else if (view.getId() == R.id.iv_personalData_back) {
            onBackPressed();
        }
        //点击保存修改后将修改后的数据传到数据库中
        else if (view.getId() == R.id.view_personalData_savingChanges) {
            OperationPromptTool.showConfirmDialog(personalDataActivity.this, "确定保存修改吗？", new Runnable() {
                @Override
                public void run() {
                    new Thread_commitData().start();
                }
            });
        }
    }

    /**
     * 自定义线程：提交保存修改
     */
    private class Thread_commitData extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            BeanUser beanUser = userSP.getUserInfo(getApplicationContext());
            // 更新登录用户的SP文件信息
            userSP.updatePersonalInfo(getApplicationContext(), (String) spinner_college.getSelectedItem(), et_personalData_major.getText().toString(),
                    et_nickname.getText().toString(), (String) spinner_sex.getSelectedItem(), et_phone.getText().toString(), tv_birthday.getText().toString());
            // 更新登录用户的数据库数据
            try {
                conn = DBUtil.getConnection();
                String sql = "update user set college=?, majorClass=?, nickName=?, sex=?, phoneNum=?, birthDate=? "
                        + "where userID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, (String) spinner_college.getSelectedItem());
                pst.setString(2, et_personalData_major.getText().toString());
                pst.setString(3, et_nickname.getText().toString());
                pst.setString(4, (String) spinner_sex.getSelectedItem());
                pst.setString(5, et_phone.getText().toString());

                // 将字符串日期转换为sql.Date类型
                java.sql.Date sqlDate = TransitionTool.StringToSqlDate(tv_birthday.getText().toString());
                pst.setDate(6, sqlDate);
                pst.setString(7, beanUser.getUserID());
                int i = pst.executeUpdate();
                if (i == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(personalDataActivity.this, "保存成功！");
                        }
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }
}

