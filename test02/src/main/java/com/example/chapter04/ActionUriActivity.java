package com.example.chapter04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class ActionUriActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_uri);

        findViewById(R.id.btn_dial).setOnClickListener(this);
        findViewById(R.id.btn_myapp).setOnClickListener(this);
        findViewById(R.id.btn_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent =  new Intent();
        String phoneNO = "13869829323";

        if(view.getId() == R.id.btn_dial) {
            //设置意图---拨号
            intent.setAction(Intent.ACTION_DIAL);
            //声明一个拨号的Uri
            Uri uri = Uri.parse("tel:" + phoneNO);
            intent.setData(uri);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_sms) {
            intent.setAction(Intent.ACTION_SENDTO);
            Uri uri2 = Uri.parse("smsto:" + phoneNO);
            intent.setData(uri2);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_sms) {
            intent.setAction("android.intent.action.APP01");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }


    }
}