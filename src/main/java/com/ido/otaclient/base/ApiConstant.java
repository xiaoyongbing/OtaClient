package com.ido.otaclient.base;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-14 09:44
 * @description
 */
public class ApiConstant {

    public static final String URL_HEAD = "http://";
    /**
     * 测试方法
     */
    public static final String METHOD_TEST = "test";
    /**
     * 获取单板列表
     */
    public static final String METHOD_VENEER_LIST = "veneer_list";
    /**
     * 启动任务
     */
    public static final String METHOD_TASK_START = "task_start";
    /**
     * 结束任务
     */
    public static final String METHOD_TASK_STOP = "task_stop";
    /**
     * 查询状态
     */
    public static final String METHOD_CHECK = "device_check";
    /**
     * 确认固件上传
     */
    public static final String METHOD_CONFIRM_FIREWARE = "confirm_fireware";
    /**
     * 固件下发
     */
    public static final String METHOD_FIRMWARE_DOWNLOAD = "firmware_download";
    /**
     * 升级类型Application
     */
    public static final String UPDATE_TYPE_APPLICATION = "application";
    /**
     * 升级类型为Bootloader
     */
    public static final String UPDATE_TYPE_BOOTLOADER = "Bootloader";

}
