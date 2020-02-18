package com.ido.otaclient.base;

import android.os.Environment;

import java.io.File;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-14 09:38
 * @description
 */
public class Constant {
    /**
     * 监听的端口号
     */
    public static final int HTTP_PORT = 5000;

    /**
     * 保存的文件夹
     */
    public static final String DIR_IN_SDCARD = "otaServer";

    /**
     * 保存的文件夹
     */
    public static final String DIR_OUT_SDCRA = Environment.getExternalStorageDirectory() + File.separator + "otaClient";

    /**
     * 文件的文件夹
     */
    public static final String DIR_IN_SDCARD_UPLOAD =
            DIR_OUT_SDCRA  + File.separator + "upload";
    /**
     * 压缩的文件
     */
    public static final String DIR_IN_SDCARD_UPLOAD_zip =
            DIR_OUT_SDCRA  + File.separator + "upload.zip";

    public static final File DIR = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.DIR_IN_SDCARD);


}
