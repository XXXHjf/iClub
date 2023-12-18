package com.example.icluub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Beans.BeanClub;
import tools.StatusTool;
import util.DBUtil;

public class clubDetailsActivity extends AppCompatActivity implements View.OnClickListener {
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
    private int clubID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());
        // 获取绑定组件
        initViews();
        // 获得社团索引页面被点击,传递过来的社团的ID
        Bundle bundle_getClub = getIntent().getExtras();
        clubID = bundle_getClub.getInt("clubID");
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread_getDetails().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_clubDetails_back.setOnClickListener(this);
    }

    /**
     * set数据到视图上
     */
    private void loadDetails() {
        Glide.with(getApplicationContext())
                .load(beanClub.getLogo())
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_clubDetails_icon);
        tv_clubDetails_clubName.setText(beanClub.getClubName());
        tv_clubDetails_clubInfo.setText(beanClub.getClubDescription());
        tv_clubDetails_guidingUnit.setText(beanClub.getGuidingUnit());
        tv_clubDetails_clubMemberNumber.setText(String.valueOf(clubMemberSum));
        Glide.with(getApplicationContext())
                .load(presidentAvatar)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_clubDetails_presidentIcon);
        tv_clubDetails_presidentName.setText(presidentName);
    }

    /**
     * 初始化所有组件
     */
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

    /**
     * 重写onClick方法
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_clubDetails_back)
            onBackPressed();
    }

    /**
     * 自定义线程：点击的社团的详细信息
     */
    private class Thread_getDetails extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                // 获取要展示的信息
                String sql = "select * from clubDetails where ClubID=?";
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
                        loadDetails();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }
}