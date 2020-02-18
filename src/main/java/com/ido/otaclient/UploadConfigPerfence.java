package com.ido.otaclient;


import android.content.Context;

import com.ido.otaclient.base.BasePreference;
import com.ido.otaclient.env.AppEnv;
import com.ido.otaclient.module.UploadConfig;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-17 20:18
 * @description
 */
public class UploadConfigPerfence extends BasePreference {
    private static final String NAME = "upload_preference";

    private static final String TASK_NAME = "taskName";
    private static final String UPLOAD_TYPE = "uploadName";
    private static final String FIREWARE_NORMAL_NAME = "firewareNormalName";
    private static final String FIREWARE_NORMAL_VERSION = "firewareNormalVersion";
    private static final String FIREWARE_SPECIAL_NAME = "firewareSpecialName";
    private static final String FIREWARE_SPECIAL_VERSION = "firewareSpecialVersion";
    private static final String FIREWARE_NEW_NAME = "firewareNewName";
    private static final String FIREWARE_NEW_VERSION = "firewareNewVersion";
    private static final String BOOTLOADER_NAME = "bootloaderName";
    private static final String BOOTLOADER_VERSION = "bootloaderVersion";
    private static final String FONT_NAME ="fontName";
    private static final String FONT_NAME_VERSION ="fontName";
    /**
     * 升级配置
     */
    private UploadConfig mUploadConfig;

    private static volatile UploadConfigPerfence instance;

    private static Context getContext() {
        return AppEnv.instance().getContext();
    }

    public static UploadConfigPerfence getInstance() {
        if (instance == null) {
            synchronized (UploadConfigPerfence.class) {
                if (instance == null) {
                    instance = new UploadConfigPerfence();
                }
            }
        }
        return instance;
    }

    /**
     * 保存配置在本地
     * @param uploadConfig
     */
    public void saveUploadConfig(UploadConfig uploadConfig){
        if(uploadConfig == null){
            uploadConfig = new UploadConfig();
        }
        saveString(getContext(),NAME,TASK_NAME,uploadConfig.getTaskName());
        saveString(getContext(),NAME,UPLOAD_TYPE,uploadConfig.getUploadName());
        saveString(getContext(),NAME,FIREWARE_NORMAL_NAME,uploadConfig.getFirewareNormalName());
        saveString(getContext(),NAME,FIREWARE_NORMAL_VERSION,uploadConfig.getFirewareNormalVersion());
        saveString(getContext(),NAME,FIREWARE_SPECIAL_NAME,uploadConfig.getFirewareSpecialName());
        saveString(getContext(),NAME,FIREWARE_SPECIAL_VERSION,uploadConfig.getFirewareSpecialVersion());
        saveString(getContext(),NAME,FIREWARE_NEW_NAME,uploadConfig.getFirewareNewName());
        saveString(getContext(),NAME,FIREWARE_NEW_VERSION,uploadConfig.getFirewareNewVersion());
        saveString(getContext(),NAME,BOOTLOADER_NAME,uploadConfig.getBootloaderName());
        saveString(getContext(),NAME,BOOTLOADER_VERSION,uploadConfig.getBootloaderVersion());
        saveString(getContext(),NAME,FONT_NAME,uploadConfig.getFontName());
        saveString(getContext(),NAME,FONT_NAME_VERSION,uploadConfig.getFontVersion());
    }

    /**
     * 从本地取出数据
     * @return
     */
    public UploadConfig uploadConfig(){
        return mUploadConfig;
    }
}
