package com.example.icluub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClubActivity;
import tools.StatusTool;
import tools.TransitionTool;
import RecyclerViewHolder.ActViewHolder;
import util.DBUtil;

public class actCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private List<BeanClubActivity> actList = new ArrayList<>();
    private List<String> clubNameList = new ArrayList<>();
    private List<Integer> regNumList = new ArrayList<>();
    private ImageView iv_center_back;
    private RecyclerView recv_center;
    private ActListAdapter actListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_center);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());
        // 初始化组件
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 查询所有活动
        new Thread_getSQL_actsList().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_center_back.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        actList = new ArrayList<>();
        clubNameList = new ArrayList<>();
        regNumList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        iv_center_back = findViewById(R.id.iv_center_back);
        recv_center = findViewById(R.id.recv_center);
        actListAdapter = new ActListAdapter();
        recv_center.setLayoutManager(new LinearLayoutManager(this));
        recv_center.setAdapter(actListAdapter);
    }

    /**
     * 重写onClick方法
     *
     * @param view 布局
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_center_back) {
            onBackPressed();
        }
    }

    /**
     * 自定义线程：查询所有活动
     */
    private class Thread_getSQL_actsList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM ClubActivityView WHERE ifPassed=1 ORDER BY closeTime DESC";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanClubActivity bean = BeanClubActivity.resultSetToActivity(rs);
                    actList.add(bean);
                    clubNameList.add(rs.getString(12));
                    regNumList.add(rs.getInt(13));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知适配器数据集已更改
                        actListAdapter.notifyDataSetChanged();
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
     *自定义适配器：活动项的适配器
     */
    private class ActListAdapter extends RecyclerView.Adapter<ActViewHolder> {

        @NonNull
        @Override
        public ActViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(actCenterActivity.this).inflate(R.layout.item_activity, parent, false);
            ActViewHolder actViewHolder = new ActViewHolder(view);
            return actViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ActViewHolder holder, int position) {
            final BeanClubActivity bean = actList.get(position);

            holder.tv_activity_actName.setText(bean.getActTitle());
            holder.tv_activity_clubName.setText(clubNameList.get(position));
            Timestamp actStartTime = bean.getStartTime();
            Timestamp actEndTime = bean.getEndTime();
            holder.tv_activity_timeStart.setText(TransitionTool.TimestampToString(actStartTime));
            holder.tv_activity_timeEnd.setText(" ~ " + TransitionTool.TimestampToString(actEndTime));
            holder.tv_activity_place.setText(bean.getActLocation());

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp actCloseTime = bean.getCloseTime();
            if (actEndTime.before(now)) {
                // 活动已经结束的情况
                Drawable closeDrawable = ContextCompat.getDrawable(actCenterActivity.this, R.drawable.res_radius_sign_end);
                holder.tv_activity_signUp.setBackground(closeDrawable);
                holder.tv_activity_signUp.setText("活动已结束");
            } else if (actCloseTime.before(now)) {
                // 活动报名截至的情况
                Drawable endDrawable = ContextCompat.getDrawable(actCenterActivity.this, R.drawable.res_radius_sign_ddl);
                holder.tv_activity_signUp.setBackground(endDrawable);
                holder.tv_activity_signUp.setText("报名已结束");
            } else {
                // 活动正常报名的情况
                Drawable onDrawable = ContextCompat.getDrawable(actCenterActivity.this, R.drawable.res_radius_sign_on);
                holder.tv_activity_signUp.setBackground(onDrawable);
                String signNum = "报名中 " + regNumList.get(position) + "/" + bean.getMaxNum() + "人";
                holder.tv_activity_signUp.setText(signNum);
            }
            Glide.with(actCenterActivity.this)
                    .load(bean.getActCover())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_activity_cover);

            holder.rootView_item_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(actCenterActivity.this, actDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("actID", bean.getActID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return actList.size();
        }
    }
}