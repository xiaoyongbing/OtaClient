package com.ido.otaclient.module;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-17 21:18
 * @description
 */
public class UploadConfig {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 升级类型
     */
    private String uploadName;
    /**
     * 固件名称
     */
    private String firewareNormalName;
    /**
     * 普通固件版本
     */
    private String firewareNormalVersion;
    /**
     * 特殊固件名称
     */
    private String firewareSpecialName;
    /**
     * 特殊固件版本
     */
    private String firewareSpecialVersion;
    /**
     * 最新固件名称
     */
    private String firewareNewName;
    /**
     * 最新固件版本
     */
    private String firewareNewVersion;
    /**
     * boot 文件名称
     */
    private String bootloaderName;
    /**
     * boot 文件版本
     */
    private String bootloaderVersion;
    /**
     * 字库文件名称
     */
    private String fontName;
    /**
     * 字库文件版本
     */
    private String fontVersion;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getFirewareNormalName() {
        return firewareNormalName;
    }

    public void setFirewareNormalName(String firewareNormalName) {
        this.firewareNormalName = firewareNormalName;
    }

    public String getFirewareNormalVersion() {
        return firewareNormalVersion;
    }

    public void setFirewareNormalVersion(String firewareNormalVersion) {
        this.firewareNormalVersion = firewareNormalVersion;
    }

    public String getFirewareSpecialName() {
        return firewareSpecialName;
    }

    public void setFirewareSpecialName(String firewareSpecialName) {
        this.firewareSpecialName = firewareSpecialName;
    }

    public String getFirewareSpecialVersion() {
        return firewareSpecialVersion;
    }

    public void setFirewareSpecialVersion(String firewareSpecialVersion) {
        this.firewareSpecialVersion = firewareSpecialVersion;
    }

    public String getFirewareNewName() {
        return firewareNewName;
    }

    public void setFirewareNewName(String firewareNewName) {
        this.firewareNewName = firewareNewName;
    }

    public String getFirewareNewVersion() {
        return firewareNewVersion;
    }

    public void setFirewareNewVersion(String firewareNewVersion) {
        this.firewareNewVersion = firewareNewVersion;
    }

    public String getBootloaderName() {
        return bootloaderName;
    }

    public void setBootloaderName(String bootloaderName) {
        this.bootloaderName = bootloaderName;
    }

    public String getBootloaderVersion() {
        return bootloaderVersion;
    }

    public void setBootloaderVersion(String bootloaderVersion) {
        this.bootloaderVersion = bootloaderVersion;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontVersion() {
        return fontVersion;
    }

    public void setFontVersion(String fontVersion) {
        this.fontVersion = fontVersion;
    }
}
