package com.example.icluub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClub;
import util.DBUtil;

public class myJoinedClubs extends AppCompatActivity implements View.OnClickListener {

    private List<BeanClub> clubList = new ArrayList<>();
    private RecyclerView recv_myJoinClubs;
    private JoinClubAdapter joinCLubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_joined_clubs);

        // 设置状态栏的相关信息
        Window window = this.getWindow();
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        findViewById(R.id.iv_myJoinClubs_back).setOnClickListener(this);

        recv_myJoinClubs = findViewById(R.id.recv_myJoinClubs);
        // 新线程———读取我加入的社团并存入clubList中
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                try {
                    conn = DBUtil.getConnection();
                    String sql = "SELECT * FROM club WHERE clubID=(SELECT clubID FROM clubmember WHERE ifPassed=1 AND userID=?)";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, login_config.LOGIN_USER.getUserID());
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
        }).start();

        joinCLubAdapter = new JoinClubAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(myJoinedClubs.this);
        recv_myJoinClubs.setLayoutManager(layoutManager);
        recv_myJoinClubs.setAdapter(joinCLubAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_myJoinClubs_back) {
            onBackPressed();
        }
    }

    private class JoinClubAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(myJoinedClubs.this, R.layout.item_club_list, null);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final BeanClub beanClub_onBindViewHolder = clubList.get(position);

            holder.tv_clubName.setText(beanClub_onBindViewHolder.getClubName());
            holder.tv_clubInfo.setText(beanClub_onBindViewHolder.getClubDescription());
            Glide.with(holder.iv_icon)
                    .load(beanClub_onBindViewHolder.getLogo())
//                    .placeholder(R.drawable.ludiwusi) // 设置占位图
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_icon);

            holder.root_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(myJoinedClubs.this, clubDetailsActivity.class);
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

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_clubName;
        TextView tv_clubInfo;
        ConstraintLayout root_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_clubList_icon);
            tv_clubName = itemView.findViewById(R.id.tv_clubList_clubName);
            tv_clubInfo = itemView.findViewById(R.id.tv_clubList_clubInfo);
            root_item = itemView.findViewById(R.id.rootView_itemClubList);
        }
    }
}