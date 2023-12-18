package com.example.icluub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Beans.BeanClub;
import Beans.BeanUser;
import fragments.manage.Fragment_actManage;
import fragments.manage.Fragment_memberManage;
import tools.FragmentTool;
import tools.StatusTool;
import util.DBUtil;
import util.SPDataUtils;

public class clubManageActivity extends AppCompatActivity implements View.OnClickListener,Fragment_actManage.ActManageFragmentListener {
    private ImageView iv_clubManage_back;
    private ImageView iv_clubManage_clubIcon;
    private TextView tv_clubManage_clubName;
    private TextView tv_clubManage_clubNum;
    private TextView tv_clubManage_actNum;
    private TextView tv_clubManage_actManage;
    private TextView tv_clubManage_clubManage;
    private int clubID = -1;    //社团ID初始值赋值为-1
    private BeanClub beanClub;
    private int memberNum;
    private int actNum;
    private FrameLayout fl_clubManage;
    private Fragment_actManage fragmentActManage;
    private Fragment_memberManage fragmentMemberManage;
    private boolean existingFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_manage);

        StatusTool.setStatusBar(getWindow());
        Thread_getSQL_clubID thread = new Thread_getSQL_clubID();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread_getSQL_baseInfo().start();

        // 如果 Fragment 不存在，则添加它
        if (!existingFragment) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTool.addFragment(R.id.fl_clubManage, fragmentActManage, fm);
            existingFragment = !existingFragment;
            FragmentTool.addFragment(R.id.fl_clubManage, fragmentMemberManage, fm);
            FragmentTool.hideFragment(fragmentMemberManage, fm);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_clubManage_back.setOnClickListener(this);
        tv_clubManage_actManage.setOnClickListener(this);
        tv_clubManage_clubManage.setOnClickListener(this);
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        iv_clubManage_back = findViewById(R.id.iv_clubManage_back);
        iv_clubManage_clubIcon = findViewById(R.id.iv_clubManage_clubIcon);
        tv_clubManage_clubName = findViewById(R.id.tv_clubManage_clubName);
        tv_clubManage_clubNum = findViewById(R.id.tv_clubManage_clubNum);
        tv_clubManage_actNum = findViewById(R.id.tv_clubManage_actNum);
        tv_clubManage_actManage = findViewById(R.id.tv_clubManage_actManage);
        tv_clubManage_clubManage = findViewById(R.id.tv_clubManage_clubManage);
        fl_clubManage = findViewById(R.id.fl_clubManage);
        Bundle bundle = new Bundle();
        bundle.putInt("clubID", clubID); // 用putXXX方法添加需要传递的数据
        fragmentActManage = new Fragment_actManage();
        fragmentActManage.setArguments(bundle);
        fragmentMemberManage = new Fragment_memberManage();
        fragmentMemberManage.setArguments(bundle);
    }

    /**
     * 显示获取到的数据
     */
    private void loadData() {
        tv_clubManage_clubName.setText(beanClub.getClubName());
        tv_clubManage_clubNum.setText(String.valueOf(memberNum));
        tv_clubManage_actNum.setText(String.valueOf(actNum));
        Glide.with(clubManageActivity.this)
                .load(beanClub.getLogo())
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_clubManage_clubIcon);
    }

    /**
     * 重写onClick方法
     *
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_clubManage_back) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_clubManage_actManage) {
            tv_clubManage_actManage.setTextColor(ContextCompat.getColor(clubManageActivity.this, R.color.textColor_chosen));
            tv_clubManage_clubManage.setTextColor(ContextCompat.getColor(clubManageActivity.this, R.color.textColor_actInfo));
            FragmentTool.hideFragment(fragmentMemberManage, getSupportFragmentManager());
            FragmentTool.showFragment(fragmentActManage, getSupportFragmentManager());
        } else if (view.getId() == R.id.tv_clubManage_clubManage) {
            tv_clubManage_clubManage.setTextColor(ContextCompat.getColor(clubManageActivity.this, R.color.textColor_chosen));
            tv_clubManage_actManage.setTextColor(ContextCompat.getColor(clubManageActivity.this, R.color.textColor_actInfo));
            FragmentTool.hideFragment(fragmentActManage, getSupportFragmentManager());
            FragmentTool.showFragment(fragmentMemberManage, getSupportFragmentManager());
        }
    }

    /**
     * 实现Fragment_actManage中接口的方法
     */
    @Override
    public void onAddActClick() {
        Intent intent = new Intent(clubManageActivity.this, createActActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("beanClub", beanClub);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 自定义线程：获取管理的社团ID
     */
    private class Thread_getSQL_clubID extends Thread {
        @Override
        public void run() {
            BeanUser beanUser = SPDataUtils.getUserInfo(getApplicationContext());
            String userID = beanUser.getUserID();
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                // 获取要展示的信息
                String sql = "SELECT clubID FROM club WHERE userID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, userID);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    clubID = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

    /**
     * 自定义线程：获取管理的社团的基本信息
     */
    private class Thread_getSQL_baseInfo extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from clubDetails where ClubID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    beanClub = BeanClub.resultSetToClub(rs);
                    memberNum = rs.getInt(9);
                    actNum = rs.getInt(10);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
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