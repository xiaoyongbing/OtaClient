package com.ido.otaclient;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ido.otaclient.base.IDLog;
import com.ido.otaclient.module.Device;
import com.ido.otaclient.module.Veneer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-15 14:40
 * @description
 */
public class DeviceManagerModel extends ViewModel {
    private static final String TAG = "DeviceManagerModel";
    /**
     * 设备的状态
     */
    private MutableLiveData<List<Device>> mDeviceListLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Veneer>> mVeneerListLiveData = new MutableLiveData<>();

    /**
     * 当前
     */
    private MutableLiveData<Long> playTime = new MutableLiveData<>();

    /**
     * 时间
     */
    private long mTime;


    /**
     * 返回ip地址集合
     *
     * @return livedata
     */
    public MutableLiveData<List<Veneer>> getVeneerModelLiveData() {
        return mVeneerListLiveData;
    }

    /**
     * 定时器
     */
    private Timer moveTimer;

    /**
     * 开始移动
     */
    public void startTimer() {
        if (moveTimer != null) {
            moveTimer.cancel();
            moveTimer = null;
        }
        moveTimer = new Timer();
        /**
         * 10 秒钟访问1次
         */
        int time = 1000 * 1;
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                IDLog.d(TAG, "run: " + time);
                getPlayTime().postValue(mTime++);
            }
        }, time, time);
    }


    /**
     * 定时器暂停
     */
    public void stopTimer() {
        if (moveTimer != null) {
            moveTimer.cancel();
            moveTimer = null;
            mTime = 0;
        }
    }

    /**
     * 监听当前视频的播放时间
     *
     * @return
     */
    public MutableLiveData<Long> getPlayTime() {
        if (playTime == null) {
            playTime = new MutableLiveData<>();
        }
        return playTime;
    }

}
