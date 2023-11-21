package com.example.testapplication01;

import static android.graphics.Color.WHITE;
import static android.graphics.Color.blue;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import util.tansition;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        Button b1 = findViewById(R.id.b_changeTextColor);

        //获取tv2的布局参数（含高度和宽度）
        ViewGroup.LayoutParams params = tv2.getLayoutParams();
        //修改布局参数中的宽度数值,注意默认是px单位，需要把dp数值转换成px
        params.width = tansition.dipTopx(this, 500);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setTextColor(Color.GRAY);
                tv1.setTextSize(80);

                //设置布局参数
                tv2.setLayoutParams(params);
            }
        });
    }
}
