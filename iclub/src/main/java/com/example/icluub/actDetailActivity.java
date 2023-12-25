package com.example.icluub;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import Beans.BeanClubActivity;
import Beans.BeanUser;
import tools.OperationPromptTool;
import tools.StatusTool;
import tools.TransitionTool;
import util.DBUtil;
import SPTools.userSP;

public class actDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private int actID;
    private BeanClubActivity bean = null;
    private int regNum = 0;
    private String presidentName = "";
    private ImageView iv_clubDetails_cover;
    private TextView tv_actDetail_title;
    private TextView tv_actDetail_notice;
    private TextView tv_actDetail_signNum;
    private TextView tv_actDetail_time;
    private TextView tv_actDetail_location;
    private TextView tv_actDetail_close;
    private TextView tv_actDetail_chargeMan;
    private TextView tv_actDetail_actContent;
    private Button button_signUp;
    private ImageView iv_actDetail_back;
    private BeanUser beanUser = null;
    private boolean ifSignFlag = false;
    private boolean ifManage = false;   // 判断是否是管理员界面点击进来的活动详情页(管理界面可以看到未审批通过的活动)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());
        // 初始化组件
        initViews();

        beanUser = userSP.getUserInfo(getApplicationContext());

        // 获得活动中心页面被点击,传递过来的活动的ID
        Bundle bundleActID = getIntent().getExtras();
        actID = bundleActID.getInt("actID");
        // 判断传递进来的数据有没有ifManage,如果有就代表是社团管理页面点进来的
        if (bundleActID.getBoolean("ifManage"))
            ifManage = bundleActID.getBoolean("ifManage");

        Thread_getSQL_ifSign threadGetSQLIfSign = new Thread_getSQL_ifSign();
        threadGetSQLIfSign.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 从数据库获取数据
        Thread_getSQL_actDetail threadGetSQLActDetail = new Thread_getSQL_actDetail();
        threadGetSQLActDetail.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        button_signUp.setOnClickListener(this);
        iv_actDetail_back.setOnClickListener(this);
    }

    /**
     * 将数据加载到组件上
     */
    private void loadDetails() {
        Glide.with(actDetailActivity.this)
                .load(bean.getActCover())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_clubDetails_cover);
        tv_actDetail_title.setText(bean.getActTitle());
        String signNum = regNum + "/" + bean.getMaxNum() + "人";
        tv_actDetail_signNum.setText(signNum);
        Timestamp actStartTime = bean.getStartTime();
        Timestamp actEndTime = bean.getEndTime();
        String actTime = TransitionTool.TimestampToString(actStartTime) + " ~ " + "\n" + TransitionTool.TimestampToString(actEndTime);
        tv_actDetail_time.setText(actTime);
        tv_actDetail_location.setText(bean.getActLocation());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp actCloseTime = bean.getCloseTime();
        tv_actDetail_close.setText(TransitionTool.TimestampToString(bean.getCloseTime()));
        if (bean.getIfPassed() == 0) {
            // 活动申请未通过
            tv_actDetail_notice.setText("");
            Drawable closeDrawable = ContextCompat.getDrawable(actDetailActivity.this, R.drawable.res_radius_sign_end);
            button_signUp.setBackground(closeDrawable);
            button_signUp.setText("暂未通过审批");
            button_signUp.setEnabled(false);
        } else {
            if (actEndTime.before(now)) {
                // 活动已经结束的情况
                tv_actDetail_notice.setText("活动已结束");
                tv_actDetail_notice.setTextColor(0xFF744E20);
                Drawable drawable = ContextCompat.getDrawable(actDetailActivity.this, R.drawable.res_radius_act_detail_end);
                button_signUp.setBackground(drawable);
                button_signUp.setEnabled(false);
                button_signUp.setText("活动已结束");
            } else if (actCloseTime.before(now)) {
                // 活动报名截至的情况
                tv_actDetail_notice.setText("报名已结束");
                tv_actDetail_notice.setTextColor(0xFF567722);
                Drawable drawable = ContextCompat.getDrawable(actDetailActivity.this, R.drawable.res_radius_act_detail_ddl);
                button_signUp.setBackground(drawable);
                button_signUp.setEnabled(false);
                if (ifSignFlag)
                    button_signUp.setText("已报名");
                else
                    button_signUp.setText("报名已结束");
            } else {
                // 活动正常报名的情况
                tv_actDetail_notice.setText("火热报名中");
                tv_actDetail_notice.setTextColor(0xFFF81803);
                Drawable drawable = ContextCompat.getDrawable(actDetailActivity.this, R.drawable.res_radius_act_detail_on);
                button_signUp.setBackground(drawable);
                if ( ifSignFlag ) {
                    // 如果已经报名过该活动
                    button_signUp.setText("取消报名");
                    button_signUp.setEnabled(true);
                }
                else {
                    // 未报名过
                    if (regNum >= bean.getMaxNum()) {
                        // 如果报名人数已满
                        button_signUp.setText("报名人数已满");
                        button_signUp.setEnabled(false);
                    }
                    else {
                        // 可以报名
                        button_signUp.setText("立即报名");
                        button_signUp.setEnabled(true);
                    }
                }

            }
        }
        tv_actDetail_chargeMan.setText(presidentName);
        tv_actDetail_actContent.setText(bean.getActDescription());
    }

    /**
     * 初始化所有组件
     */
    private void initViews() {
        iv_clubDetails_cover = findViewById(R.id.iv_clubDetails_cover);
        tv_actDetail_title = findViewById(R.id.tv_actDetail_title);
        tv_actDetail_notice = findViewById(R.id.tv_actDetail_notice);
        tv_actDetail_signNum = findViewById(R.id.tv_actDetail_signNum);
        tv_actDetail_time = findViewById(R.id.tv_actDetail_time);
        tv_actDetail_location = findViewById(R.id.tv_actDetail_location);
        tv_actDetail_close = findViewById(R.id.tv_actDetail_close);
        tv_actDetail_chargeMan = findViewById(R.id.tv_actDetail_chargeMan);
        tv_actDetail_actContent = findViewById(R.id.tv_actDetail_actContent);
        button_signUp = findViewById(R.id.button_signUp);
        iv_actDetail_back = findViewById(R.id.iv_actDetail_back);
    }

    /**
     * 重写点击方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_signUp) {
            if (ifSignFlag) {
                // 取消报名
                OperationPromptTool.showConfirmDialog(actDetailActivity.this, "您确定取消报名吗？", new Runnable() {
                    @Override
                    public void run() {
                        ifSignFlag = !ifSignFlag;
                        Thread_cancelSignUp thread = new Thread_cancelSignUp();
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Thread_getSQL_actDetail().start();
                        OperationPromptTool.showMsg(actDetailActivity.this, "取消报名成功！");
                    }
                });
            } else if (!ifSignFlag) {
                // 报名
                ifSignFlag = !ifSignFlag;
                Thread_signUp thread = new Thread_signUp();
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread_getSQL_actDetail().start();
                OperationPromptTool.showMsg(actDetailActivity.this, "报名成功！");
            }
        } else if (view.getId() == R.id.iv_actDetail_back) {
            onBackPressed();
        }
    }

    /**
     * 自定义线程：获取活动的详细信息，获取后执行加载数据
     */
    private class Thread_getSQL_actDetail extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM ClubActivityView WHERE actID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, actID);
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    bean = BeanClubActivity.resultSetToActivity(rs);
                    regNum = rs.getInt(13);
                    presidentName = rs.getString(14);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载组件的信息
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

    /**
     * 自定义线程：用于判断点进某个活动时是否已经报名
     */
    private class Thread_getSQL_ifSign extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM registration WHERE userID=? AND actID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, beanUser.getUserID());
                pst.setInt(2, actID);
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next())
                    ifSignFlag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

    /**
     * 自定义线程：取消报名
     */
    private class Thread_cancelSignUp extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "DELETE FROM registration WHERE userID=? AND actID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, beanUser.getUserID());
                pst.setInt(2, actID);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

    /**
     * 自定义线程：报名活动
     */
    private class Thread_signUp extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "INSERT INTO registration(userID, actID, registrationDate) " +
                        "VALUES (?, ?, NOW())";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, beanUser.getUserID());
                pst.setInt(2, actID);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }
}