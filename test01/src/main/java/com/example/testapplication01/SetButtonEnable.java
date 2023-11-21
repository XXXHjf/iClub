package com.example.testapplication01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testapplication01.R;

public class SetButtonEnable extends AppCompatActivity implements View.OnClickListener {

    private Button button_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_button_enable);

        Button button_enable = findViewById(R.id.button_enable);
        Button button_disable = findViewById(R.id.button_disable);
        button_view = findViewById(R.id.button_view);

        button_enable.setOnClickListener(this);
        button_disable.setOnClickListener(this);
        button_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_enable) {
            //启用按钮
            button_view.setEnabled(true);
            button_view.setTextColor(Color.BLACK);
        } else if (v.getId() == R.id.button_disable) {
            //禁用按钮
            button_view.setEnabled(false);
            button_view.setTextColor(Color.GRAY);
        } else if (v.getId() == R.id.button_view) {
            //禁用按钮
            button_view.setText("点击成功！" + System.currentTimeMillis());
        }
    }
}