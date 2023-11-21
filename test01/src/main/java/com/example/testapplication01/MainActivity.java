package com.example.testapplication01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bSubmit = findViewById(R.id.bSubmit);

        EditText etInput = findViewById(R.id.inputText);
        Button button01 = findViewById(R.id.button01);

        //注册监听器
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sInput = etInput.getText().toString();
                Toast.makeText(getApplicationContext(), "提交成功！" + sInput, Toast.LENGTH_SHORT).show();
            }
        });

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OnclickActivity.class);
                startActivity(intent);
            }
        });
    }
}
