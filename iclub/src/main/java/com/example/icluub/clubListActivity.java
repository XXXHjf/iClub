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
import android.widget.Button;
import android.widget.EditText;
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
    private Button button_clubList_search;
    private EditText et_clubList_search;
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
        et_clubList_search.getText().clear();
        new Thread_getSQL_clubList().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_clubList_back.setOnClickListener(this);
        findViewById(R.id.button_clubList_search).setOnClickListener(this);
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
        et_clubList_search = findViewById(R.id.et_clubList_search);
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
        } else if (view.getId() == R.id.button_clubList_search) {
            new Thread_get_searchList().start();
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

    /**
     * 自定义线程：获取所有社团
     */
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

    /**
     * 自定义线程；获取搜索的关键字的社团
     */
    private class Thread_get_searchList extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from club ";

                String inputText = et_clubList_search.getText().toString().trim();   // 去除首尾空格
                String[] keyWords = inputText.split("\\s+");   // 使用正则表达式匹配任意空白字符（包括空格、制表符等）
                List<String> conditions = new ArrayList<>();
                // 检查关键词数组是否为空
                if (keyWords.length > 0 && !keyWords[0].isEmpty()) {
                    // 处理关键词数组
                    for (String keyword : keyWords) {
                        String condition = "clubName LIKE '%" + keyword + "%'";
                        conditions.add(condition);
                    }
                    if ( !conditions.isEmpty() ) {
                        String joinedConditions = String.join(" OR ", conditions);
                        // 在原始 SQL 语句上添加条件
                        sql += "WHERE (" + joinedConditions + ") ";
                    }
                }else {
                    // 输入文本为空或只包含空白字符的情况
                    new Thread_getSQL_clubList().start();
                    return;
                }
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery();
                clubList = new ArrayList<>();
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