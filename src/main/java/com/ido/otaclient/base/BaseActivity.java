package com.ido.otaclient.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.ido.otaclient.env.LanguageManager;

import butterknife.ButterKnife;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-11 14:55
 * @description
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
     final LifecycleHelper mLifecycleHelper = new LifecycleHelper(TAG);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageManager.setLanguage(newBase));
    }

    @Override
    protected final void onNewIntent(Intent intent) {
        mLifecycleHelper.logLifecycle("onNewIntent",true);
        super.onNewIntent(intent);
        onNewIntentTask(intent);
        mLifecycleHelper.logLifecycle("onNewIntent",false);
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        mLifecycleHelper.logLifecycle("onCreate",true);
        super.onCreate(savedInstanceState);
        onCreateTask(savedInstanceState);
        mLifecycleHelper.logLifecycle("onCreate",false);
    }

    @Override
    protected final void onResume() {
        mLifecycleHelper.logLifecycle("onResume",true);
        mLifecycleHelper.setInteractive(true);
        super.onResume();
        onResumeTask();
        mLifecycleHelper.logLifecycle("onResume",false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mLifecycleHelper.logLifecycle("onSaveInstanceState",true);
        super.onSaveInstanceState(outState);
        mLifecycleHelper.logLifecycle("onSaveInstanceState",false);
    }

    @Override
    protected final void onPause() {
        mLifecycleHelper.logLifecycle("onPause",true);
        mLifecycleHelper.setInteractive(false);
        super.onPause();
        onPauseTask();
        mLifecycleHelper.logLifecycle("onPause",false);
    }

    @Override
    protected final void onStart() {
        mLifecycleHelper.logLifecycle("onStart",true);
        onStartTask();
        super.onStart();
        mLifecycleHelper.logLifecycle("onStart",false);
    }

    @Override
    protected final void onRestart() {
        mLifecycleHelper.logLifecycle("onRestart",true);
        super.onRestart();
        onRestartTask();
        mLifecycleHelper.logLifecycle("onRestart",false);
    }

    @Override
    protected final void onStop() {
        mLifecycleHelper.logLifecycle("onStop",true);
        super.onStop();
        onStopTask();
        mLifecycleHelper.logLifecycle("onStop",false);
    }

    @Override
    protected final void onDestroy() {
        mLifecycleHelper.logLifecycle("onDestroy",true);
        super.onDestroy();
        onDestroyTask();
        mLifecycleHelper.logLifecycle("onDestroy",false);
    }

    protected void onNewIntentTask(Intent intent){}

    /**
     * {@link Activity#onCreate(Bundle)}
     * @param savedInstanceState
     */
    protected void onCreateTask(Bundle savedInstanceState) {  }

    /**
     * {@link Activity#onStart()}
     */
    protected void onStartTask(){  }

    /**
     * {@link Activity#onResume()}
     */
    protected void onResumeTask(){  }

    /**
     * {@link Activity#onPause()}
     */
    protected void onPauseTask(){  }

    /**
     * {@link Activity#onStop()}
     */
    protected void onStopTask(){  }

    /**
     * {@link Activity#onRestart()}
     */
    protected void onRestartTask(){  }

    /**
     * {@link Activity#onDestroy()}
     */
    protected void onDestroyTask(){  }


    @Override
    public void setContentView(@LayoutRes int layoutResID){
        setContentView(layoutResID,false);
    }

    public void setContentView(@LayoutRes int layoutResID,boolean translucent) {
        this.setContentView(layoutResID,translucent,0);
    }

    public void setContentView(@LayoutRes int layoutResID,boolean translucent,@ColorRes int color) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setTranslucentStatusBar(translucent);
        if(0 != color)
            setStatusBarColor(getResources().getColor(color));
    }

    /**
     * 获取statusBar 高度
     * @return 状态栏高度
     */
    public int getStatusBarHeight(){
        Resources resources = getResources();
        int id = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(id);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        final Window window = getWindow();
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    /**
     * 设置状态栏透明
     * @param translucent true 透明,false 不透明
     */
    private void setTranslucentStatusBar(boolean translucent){
        Window win = getWindow();
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        if(true == translucent){
            winParams.flags |= FLAG_TRANSLUCENT_STATUS;
        }
        else{
            winParams.flags &= ~FLAG_TRANSLUCENT_STATUS;
        }
        win.setAttributes(winParams);
    }

    /**
     * 当前界面是否可交互
     * @return
     */
    protected boolean isInteractive(){
        return mLifecycleHelper.isInteractive();
    }

    @Override
    public Resources getResources() {
        // 设置App字体大小不跟随系统字体调整而变化
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return resources;
    }
}
