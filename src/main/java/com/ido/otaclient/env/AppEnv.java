package com.ido.otaclient.env;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 用于获取环境变量，如上下文context
 * 注意：此类为所有工具类的基础，工具类从Env类中获取上下文
 */
public class AppEnv {

    public static final String TAG = "AppEnv";

    private static AppEnv sAppEnv;

    /**
     * 程序上下文
     */
    private Context mContext;

    public static void initialize(Context context){
        if(sAppEnv == null){
            sAppEnv = new AppEnv(context);
        }
        else {
            sAppEnv.mContext = context;
        }
    }

    public static AppEnv instance(){
        return Objects.requireNonNull(sAppEnv,"AppEnv was not initialized.");
    }

    private AppEnv(Context context){
        this.mContext = context;
    }

    /**
     * 获取当前程序的上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取当前创建的进程名称，区分主进程和消息推送进程
     */
    public String getProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listInfo = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : listInfo) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 记录程序启动时间，在application创建的时候统一设置
     */
    private long sAppStartTime = 0;
    public void setAppStartTime() {
        sAppStartTime = System.currentTimeMillis();
    }

    /**
     * 获取程序当前运行时间点
     */
    public long getAppRunTime() {
        return System.currentTimeMillis() - sAppStartTime;
    }

    /**
     * 判断当前线程是否是主线程
     */
    public boolean isMainThread(){
        Thread mainThread = mContext.getMainLooper().getThread();
        Thread currentThread = Thread.currentThread();
        return mainThread.getId() == currentThread.getId();
    }

    private Locale getLocale() {
        return LanguageManager.getLocale(getContext().getResources());
    }
}