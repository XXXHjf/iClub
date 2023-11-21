package com.example.chapter04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RecentDataActivity extends AppCompatActivity {

    private TextView tv_recentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_data);

        tv_recentdata = findViewById(R.id.tv_recentdata);
        //从上一个页面中获取包裹
        Bundle bundle = getIntent().getExtras();
        String request_time = bundle.getString("request_time");
        String request_text = bundle.getString("request_text");
        String desc = String.format("成功收到请求消息：\n当前时间为：%s\n获取的信息为：%s", request_time, request_text);
        tv_recentdata.setText(desc);
    }
}