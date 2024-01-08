package com.example.icluub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClub;
import Beans.BeanClubActivity;
import RecyclerViewHolder.ActManageViewHolder;
import SPTools.userSP;
import tools.StatusTool;
import tools.TransitionTool;
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
    private RecyclerView rv_clubDetail_acts;
    private List<BeanClubActivity> beanActList = new ArrayList<>();
    private List<Integer> regNumList = new ArrayList<>();
    private ActListAdapter adapter;
    private int clubID = 0;
    private int ifJoin = -1;
    private ImageView iv_joinUs;
    private TextView tv_joinUs;

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
        new Thread_getSQL_ifJoinClub().start();
        new Thread_getSQL_actsList().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_clubDetails_back.setOnClickListener(this);
        iv_joinUs.setOnClickListener(this);
        tv_joinUs.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        rv_clubDetail_acts = findViewById(R.id.rv_clubDetail_acts);
        tv_joinUs = findViewById(R.id.tv_joinUs);
        iv_joinUs = findViewById(R.id.iv_joinUs);
        regNumList = new ArrayList<>();
        beanActList = new ArrayList<>();
        adapter = new ActListAdapter();
        rv_clubDetail_acts.setLayoutManager(new LinearLayoutManager(clubDetailsActivity.this));
        rv_clubDetail_acts.setAdapter(adapter);
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
        else if (view.getId() == R.id.iv_joinUs || view.getId() == R.id.tv_joinUs) {
            Intent intent = new Intent(clubDetailsActivity.this, joinClubActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("beanClub", beanClub);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
                    beanClub = new BeanClub();
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

    /**
     * 自定义线程：查询本社团的所有活动
     */
    private class Thread_getSQL_actsList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM ClubActivityView WHERE clubID=? AND ifPassed=1 ORDER BY closeTime DESC";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                java.sql.ResultSet rs = pst.executeQuery();
                beanActList = new ArrayList<>();
                regNumList = new ArrayList<>();
                while (rs.next()) {
                    BeanClubActivity bean = BeanClubActivity.resultSetToActivity(rs);
                    beanActList.add(bean);
                    regNumList.add(rs.getInt(13));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知适配器数据集已更改
                        adapter.notifyDataSetChanged();
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
     * 自定义线程：查询是否已经加入该社团
     */
    private class Thread_getSQL_ifJoinClub extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT ifPassed FROM ClubMember WHERE clubID=? AND userID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, clubID);
                pst.setString(2, userSP.getUserInfo(clubDetailsActivity.this).getUserID());
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    ifJoin = rs.getInt(1);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( ifJoin == -1 ) {
                            tv_joinUs.setText("加入我们");
                        } else if ( ifJoin == 0 ) {
                            iv_joinUs.setImageResource(R.drawable.ic_club_detail_christmas_toy);
                            iv_joinUs.setClickable(false);
                            tv_joinUs.setClickable(false);
                            tv_joinUs.setText("申请审核中");
                        } else if ( ifJoin == 1 ) {
                            iv_joinUs.setImageResource(R.drawable.ic_club_detail_christmas_elk);
                            iv_joinUs.setClickable(false);
                            tv_joinUs.setClickable(false);
                            tv_joinUs.setText("您已加入");
                        }
                        iv_joinUs.setVisibility(View.VISIBLE);
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
     * 自定义适配器：活动项的适配器
     */
    private class ActListAdapter extends RecyclerView.Adapter<ActManageViewHolder> {

        @NonNull
        @Override
        public ActManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(clubDetailsActivity.this).inflate(R.layout.item_act_manage, parent, false);
            ActManageViewHolder actManageViewHolder = new ActManageViewHolder(view);
            return actManageViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ActManageViewHolder holder, int position) {
            final BeanClubActivity bean = beanActList.get(position);

            holder.tv_actManage_actName.setText(bean.getActTitle());
            Timestamp actStartTime = bean.getStartTime();
            Timestamp actEndTime = bean.getEndTime();
            holder.tv_actManage_timeStart.setText(TransitionTool.TimestampToString(actStartTime));
            holder.tv_actManage_timeEnd.setText(TransitionTool.TimestampToString(actEndTime));

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp actCloseTime = bean.getCloseTime();
            if (actEndTime.before(now)) {
                // 活动已经结束的情况
                Drawable closeDrawable = ContextCompat.getDrawable(clubDetailsActivity.this, R.drawable.res_radius_sign_end);
                holder.tv_actManage_signUp.setBackground(closeDrawable);
                holder.tv_actManage_signUp.setText("活动已结束");
            } else if (actCloseTime.before(now)) {
                // 活动报名截至的情况
                Drawable endDrawable = ContextCompat.getDrawable(clubDetailsActivity.this, R.drawable.res_radius_sign_ddl);
                holder.tv_actManage_signUp.setBackground(endDrawable);
                holder.tv_actManage_signUp.setText("报名已结束");
            } else {
                // 活动正常报名的情况
                Drawable onDrawable = ContextCompat.getDrawable(clubDetailsActivity.this, R.drawable.res_radius_sign_on);
                holder.tv_actManage_signUp.setBackground(onDrawable);
                String signNum = "报名中 " + regNumList.get(position) + "/" + bean.getMaxNum() + "人";
                holder.tv_actManage_signUp.setText(signNum);
            }

            Glide.with(clubDetailsActivity.this)
                    .load(bean.getActCover())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_actManage_cover);

            holder.rootView_item_actManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(clubDetailsActivity.this, actDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("actID", bean.getActID());
                    bundle.putBoolean("ifManage", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return beanActList.size();
        }
    }


}