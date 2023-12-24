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
import Beans.BeanUser;
import tools.StatusTool;
import RecyclerViewHolder.ClubViewHolder;
import util.DBUtil;
import SPTools.userSP;

public class clubMineActivity extends AppCompatActivity implements View.OnClickListener {

    private List<BeanClub> clubList = new ArrayList<>();
    private RecyclerView recv_myJoinClubs;
    private JoinClubAdapter joinCLubAdapter;
    private ImageView iv_myJoinClubs_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_mine);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());

        initViews();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        iv_myJoinClubs_back = findViewById(R.id.iv_myJoinClubs_back);
        recv_myJoinClubs = findViewById(R.id.recv_myJoinClubs);
        joinCLubAdapter = new JoinClubAdapter();
        recv_myJoinClubs.setLayoutManager(new LinearLayoutManager(this));
        recv_myJoinClubs.setAdapter(joinCLubAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread_getSQL_clubMineList().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_myJoinClubs_back.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        clubList = new ArrayList<>();
    }

    /**
     * 重写onCli方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_myJoinClubs_back) {
            onBackPressed();
        }
    }

    /**
     * 自定义适配器：我加入的社团
     */
    private class JoinClubAdapter extends RecyclerView.Adapter<ClubViewHolder> {
        @NonNull
        @Override
        public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(clubMineActivity.this).inflate(R.layout.item_club_list, parent, false);
            ClubViewHolder clubViewHolder = new ClubViewHolder(view);
            return clubViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
            final BeanClub beanClub_onBindViewHolder = clubList.get(position);

            holder.tv_clubName.setText(beanClub_onBindViewHolder.getClubName());
            holder.tv_clubInfo.setText(beanClub_onBindViewHolder.getClubDescription());
            Glide.with(holder.iv_icon)
                    .load(beanClub_onBindViewHolder.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_icon);

            holder.root_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(clubMineActivity.this, clubDetailsActivity.class);
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

    /**
     * 自定义线程：获取加入的社团
     */
    private class Thread_getSQL_clubMineList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "SELECT * FROM club WHERE clubID=(SELECT clubID FROM clubmember WHERE ifPassed=1 AND userID=?)";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                BeanUser beanUser = userSP.getUserInfo(getApplicationContext());
                pst.setString(1, beanUser.getUserID());
                java.sql.ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    BeanClub beanClub = BeanClub.resultSetToClub(rs);
                    clubList.add(beanClub);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 通知适配器数据集已更改
                        joinCLubAdapter.notifyDataSetChanged();
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