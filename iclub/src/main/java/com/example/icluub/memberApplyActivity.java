package com.example.icluub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

import Beans.BeanClubActivity;
import Beans.BeanUser;
import RecyclerViewHolder.MemberApplyHolder;
import tools.OperationPromptTool;
import tools.StatusTool;
import util.DBUtil;

public class memberApplyActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_memberApply_back;
    private RecyclerView recv_memberApply;
    private MemberApplyAdapter adapter;
    private List<BeanUser> userList = new ArrayList<>();
    private List<String> weChatList = new ArrayList<>();
    private List<String> reasonList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_apply);

        StatusTool.setStatusBar(getWindow());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("userList")) {
                Object userListObject = bundle.getSerializable("userList");
                if (userListObject instanceof List<?>) {
                    userList = (List<BeanUser>) userListObject;
                }
            }
        }
        weChatList = bundle.getStringArrayList("weChatList");
        reasonList = bundle.getStringArrayList("reasonList");
        
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.iv_memberApply_back).setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        userList = new ArrayList<>();
        weChatList= new ArrayList<>();
        reasonList = new ArrayList<>();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        recv_memberApply = findViewById(R.id.recv_memberApply);

        adapter = new MemberApplyAdapter();
        recv_memberApply.setLayoutManager(new LinearLayoutManager(this));
        recv_memberApply.setAdapter(adapter);
    }

    /**
     * 重写onClick方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_memberApply_back) {
            onBackPressed();
        }
    }

    /**
     * 自定义适配器：申请入社的列表
     */
    private class MemberApplyAdapter extends RecyclerView.Adapter<MemberApplyHolder> {
        @NonNull
        @Override
        public MemberApplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(memberApplyActivity.this).inflate(R.layout.item_membership_apply, parent, false);
            MemberApplyHolder holder = new MemberApplyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MemberApplyHolder holder, int position) {
            final BeanUser bean = userList.get(position);
            final String weChat = weChatList.get(position);
            final String reason = reasonList.get(position);

            holder.tv_mbrApply_name.setText(bean.getUserName());
            holder.tv_mbrApply_college.setText(bean.getCollege());
            holder.tv_mbrApply_major.setText(bean.getMajorClass());
            holder.tv_mbrApply_phone.setText(bean.getPhoneNum());
            holder.tv_mbrApply_weChat.setText(weChat);
            holder.tv_mbrApply_reason.setText(reason);
            Glide.with(memberApplyActivity.this)
                    .load(bean.getProfileURL())
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.iv_mbrApply_icon);

            holder.button_mbrApply_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread_post_passApply(bean.getUserID(), holder.getAbsoluteAdapterPosition()).start();
                }
            });
            holder.button_mbrApply_unPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread_post_unPassApply(bean.getUserID(), holder.getAbsoluteAdapterPosition()).start();
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    private class Thread_post_passApply extends Thread {
        private String userID;
        private int position;
        private Thread_post_passApply(String userID, int position) {
            this.userID = userID;
            this.position = position;
        }
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "UPDATE ClubMember SET ifPassed=1 WHERE userID=? AND clubID=? AND ifPassed=0";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, userID);
                pst.setInt(2, clubManageActivity.clubID);
                int rs = pst.executeUpdate();
                if (rs == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(memberApplyActivity.this,"审批通过！");
                            userList.remove(position);
                            reasonList.remove(position);
                            weChatList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(memberApplyActivity.this,"审批未通过，请稍后重试");
                        }
                    });
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }


    private class Thread_post_unPassApply extends Thread {
        private String userID;
        private int position;
        private Thread_post_unPassApply(String userID, int position) {
            this.userID = userID;
            this.position = position;
        }
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "DELETE FROM ClubMember WHERE userID=? AND clubID=? AND ifPassed=0";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, userID);
                pst.setInt(2, clubManageActivity.clubID);
                int rs = pst.executeUpdate();
                if (rs == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(memberApplyActivity.this,"拒绝审批成功！");
                            userList.remove(position);
                            reasonList.remove(position);
                            weChatList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(memberApplyActivity.this,"拒绝审批失败，请稍后重试");
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