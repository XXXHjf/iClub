package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Beans.BeanClub;
import util.DBUtil;

public class clubDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG_findClubDetails = "查找社团详细信息";
    private String sClubName = "";
    private String sClubInfo = "";
    private BeanClub beanClub = new BeanClub();
    private int clubMemberSum;
    private String presidentAvatar = "";
    private String presidentName = "";
    private ImageView iv_clubDetails_back;
    private TextView tv_clubDetails_clubName;
    private TextView tv_clubDetails_clubInfo;
    private ImageView iv_clubDetails_icon;
    private TextView tv_clubDetails_guidingUnit;
    private TextView tv_clubDetails_clubMemberNumber;
    private ImageView iv_clubDetails_presidentIcon;
    private TextView tv_clubDetails_presidentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        // 获取绑定组件
        initViews();

        Window window = this.getWindow();
        // 设置状态栏和底部导航栏为沉浸式(xml文件里设置了android:fitsSystemWindows="true"所以不会完全嵌入状态栏)
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        // 设置顶部状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
        // 设置顶部状态栏的图标、字体为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 获得社团索引页面被点击,传递过来的社团的ID
        Bundle bundle_getClub = getIntent().getExtras();
        int clubID = bundle_getClub.getInt("clubID");

        // 读取被点击的社团的详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG_findClubDetails, "线程创建成功");
                Connection conn = null;
                try {
                    conn = DBUtil.getConnection();
                    // 获取要展示的信息
                    String sql = "select * from clubdetails where ClubID=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, clubID);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        beanClub = BeanClub.resultSetToClub(rs);
                        presidentName = rs.getString(7);
                        presidentAvatar = rs.getString(8);
                        clubMemberSum = rs.getInt(9);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(iv_clubDetails_icon)
                                    .load(beanClub.getLogo())
                                    .apply(RequestOptions.circleCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(iv_clubDetails_icon);
                            tv_clubDetails_clubName.setText(beanClub.getClubName());
                            tv_clubDetails_clubInfo.setText(beanClub.getClubDescription());
                            tv_clubDetails_guidingUnit.setText(beanClub.getGuidingUnit());
                            tv_clubDetails_clubMemberNumber.setText(String.valueOf(clubMemberSum));
                            Glide.with(iv_clubDetails_icon)
                                    .load(beanClub.getLogo())
                                    .apply(RequestOptions.circleCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(iv_clubDetails_icon);
                            tv_clubDetails_presidentName.setText(presidentName);
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DBUtil.close(conn);
                }
            }
        }).start();

        iv_clubDetails_back.setOnClickListener(this);


    }

    private void initViews() {
        iv_clubDetails_back = findViewById(R.id.iv_clubDetails_back);
        iv_clubDetails_icon = findViewById(R.id.iv_clubDetails_icon);
        tv_clubDetails_clubName = findViewById(R.id.tv_clubDetails_clubName);
        tv_clubDetails_clubInfo = findViewById(R.id.tv_clubDetails_clubInfo);
        tv_clubDetails_guidingUnit = findViewById(R.id.tv_clubDetails_guidingUnit);
        tv_clubDetails_clubMemberNumber = findViewById(R.id.tv_clubDetails_clubMemberNumber);
        iv_clubDetails_presidentIcon = findViewById(R.id.iv_clubDetails_presidentIcon);
        tv_clubDetails_presidentName = findViewById(R.id.tv_clubDetails_presidentName);
    }

    // 重写onClick方法
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_clubDetails_back)
            onBackPressed();
    }
}