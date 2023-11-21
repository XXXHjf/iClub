package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.SQLException;

import Beans.BeanClub;
import util.DBUtil;

public class clubDetailsActivity extends AppCompatActivity {

    private static String TAG_findClubDetails = "查找社团详细信息";
    private String sClubName = "";
    private String sClubInfo = "";
    private TextView tv_clubDetails_clubName;
    private TextView tv_clubDetails_clubInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        tv_clubDetails_clubName = findViewById(R.id.tv_clubDetails_clubName);
        tv_clubDetails_clubInfo = findViewById(R.id.tv_clubDetails_clubInfo);

        Window window = this.getWindow();
        //设置状态栏和底部导航栏为沉浸式(xml文件里设置了android:fitsSystemWindows="true"所以不会完全嵌入状态栏)
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        //设置顶部状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置顶部状态栏的图标、字体为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //获得社团索引页面被点击的社团的ID
        Bundle bundle_getClub = getIntent().getExtras();
        int clubID = bundle_getClub.getInt("clubID");
        Log.d(TAG_findClubDetails, "社团ID为+++++++++++++++++++" + clubID);

        //读取被点击的社团的详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG_findClubDetails, "线程创建成功");
                Connection conn = null;
                try {
                    conn = DBUtil.getConnection();
                    if (conn == null)
                        Log.e(TAG_findClubDetails, "数据库连接后为空");
                    else
                        Log.e(TAG_findClubDetails, "数据库连接成功");
                    String sql = "select * from club where ClubID=?";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, clubID);
                    java.sql.ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        sClubName = rs.getString(2);
                        sClubInfo = rs.getString(6);
                        Log.e(TAG_findClubDetails, "读取成功！社团名称为" + sClubName);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_clubDetails_clubName.setText(sClubName);
                            tv_clubDetails_clubInfo.setText(sClubInfo);
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e(TAG_findClubDetails, "数据库异常" + e.getMessage());
                } finally {
                    if (conn != null)
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
            }
        }).start();


    }
}