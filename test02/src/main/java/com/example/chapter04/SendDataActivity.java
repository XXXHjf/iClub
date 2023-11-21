package com.example.chapter04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import util.tansition;

public class SendDataActivity extends AppCompatActivity {

    private TextView tv_senddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        tv_senddata = findViewById(R.id.tv_senddata);
        findViewById(R.id.bt_senddata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendDataActivity.this, RecentDataActivity.class);
                //创建一个新包裹
                Bundle bundle = new Bundle();
                bundle.putString("request_time", tansition.getCurrentTime_String());
                bundle.putString("request_text", tv_senddata.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}