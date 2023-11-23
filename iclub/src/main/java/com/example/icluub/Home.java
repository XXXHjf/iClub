package com.example.icluub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.BannerBean_resource;
import homefragments.Fragment_find;
import homefragments.Fragment_home;
import homefragments.Fragment_mine;
import util.BottomMenuFragmentsManager;
import util.DBUtil;
import util.DbException;

public class Home extends AppCompatActivity {
    //如果你是在Fragment中，则把this换成getActivity()
    private final RxPermissions rxPermissions = new RxPermissions(this);  //权限请求
    public static boolean hasPermissions = false;  //是否拥有权限
    private Fragment_home fragment_home;
    private Fragment_find fragment_find;
    private Fragment_mine fragment_mine;
    private Fragment[] fragments;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = this.getWindow();
        // 设置状态栏和底部导航栏为沉浸式(xml文件里设置了android:fitsSystemWindows="true"所以不会完全嵌入状态栏)
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        // 设置顶部状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(Color.TRANSPARENT);      设置底部导航栏(全屏手机最底下那个黑杠杠)为透明
        // 设置顶部状态栏的图标、字体为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 权限请求
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {  //申请成功
                        showMsg("已获取相机和写权限");
                        hasPermissions = true;
                    } else {//申请失败
                        showMsg("权限未开启");
                        hasPermissions = false;
                    }
                });


        // 底部导航栏选项监听器           (注意setOnNavigationItemSelectedListener()方法已经被弃用)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_bottom_menu);
//        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.navigation_home) {
                    replaceFragment(fragments[0]);
                    return true;
                } else if (item.getItemId() == R.id.navigation_find) {
                    replaceFragment(fragments[1]);
                    return true;
                } else if (item.getItemId() == R.id.navigation_mine) {
                    replaceFragment(fragments[2]);
                    return true;
                }
                return false;
            }
        });

        // 创建各个Fragment实例
        fragment_home = new Fragment_home();
        fragment_find = new Fragment_find();
        fragment_mine = new Fragment_mine();

        // 将Fragment实例逐个添加到数组中并且隐藏,之后显示第一个Fragment
        fragments = new Fragment[] {fragment_home, fragment_find, fragment_mine};
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment fragment : fragments) {
            addFragment(fragment);
            hideFragment(fragment);
        }
        showFragment(fragments[0]);

    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_fragments, fragment); // 将 Fragment 添加到容器中
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_fragments, fragment); // 替换当前显示的 Fragment
        transaction.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment); // 显示指定的 Fragment
        transaction.commit();
    }

    private void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment); // 隐藏指定的 Fragment
        transaction.commit();
    }

    // Toast提示
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }




}