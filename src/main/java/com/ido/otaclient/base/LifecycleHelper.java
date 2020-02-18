package com.ido.otaclient.base;


/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2019-12-11 15:43
 * @description
 */
public class LifecycleHelper {
    private static final boolean ENABLE = false;

    private final String TAG;

    private boolean isInteractive; // 是否可交互

    public LifecycleHelper(String tag){
        this.TAG = tag;
    }

    protected void logLifecycle(String methodName, boolean start) {
        if (ENABLE) {
            String prefix = start ? "START" : "END";
            IDLog.v(TAG, prefix + " " + methodName + ": Activity = " + toString());
        }
    }

    /**
     * 设置可交互状态
     * @param interactive
     */
    protected void setInteractive(boolean interactive){
        this.isInteractive = interactive;
    }

    /**
     * 是否可交互
     * @return
     */
    protected boolean isInteractive(){
        return isInteractive;
    }
}
