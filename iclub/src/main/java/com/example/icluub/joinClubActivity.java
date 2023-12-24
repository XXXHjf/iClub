package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Beans.BeanClub;
import Beans.BeanUser;
import SPTools.LoginStatusSP;
import SPTools.userSP;
import tools.OperationPromptTool;
import tools.StatusTool;
import util.DBUtil;

public class joinClubActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout joinClub_rootItem;
    private ImageView iv_joinClub_back;
    private TextView tv_joinClub_name;
    private TextView tv_joinClub_stuID;
    private TextView tv_joinClub_belongCollege;
    private TextView tv_joinClub_majorClass;
    private TextView tv_joinClub_toJoinClub;
    private EditText et_joinClub_phone;
    private EditText et_joinClub_Wechat;
    private EditText et_joinClub_reason;
    private Button button_joinClub_commit;
    private BeanClub beanClub = null;
    private BeanUser beanUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);
        StatusTool.setStatusBar(getWindow());

        // 获取社团详细页面，传递过来的社团信息
        Bundle bundle = getIntent().getExtras();
        beanClub = (BeanClub) bundle.getSerializable("beanClub");

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        beanUser = userSP.getUserInfo(this);
        loadBaseInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_joinClub_back.setOnClickListener(this);
        button_joinClub_commit.setOnClickListener(this);
        findViewById(R.id.tv_createAct_to).setOnClickListener(this);
    }


    /**
     *初始化组件
     */
    private void initViews() {
        joinClub_rootItem = findViewById(R.id.joinClub_rootItem);
        iv_joinClub_back = findViewById(R.id.iv_joinClub_back);
        tv_joinClub_name = findViewById(R.id.tv_joinClub_name);
        tv_joinClub_stuID = findViewById(R.id.tv_joinClub_stuID);
        tv_joinClub_belongCollege = findViewById(R.id.tv_joinClub_belongCollege);
        tv_joinClub_majorClass = findViewById(R.id.tv_joinClub_majorClass);
        tv_joinClub_toJoinClub = findViewById(R.id.tv_joinClub_toJoinClub);
        et_joinClub_phone = findViewById(R.id.et_joinClub_phone);
        et_joinClub_Wechat = findViewById(R.id.et_joinClub_Wechat);
        et_joinClub_reason = findViewById(R.id.et_joinClub_reason);
        button_joinClub_commit = findViewById(R.id.button_joinClub_commit);
    }

    /**
     * 重写onClick方法
     * @param view  视图
     */
    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.iv_joinClub_back ) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_createAct_to) {
            Intent intent = new Intent(joinClubActivity.this, personalDataActivity.class);
            startActivity(intent);
        }else if ( view.getId() == R.id.button_joinClub_commit ) {
            OperationPromptTool.showConfirmDialog(joinClubActivity.this, "您确定申请加入该社团吗？", new Runnable() {
                @Override
                public void run() {
                    new Thread_post_joinClub().start();
                }
            });
        }
    }

    /**
     * 显示部分不需要填写的信息
     */
    private void loadBaseInfo() {
        tv_joinClub_name.setText(beanUser.getUserName());
        tv_joinClub_stuID.setText(beanUser.getUserID());
        tv_joinClub_belongCollege.setText(beanUser.getCollege());
        tv_joinClub_majorClass.setText(beanUser.getMajorClass());
        tv_joinClub_toJoinClub.setText(beanClub.getClubName());
        et_joinClub_phone.setText(beanUser.getPhoneNum());
    }

    /**
     * 自定义线程：申请加入社团
     */
    private class Thread_post_joinClub extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                // 获取要展示的信息
                String sql = "INSERT INTO ClubMember(clubID, userID, joinDate, WeChat, applyReason) VALUES (?,?,NOW(),?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, beanClub.getClubID());
                pst.setString(2, beanUser.getUserID());
                pst.setString(3, et_joinClub_Wechat.getText().toString());
                pst.setString(4, et_joinClub_reason.getText().toString());
                int rs = pst.executeUpdate();
                if ( rs == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.showMsg(joinClubActivity.this, "申请成功,请等待审核！");
                            onBackPressed();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OperationPromptTool.SnackEasyMsg(joinClub_rootItem, "申请失败,请稍后重试！");
                        }
                    });
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn);
            }
        }
    }

}