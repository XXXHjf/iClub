package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Beans.BeanUser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tools.Transition;
import util.DBUtil;
import util.SPDataUtils;

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
    private static String TAG_getPrsnData = "数据库调试";
    private static String TAG = "数据库调试";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        // 顶部状态栏调整
        Window window = this.getWindow();
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();
        initPersonalInfo();

//        Thread_getPersonalData thread_getPersonalData = new Thread_getPersonalData();
//        thread_getPersonalData.start();
    }

    /**
     * 初始化个人信息（从登陆时存储到sharedPreferences里的信息获取）
     */
    private void initPersonalInfo() {
        BeanUser beanUser = SPDataUtils.getUserInfo(getApplicationContext());
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
        tv_birthday.setText(Transition.sqlDateToString(sqlDate));
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

        tv_birthday.setOnClickListener(this);

        iv_personalData_back = findViewById(R.id.iv_personalData_back);
        iv_personalData_back.setOnClickListener(this);

        view_personalData_savingChanges = findViewById(R.id.view_personalData_savingChanges);
        view_personalData_savingChanges.setOnClickListener(this);
    }

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
     * 获取array中某个选项在其中的位置
     * @param userCollege 用户的学院信息
     * @param collegeArray 学院数组布局
     * @return array中item的下标
     */
    public int getPositionForArray(String userCollege, String[] collegeArray) {
        // 将数组collegeArray转换为列表
        List<String> collegeList = Arrays.asList(collegeArray);

        // 检查数组是否包含 null 值，处理包含 null 值的情况，返回第一位
        if (collegeList.contains(null))
            return 0;

        // 如果在列表中找到就返回position，否则返回0
        int position = collegeList.indexOf(userCollege);
        return position != -1 ? position : 0;
    }


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
            Thread threads = new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = null;
                    BeanUser beanUser = SPDataUtils.getUserInfo(getApplicationContext());
                    // 更新登录用户的SP文件信息
                    SPDataUtils.updatePersonalInfo(getApplicationContext(), (String) spinner_college.getSelectedItem(), et_personalData_major.getText().toString(),
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
                        java.sql.Date sqlDate = Transition.StringToSqlDate(tv_birthday.getText().toString());
                        pst.setDate(6, sqlDate);
                        pst.setString(7, beanUser.getUserID());
                        int i = pst.executeUpdate();
                        if (i == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn);
                    }
                }
            });
            new Thread(threads).start();
        }
    }

    /**
     * 自定义线程：从数据库中获取登录用户的个人信息
     */
    public class Thread_getPersonalData extends Thread {
        @Override
        public void run() {
            Log.d(TAG_getPrsnData, "线程创建成功");
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                if (conn == null)
                    Log.d(TAG_getPrsnData, "数据库连接后为空");
                Log.d(TAG_getPrsnData, "数据库连接成功");
                String sql = "select * from user where userID = '" + "32100012" + "'";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("tv_personalData_userID", rs.getString(1));
                    bundle.putString("tv_personalData_userName", rs.getString(2));
                    bundle.putString("spinner_personalData_college", rs.getString(5));
                    bundle.putString("et_personalData_major", rs.getString(6));
                    bundle.putString("et_personalData_nickName", rs.getString(7));
                    bundle.putString("spinner_personalData_sex", rs.getString(8));
                    bundle.putString("et_personalData_phone", rs.getString(9));
                    bundle.putString("tv_personalData_birthDate", rs.getString(10));

                    msg.setData(bundle);
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(TAG_getPrsnData, "数据库异常" + e.getMessage());
            } finally {
                DBUtil.close(conn);
            }
        }


    }




}

