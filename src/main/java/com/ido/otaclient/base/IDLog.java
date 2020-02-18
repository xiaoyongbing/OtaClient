package com.ido.otaclient.base;

import android.util.Log;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-11 15:42
 * @description
 */
public class IDLog {
    private static boolean LOG_ENABLE = true;

    public static void setLogEnable(boolean enable){
        LOG_ENABLE = enable;
    }

    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e){
        if(LOG_ENABLE){
            Log.e(tag,msg,e);
        }
    }

    public static void w(String tag, String msg) {
        if(LOG_ENABLE){
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        if(LOG_ENABLE){
            Log.w(tag, msg,t);
        }
    }

    public static void i(String msg) {
        if (LOG_ENABLE) {
            Log.i(getTag(), msg);
        }
    }

    public static void v(String msg) {
        if (LOG_ENABLE) {
            Log.v(getTag(), msg);
        }
    }

    public static void d(String msg) {
        if (LOG_ENABLE) {
            Log.d(getTag(), msg);
        }
    }

    public static void e(String msg) {
        if (LOG_ENABLE) {
            Log.e(getTag(), msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (LOG_ENABLE) {
            Log.e(getTag(), msg, e);
        }
    }

    public static void w(String msg) {
        if (LOG_ENABLE) {
            Log.w(getTag(), msg);
        }
    }

    public static void w(String msg, Throwable t) {
        if (LOG_ENABLE) {
            Log.w(getTag(), msg, t);
        }
    }

    private static String getTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        String methodName = caller.getMethodName();
        int lineNumber = caller.getLineNumber();
        String tag = "IDLog: " + callerClazzName + "#" + methodName + "(line:" + lineNumber + ")";
        return tag;
    }
}
