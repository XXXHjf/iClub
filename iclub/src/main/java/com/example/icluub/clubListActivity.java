package com.example.icluub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClub;
import tools.StatusTool;
import RecyclerViewHolder.ClubViewHolder;
import util.DBUtil;

public class clubListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_clubList_back;
    private RecyclerView recv_clubList;
    private ClubListAdapter clubListAdapter;
    private List<BeanClub> clubList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread_getSQL_clubList().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_clubList_back.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        clubList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        iv_clubList_back = findViewById(R.id.iv_clubList_back);
        recv_clubList = findViewById(R.id.recv_clubList);
        clubListAdapter = new ClubListAdapter();
        recv_clubList.setLayoutManager(new LinearLayoutManager(this));
        recv_clubList.setAdapter(clubListAdapter);
    }

    /**
     * 重写onClick方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_clubList_back) {
            onBackPressed();
        }
    }

    /**
     * 自定义社团列表的适配器
     */
    private class ClubListAdapter extends RecyclerView.Adapter<ClubViewHolder> {
        @NonNull
        @Override
        public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(clubListActivity.this).inflate(R.layout.item_club_list, parent, false);
            ClubViewHolder clubViewHolder = new ClubViewHolder(view);
            return clubViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
            final BeanClub beanClub_onBindViewHolder = clubList.get(position);

            holder.tv_clubName.setText(beanClub_onBindViewHolder.getClubName());
            holder.tv_clubInfo.setText(beanClub_onBindViewHolder.getClubDescription());
            Glide.with(getApplicationContext())
                    .load(beanClub_onBindViewHolder.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_icon);

            holder.root_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(clubListActivity.this, clubDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("clubID", beanClub_onBindViewHolder.getClubID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return clubList.size();
        }
    }

    private class Thread_getSQL_clubList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from club";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanClub beanClub = BeanClub.resultSetToClub(rs);
                    clubList.add(beanClub);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知适配器数据集已更改
                        clubListAdapter.notifyDataSetChanged();
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