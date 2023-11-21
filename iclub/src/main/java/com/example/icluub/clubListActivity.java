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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.BeanClub;
import util.DBUtil;

public class clubListActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TAG_readClubTable = "读取club表";
    private ImageView iv_clubList_back;
    private RecyclerView recv_clubList;
    private ClubListAdapter clubListAdapter;
    private List<BeanClub> clubList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        Window window = this.getWindow();
        //设置状态栏和底部导航栏为沉浸式(xml文件里设置了android:fitsSystemWindows="true"所以不会完全嵌入状态栏)
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        //设置顶部状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置顶部状态栏的图标、字体为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        iv_clubList_back = findViewById(R.id.iv_clubList_back);
        iv_clubList_back.setOnClickListener(this);

        recv_clubList = findViewById(R.id.recv_clubList);
        //读取数据库里的社团信息并存入clubList中
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG_readClubTable, "线程创建成功");
                Connection conn = null;
                try {
                    conn = DBUtil.getConnection();
                    if (conn == null)
                        Log.e(TAG_readClubTable, "数据库连接后为空");
                    else
                        Log.e(TAG_readClubTable, "数据库连接成功");
                    String sql = "select * from club";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    java.sql.ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        BeanClub beanClub = new BeanClub();
                        beanClub.setClubID(rs.getInt(1));
                        beanClub.setClubName(rs.getString(2));;
                        beanClub.setClubDescription(rs.getString(6));
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
                    Log.e(TAG_readClubTable, "数据库异常" + e.getMessage());
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

        clubListAdapter = new ClubListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(clubListActivity.this);
        recv_clubList.setLayoutManager(layoutManager);
        recv_clubList.setAdapter(clubListAdapter);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_clubList_back) {
            onBackPressed();
        }
    }

    class ClubListAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(clubListActivity.this, R.layout.item_club_list, null);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final BeanClub beanClub_onBindViewHolder = clubList.get(position);

            Glide.with(holder.iv_icon)
                    .load(R.drawable.ludiwusi)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_icon);
            holder.tv_clubName.setText(beanClub_onBindViewHolder.getClubName());
            holder.tv_clubInfo.setText(beanClub_onBindViewHolder.getClubDescription());

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

    class MyViewHolder extends RecyclerView.ViewHolder {
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