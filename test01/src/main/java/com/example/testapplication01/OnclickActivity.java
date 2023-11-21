package com.example.testapplication01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnclickActivity extends AppCompatActivity  implements View.OnClickListener{

    private static TextView text_onclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onclick);

        Button single_button_01 = findViewById(R.id.single_button01);
        Button public_button_02 = findViewById(R.id.public_button01);
        Button long_button_01 = findViewById(R.id.long_button_01);
        TextView text_onclick = findViewById(R.id.text_onclick);

        //定义单个点击事件
        single_button_01.setOnClickListener(new myOnclikListener(text_onclick));
        //多个点击事件
        public_button_02.setOnClickListener(this);
        //定义长点击事件
        long_button_01.setOnLongClickListener(view -> {
            text_onclick.setText("您点击了长点击事件");
            return true;    //返回true代表长按事件不会向外传递给父容器
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.public_button01){
            text_onclick.setText("public按钮触发成功");
        }
    }

    static class myOnclikListener implements View.OnClickListener {
        private final TextView text_onclick;

        public myOnclikListener(TextView text_onclick) {
            this.text_onclick = text_onclick;
        }

        @Override
        public void onClick(View view) {
            text_onclick.setText("single按钮触发成功");
        }
    }


}