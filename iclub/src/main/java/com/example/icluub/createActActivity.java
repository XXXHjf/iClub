package com.example.icluub;

import static tools.OperationPromptTool.SnackEasyMsg;
import static tools.OperationPromptTool.showMsg;
import static tools.TransitionTool.StringToTimestamp;
import static tools.TransitionTool.TimeMillisToString;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import Beans.BeanClub;
import Beans.BeanUser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tools.CameraUtils;
import tools.OperationPromptTool;
import tools.StatusTool;
import util.DBUtil;
import util.OSSUpload;
import SPTools.userSP;

public class createActActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout root_createActivity;
    private ImageView iv_createAct_back;
    private EditText ev_createAct_actNme;
    private TextView tv_createAct_stuID;
    private TextView tv_createAct_belongCollege;
    private TextView tv_createAct_applyPerson;
    private TextView tv_createAct_actStartTime;
    private TextView tv_createAct_actEndTime;
    private TextView tv_createAct_applyEndTime;
    private EditText et_createAct_actPlace;
    private EditText ev_createAct_maxNum;
    private ImageView iv_createAct_addCover;
    private EditText et_createAct_content;
    private Button button_createAct_apply;
    private BeanClub beanClub = null;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_act);

        StatusTool.setStatusBar(getWindow());
        initViews();

        Bundle bundle = getIntent().getExtras();
        beanClub = (BeanClub) bundle.getSerializable("beanClub");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadBaseInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        button_createAct_apply.setOnClickListener(this);
        iv_createAct_back.setOnClickListener(this);
        tv_createAct_actStartTime.setOnClickListener(this);
        tv_createAct_actEndTime.setOnClickListener(this);
        tv_createAct_applyEndTime.setOnClickListener(this);
        iv_createAct_addCover.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        root_createActivity = findViewById(R.id.root_createActivity);
        iv_createAct_back = findViewById(R.id.iv_createAct_back);
        ev_createAct_actNme = findViewById(R.id.ev_createAct_actNme);
        tv_createAct_stuID = findViewById(R.id.tv_createAct_stuID);
        tv_createAct_belongCollege = findViewById(R.id.tv_createAct_belongCollege);
        tv_createAct_applyPerson = findViewById(R.id.tv_createAct_applyPerson);
        tv_createAct_actStartTime = findViewById(R.id.tv_createAct_actStartTime);
        tv_createAct_actEndTime = findViewById(R.id.tv_createAct_actEndTime);
        tv_createAct_applyEndTime = findViewById(R.id.tv_createAct_applyEndTime);
        et_createAct_actPlace = findViewById(R.id.et_createAct_actPlace);
        ev_createAct_maxNum = findViewById(R.id.ev_createAct_maxNum);
        iv_createAct_addCover = findViewById(R.id.iv_createAct_addCover);
        et_createAct_content = findViewById(R.id.et_createAct_content);
        button_createAct_apply = findViewById(R.id.button_createAct_apply);
    }

    /**
     * 加载基础数据
     */
    private void loadBaseInfo() {
        tv_createAct_stuID.setText(beanClub.getClubName());
        tv_createAct_belongCollege.setText(beanClub.getGuidingUnit());
        BeanUser applier = userSP.getUserInfo(getApplicationContext());
        String applyName = applier.getUserName();
        tv_createAct_applyPerson.setText(applyName);
    }

    /**
     * 重写OnClick方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_createAct_apply) {
            if (ev_createAct_actNme.getText().toString().isEmpty() || tv_createAct_actStartTime.getText().toString().isEmpty() ||
                    tv_createAct_actEndTime.getText().toString().isEmpty() || tv_createAct_applyEndTime.getText().toString().isEmpty() ||
                    ev_createAct_maxNum.getText().toString().isEmpty() || et_createAct_actPlace.getText().toString().isEmpty() ||
                    imagePath == null) {
                SnackEasyMsg(root_createActivity, "请补全活动信息");
            } else if ( Integer.parseInt(ev_createAct_maxNum.getText().toString()) > 1000) {
                SnackEasyMsg(root_createActivity, "活动人数最大为1000！");
            } else {
                OperationPromptTool.showConfirmDialog(createActActivity.this, "您确定申请创办此活动吗？\n(申请后不可撤销、内容不可修改)", new Runnable() {
                    @Override
                    public void run() {
                        new Thread_commitAct().start();
                    }
                });
            }
        } else if (view.getId() == R.id.iv_createAct_back) {
            onBackPressed();
        }  else if (view.getId() == R.id.tv_createAct_actStartTime) {
            CardDatePickerDialog.Builder builder = new CardDatePickerDialog.Builder(this)
                    .setTitle("活动开始时间")
                    .showBackNow(false)
                    .showFocusDateInfo(false)   //是否用一个小横条显示选中的日期
                    .setBackGroundModel(CardDatePickerDialog.STACK) //显示模式--顶部圆角
                    .setWrapSelectorWheel(false)    //是否循环滚动
                    .showDateLabel(true)    //是否显示单位
                    .setThemeColor(ContextCompat.getColor(this, R.color.blue))
                    .setDisplayType(0, 1, 2, 3, 4)    //设置显示的标签
                    .setLabelText("年", "月", "日", "时", "分", "秒")
                    .setOnChoose("确定", new Function1<Long, Unit>() {
                        @Override
                        public Unit invoke(Long timeInMillis) {
                            String stringTime = TimeMillisToString(timeInMillis);
                            tv_createAct_actStartTime.setText(stringTime);
                            return null;
                        }
                    });
            String stringApply = tv_createAct_applyEndTime.getText().toString();
            // 如果选择过报名截止时间，设置活动开始时间在报名截止时间之后
            if (!stringApply.isEmpty()) {
                Timestamp applyEndTime = StringToTimestamp(stringApply);
                builder.setMinTime(applyEndTime.getTime())
                        .build().show();
            }
            // 否则设置活动开始最小时间为现在
            else {
                builder.setMinTime(System.currentTimeMillis())
                        .build().show();
            }
        } else if (view.getId() == R.id.tv_createAct_actEndTime) {
            CardDatePickerDialog.Builder builder = new CardDatePickerDialog.Builder(this)
                    .setTitle("活动结束时间")
                    .showBackNow(false)
                    .showFocusDateInfo(false)   //是否用一个小横条显示选中的日期
                    .setBackGroundModel(CardDatePickerDialog.STACK) //显示模式--顶部圆角
                    .setWrapSelectorWheel(false)    //是否循环滚动
                    .showDateLabel(true)    //是否显示单位
                    .setThemeColor(ContextCompat.getColor(this, R.color.blue))
                    .setDisplayType(0, 1, 2, 3, 4)    //设置显示的标签
                    .setLabelText("年", "月", "日", "时", "分", "秒")
                    .setOnChoose("确定", new Function1<Long, Unit>() {
                        @Override
                        public Unit invoke(Long timeInMillis) {
                            String stringTime = TimeMillisToString(timeInMillis);
                            tv_createAct_actEndTime.setText(stringTime);
                            return null;
                        }
                    });
            String stringStart = tv_createAct_actStartTime.getText().toString();
            // 如果选择过活动开始时间，设置活动结束时间在活动开始时间之后
            if (!stringStart.isEmpty()) {
                Timestamp actStartTime = StringToTimestamp(stringStart);
                builder.setMinTime(actStartTime.getTime())
                        .build().show();
            }
            // 如果还没选择过活动开始时间，提示先选择活动开始时间
            else {
                SnackEasyMsg(root_createActivity, "请先选择活动开始时间!");
            }
        } else if (view.getId() == R.id.tv_createAct_applyEndTime) {
            CardDatePickerDialog.Builder builder = new CardDatePickerDialog.Builder(this)
                    .setTitle("报名截止时间")
                    .showBackNow(false)
                    .showFocusDateInfo(false)   //是否用一个小横条显示选中的日期
                    .setBackGroundModel(CardDatePickerDialog.STACK) //显示模式--顶部圆角
                    .setWrapSelectorWheel(false)    //是否循环滚动
                    .showDateLabel(true)    //是否显示单位
                    .setThemeColor(ContextCompat.getColor(this, R.color.blue))
                    .setDisplayType(0, 1, 2, 3, 4)    //设置显示的标签
                    .setLabelText("年", "月", "日", "时", "分", "秒")
                    .setOnChoose("确定", new Function1<Long, Unit>() {
                        @Override
                        public Unit invoke(Long timeInMillis) {
                            String stringTime = TimeMillisToString(timeInMillis);
                            tv_createAct_applyEndTime.setText(stringTime);
                            return null;
                        }
                    });
            String stringStart = tv_createAct_actStartTime.getText().toString();
            // 如果选择过活动开始时间，设置报名截止时间最大为活动开始时间，最小为现在
            if (!stringStart.isEmpty()) {
                Timestamp actStartTime = StringToTimestamp(stringStart);
                builder.setMinTime(System.currentTimeMillis()).setMaxTime(actStartTime.getTime())
                        .build().show();
            }
            // 否则设置报名截至时间最小为现在
            else {
                builder.setMinTime(System.currentTimeMillis())
                        .build().show();
            }
        } else if (view.getId() == R.id.iv_createAct_addCover) {
            startActivityForResult(CameraUtils.getSelectPhotoIntent(), CameraUtils.SELECT_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null )
            return;
        if (requestCode == CameraUtils.SELECT_PHOTO) {
            imagePath = CameraUtils.getImageOnKitKatPath(data, createActActivity.this);
            if (!TextUtils.isEmpty(imagePath)) {
                // 显示图片
                Glide.with(this)
                        .load(imagePath)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(iv_createAct_addCover);
            } else {
                showMsg(this, "图片获取失败");
            }
        }
    }

    /**
     * 自定义线程：上传选中的照片到OSS中
     */
    private class Thread_uploadOSS extends Thread {
        @Override
        public void run() {
            // 用当前的时间戳作为上传的照片的名字上传到OSS里，数据库对应位置存储图片的下载路径
            String fileName = System.currentTimeMillis() + "." + CameraUtils.getImageType(imagePath);
            OSSUpload.uploadOSS(createActActivity.this, "actCover/" + fileName, imagePath);
        }
    }

    /**
     * 自定义线程：上传活动申请
     */
    private class Thread_commitAct extends Thread {
        @Override
        public void run() {
            Connection conn = null;
            try {
                // 用当前的时间戳作为上传的照片的名字上传到OSS里，数据库对应位置存储图片的下载路径
                String fileName = System.currentTimeMillis() + "." + CameraUtils.getImageType(imagePath);

                conn = DBUtil.getConnection();
                String sql = "INSERT INTO ClubActivity (clubID, actTitle, actDescription, actCover, actLocation, maxNum, startTime, endTime, closeTime) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, beanClub.getClubID());
                pst.setString(2, ev_createAct_actNme.getText().toString());
                pst.setString(3, et_createAct_content.getText().toString());
                pst.setString(4, OSSUpload.getActCoverPrefix() + fileName);
                pst.setString(5, et_createAct_actPlace.getText().toString());
                pst.setInt(6, Integer.parseInt(ev_createAct_maxNum.getText().toString()));
                pst.setTimestamp(7, StringToTimestamp(tv_createAct_actStartTime.getText().toString()));
                pst.setTimestamp(8, StringToTimestamp(tv_createAct_actEndTime.getText().toString()));
                pst.setTimestamp(9, StringToTimestamp(tv_createAct_applyEndTime.getText().toString()));
                int rs = pst.executeUpdate();
                if (rs == 1) {
                    OSSUpload.uploadOSS(createActActivity.this, "actCover/" + fileName, imagePath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsg(createActActivity.this, "申请成功!");
                            onBackPressed();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsg(createActActivity.this, "创建失败!");
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