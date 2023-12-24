package com.example.icluub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tbruyelle.rxpermissions3.RxPermissions;

import fragments.home.Fragment_find;
import fragments.home.Fragment_home;
import fragments.home.Fragment_mine;
import tools.FragmentTool;
import tools.OperationPromptTool;
import tools.StatusTool;
import SPTools.LoginStatusSP;
import SPTools.appStatusSP;

public class homeActivity extends AppCompatActivity {
    private final RxPermissions rxPermissions = new RxPermissions(this);  //权限请求
    private BottomNavigationView bottomNavigationView;
    private Fragment_home fragment_home;
    private Fragment_find fragment_find;
    private Fragment_mine fragment_mine;
    private Fragment[] fragments;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 设置状态栏
        StatusTool.setStatusBar(this.getWindow());

        if ( !LoginStatusSP.getHasLogin(this) ) {
            Intent intent = new Intent(homeActivity.this, loginActivity.class);
            startActivity(intent);
            return;
        }

        SharedPreferences sp = this.getSharedPreferences(appStatusSP.fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if ( !sp.contains("hasCameraNotice") ) {
            editor.putBoolean("hasCameraNotice", false);
            Boolean hasCameraNotice = sp.getBoolean("hasCameraNotice", false);
            if(hasCameraNotice == false) {
                // 权限请求
                rxPermissions
                        .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {  //申请成功
                                editor.putBoolean("hasCameraNotice", true);
                                editor.putBoolean("hasPermissions", true);
                                editor.apply();
                                OperationPromptTool.showMsg(homeActivity.this, "已获取相机和相册权限");
                            } else {    //申请失败
                                editor.putBoolean("hasPermissions", false);
                                editor.apply();
                                OperationPromptTool.showMsg(homeActivity.this, "权限未开启");
                            }
                        });
            }
        }

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    FragmentTool.hideFragment(fragments[1], fragmentManager);
                    FragmentTool.hideFragment(fragments[2], fragmentManager);
                    FragmentTool.showFragment(fragments[0], fragmentManager);
                    return true;
                } else if (item.getItemId() == R.id.navigation_find) {
                    FragmentTool.hideFragment(fragments[0], fragmentManager);
                    FragmentTool.hideFragment(fragments[2], fragmentManager);
                    FragmentTool.showFragment(fragments[1], fragmentManager);
                    return true;
                } else if (item.getItemId() == R.id.navigation_mine) {
                    FragmentTool.hideFragment(fragments[0], fragmentManager);
                    FragmentTool.hideFragment(fragments[1], fragmentManager);
                    FragmentTool.showFragment(fragments[2], fragmentManager);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 初始化
     */
    private void initViews() {
        bottomNavigationView = findViewById(R.id.bnv_bottom_menu);
        // 创建各个Fragment实例
        fragment_home = new Fragment_home();
        fragment_find = new Fragment_find();
        fragment_mine = new Fragment_mine();
        // 将Fragment实例逐个添加到数组中并且隐藏,之后显示第一个Fragment
        fragments = new Fragment[]{fragment_home, fragment_find, fragment_mine};
        FragmentTool.addFragment(R.id.fl_fragments, fragments[0], fragmentManager);
        FragmentTool.addFragment(R.id.fl_fragments, fragments[1], fragmentManager);
        FragmentTool.addFragment(R.id.fl_fragments, fragments[2], fragmentManager);
        FragmentTool.hideFragment(fragments[1], fragmentManager);
        FragmentTool.hideFragment(fragments[2], fragmentManager);
        FragmentTool.showFragment(fragments[0], fragmentManager);
    }

}