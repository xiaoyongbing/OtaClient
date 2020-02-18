package com.ido.otaclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.ido.otaclient.base.BaseActivity;
import com.ido.otaclient.device.DeviceFragment;
import com.ido.otaclient.font.FontActivity;
import com.ido.otaclient.fontb.FontBActivity;
import com.ido.otaclient.four.FourToOneActivity;
import com.ido.otaclient.home.HomeFragment;
import com.ido.otaclient.module.Veneer;
import com.ido.otaclient.nordic.NordicActivity;
import com.ido.otaclient.realtk.RealtkActivity;
import com.ido.otaclient.server.NIOHttpServer;
import com.ido.otaclient.three.ThreeToOneActivity;
import com.ido.otaclient.two.TwoToOneActivity;
import com.ido.otaclient.util.FragmentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-11 17:13
 * @description
 */
public class MainActivity extends BaseActivity {

    private HomeFragment mHomeFragment;
    private DeviceFragment mDeviceFragment;

    @BindView(R.id.main_tab)
    RadioGroup mMainTab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    /**
     * nordic 升级
     */
    @BindView(R.id.tv_nordic)
    TextView mTvNordic;

    /**
     * realtk升级
     */
    @BindView(R.id.tv_realtk)
    TextView mTvRealtk;
    /**
     * 四合一升级
     */
    @BindView(R.id.tv_four_to_one)
    TextView mTvFourToOne;

    private ActionBarDrawerToggle toggle;

    /**
     * 主界面之间的通信
     */
    private DeviceManagerModel deviceManagerModel;
    /**
     * 设备集合
     */
    private List<Veneer> mVeneerList = new ArrayList<>();
    /**
     * 权限申请
     */
    private List<String> permissionList = new ArrayList<>();

    private int requestPermissonCount = 0;

    private final int SDK_PERMISSION_REQUEST = 127;


    @Override
    protected void onCreateTask(Bundle savedInstanceState) {
        super.onCreateTask(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, HttpService.class);
        //startService(intent);

        /**
         * 启动服务
         */
        NIOHttpServer nioHttpServer = NIOHttpServer.getInstance();
        nioHttpServer.startServer();
        //设置权限
        initPermision();
        initData();
        initListener();
        initDrawerLayout();
    }

    private void initPermision() {
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
        permissionList.add(Manifest.permission.BLUETOOTH);
        getPermission();
    }






    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestPermissonCount < 5) {
            if (permissionList.size() != 0) {
                requestPermissonCount++;
                requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                        SDK_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SDK_PERMISSION_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionList.remove(permissions[i]);
                }
            }
            getPermission();
        }
    }

    private void initData() {

        deviceManagerModel = ViewModelProviders.of(this).get(DeviceManagerModel.class);
        mHomeFragment = HomeFragment.newInstance();
        FragmentUtil.replaceFragment(getSupportFragmentManager(),
                mHomeFragment,R.id.main_frame);

        deviceManagerModel.getVeneerModelLiveData().observe(this,veneers -> {
            mVeneerList = veneers;
        });
    }

    private void initDrawerLayout() {
        //注意：初始化的是drawerlayout整个大布局，不是初始化抽屉的那个id
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //drawerLayout.closeDrawers();
        //禁止手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //打开侧栏
        //drawerLayout.openDrawer(GravityCompat.END);
        //关闭侧栏
        drawerLayout.closeDrawers();
        //v4控件 actionbar上的抽屉开关，可以实现一些开关的动态效果
        /*toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.star_change, R.string.drawer_open
                , R.string.drawer_close) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);//抽屉关闭后
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);//抽屉打开后
            }
        };
        drawerLayout.setDrawerListener(toggle);*/
    }

    private void initListener() {
        mMainTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main_tab_device_rb:
                        mHomeFragment = HomeFragment.newInstance();
                        FragmentUtil.replaceFragment(getSupportFragmentManager(),
                                mHomeFragment,R.id.main_frame);
                        setConfigListener();
                        break;
                    case R.id.main_tab_media_rb:
                        mDeviceFragment = DeviceFragment.newInstance();
                        FragmentUtil.replaceFragment(getSupportFragmentManager(),
                                mDeviceFragment,R.id.main_frame);
                        break;
                }
            }
        });

        /**
         * 配置监听
         */
        mHomeFragment.setOnItemListener(new HomeFragment.OnItemListener() {
            @Override
            public void onConfig() {
                //打开侧栏
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }

    private void setConfigListener() {
        /**
         * 配置监听
         */
        mHomeFragment.setOnItemListener(new HomeFragment.OnItemListener() {
            @Override
            public void onConfig() {
                //打开侧栏
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    @OnClick(R.id.tv_nordic)
    public void toNordic(View view){

        NordicActivity.startActivity(MainActivity.this,mVeneerList);
    }


    @OnClick(R.id.tv_realtk)
    public void toRealtk(View view){
        RealtkActivity.startActivity(MainActivity.this,mVeneerList);
    }

    @OnClick(R.id.tv_four_to_one)
    public void toFourToOne(View view){
        FourToOneActivity.startActivity(MainActivity.this);
    }

    @OnClick(R.id.tv_two_to_one)
    public void toTwoToOne(View view){
        TwoToOneActivity.startActivity(MainActivity.this);
    }

    @OnClick(R.id.tv_three_to_one)
    public void toThreeToOne(View viwe){
        ThreeToOneActivity.startActivity(MainActivity.this);
    }

    @OnClick(R.id.tv_font_library)
    public void toFontLibrary(View view){
        FontActivity.startActivity(MainActivity.this);
    }

    @OnClick(R.id.tv_b_font_library)
    public void toFontBLibrary(View view){
        FontBActivity.startActivity(MainActivity.this);
    }

}
