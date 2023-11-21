package com.example.testapplication01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageViewActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView iv_sleep = findViewById(R.id.iv_sleep);
        ImageButton ib_01 = findViewById(R.id.ib_01);

        ib_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_sleep.setImageResource(R.drawable.loopy_sleep);
            }
        });

    }
}