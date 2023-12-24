package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import SPTools.LoginStatusSP;
import SPTools.userSP;
import tools.OperationPromptTool;
import tools.SPUtils;

public class systemSettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.view_systemSetting_logout).setOnClickListener(this);
        findViewById(R.id.iv_systemSetting_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.view_systemSetting_logout ) {
            OperationPromptTool.showConfirmDialog(systemSettingActivity.this, "您确定退出登陆吗？", new Runnable() {
                @Override
                public void run() {
                    LoginStatusSP.clearFile(getApplicationContext());
                    userSP.clearFile(getApplicationContext());
                    SPUtils.clear(getApplicationContext());
                    Intent intent = new Intent(systemSettingActivity.this, loginActivity.class);
                    startActivity(intent);
                }
            });
        } else if ( view.getId() == R.id.iv_systemSetting_back ) {
            onBackPressed();
        }
    }
}