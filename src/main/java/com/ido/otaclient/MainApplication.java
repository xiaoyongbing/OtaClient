package com.ido.otaclient;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Process;

import com.ido.otaclient.env.AppEnv;
import com.ido.otaclient.env.LanguageManager;


/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-18 09:31
 * @description
 */
public class MainApplication extends Application {

    private static final String TAG = Application.class.getSimpleName();

    /** 程序进程名字 */
    public  static final String PROCESS_NAME_MAIN  = BuildConfig.APPLICATION_ID; // 主进程
    private static final String PROCESS_NAME_BACK  = BuildConfig.APPLICATION_ID+":remote";//后台进程（暂未使用）

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖屏切换重新设置语言
        LanguageManager.setLanguage(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageManager.setLanguage(base));
    }

    /**
     * onCreate
     * PS：程序内的所有组件的初始化在这里执行
     */
    @Override
    public void onCreate() {
        super.onCreate();

        initAppEnv();
        //initProcess();
        /*initCrashStatistics();
        listenerAppState();
//        setupLeakCanary();
        // 初始化更新到1.8.7的标记
        VideoFixHelper.getInstance().initUpdateTo187Time();
        AppDatabase.getInstance();
        StabilizerService.getInstance().init(this, BuildConfig.DEBUG_ENABLE);
        CrashHandler.init(this);*/
        //BLEManager.onApplicationCreate(this);
    }

    /**
     * 置程序环境（必须先设置）
     */
    protected void initAppEnv(){
        AppEnv.initialize(this);
        AppEnv.instance().setAppStartTime();
    }

    /*protected void initProcess(){
        String processName = ProcessUtil.getProcessName(this);
        // 前台进程
        if (PROCESS_NAME_MAIN.equals(processName)) {
            // 创建桌面快捷方式
            //ShortcutUtil.createShortcut(this, R.mipmap.ic_launcher, SplashActivity.class);
            //  初始化组件
            //initMainComponents();
            // 启动后台服务
            startBackService();
        }
        // 如果是后台进程启动，只初始化后台进程相关组件
        else if(PROCESS_NAME_BACK.equals(processName)){
            // 初始化组件
            initBackComponents();
        }
        // 其他：暂无
        else {

        }
    }*/

    /**
     * 初始化Crash统计
     */
    /*protected void initCrashStatistics(){
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }*/

    /**
     * 初始化主进程的组件
     */
    /*public void initMainComponents() {
        ActivityLifecycle.getInstance().register(this);

        Community.init(this);

        // lean cloud
        final String LC_ID_RELEASE  = "2bcK6NAJvoNEw8e8GqtFrT4r-gzGzoHsz";
        final String LC_ID_DEBUG    = "FbxQwaGlWrU6JMbvqbYiUGpo-gzGzoHsz" ;
        final String LC_KEY_RELEASE = "PLLxcAN5hUORwRaGHPBCASKg";
        final String LC_KEY_DEBUG   = "lffhc4enOaWpWCuBUwuEvKRJ";

        String applicationId = BuildConfig.DEBUG_ENABLE ? LC_ID_DEBUG : LC_ID_RELEASE;
        String key = BuildConfig.DEBUG_ENABLE ? LC_KEY_DEBUG : LC_KEY_RELEASE;
        AVOSCloud.initialize(this, applicationId, key);
        AVOSCloud.setDebugLogEnabled(false);

        initPush();
    }*/


    /*private void initPush() {
        // 设置device_id
        Community.setDevicesId((String) AnalyticsUtils.getDeviceInfo(this).get("device_id"));
        // LeanCloud 推送
        Community.PushManager.init(this, SplashActivity.class, R.drawable.logo_notify,
                NotifyClickReceiver.class);
        // LeanCloud 混合推送
        Community.PushManager.registerHMSPush(this, "zyplay_hw");
        Community.PushManager.registerXiaomiPush(this, "2882303761517729140",
                "5661772998140", "zyplay_mi");
    }*/


    /**
     *  启动后台服务
     */
    private void startBackService(){

        // 延后启动，方便前台正式启动后，后台才有顺起来
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //BackstageServiceManager.instance().startBackstageService(getApplicationContext());
            }
        };
        new Handler().postDelayed(runnable, (2 * 1000));
    }


    /**
     *  初始化后台进程的组件
     */
    private void initBackComponents(){
        // ...
    }

    /**
     * 销毁主进程的组件
     */
    /*private void deinitMainComponents(){
        //注销接口
        ActivityLifecycle.getInstance().unregister(this);
    }*/

    /*private void listenerAppState() {
        ActivityLifecycle
                .getInstance()
                .addOnAppStatusListener(new ActivityLifecycle.OnAppStatusListener() {
                    @Override
                    public void onFront() {
                        StabilizerService.getInstance().enableHeartbeat();
                    }

                    @Override
                    public void onBack() {
                        StabilizerService.getInstance().disableHeartbeat();
                    }
                });
    }*/

    /**
     * 程序主进程退出
     */
    public void exitMainProcess() {
        // 销毁组件
        //deinitMainComponents();
        // 强杀进程
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Process.killProcess(Process.myPid());
            }
        };
        new Handler().postDelayed(runnable, 100);
    }

}
