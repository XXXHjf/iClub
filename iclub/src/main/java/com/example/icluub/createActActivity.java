package com.example.icluub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tools.StatusTool;

public class createActActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_act);

        StatusTool.setStatusBar(getWindow());
        initViews();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
    }
}