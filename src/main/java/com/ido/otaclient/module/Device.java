package com.ido.otaclient.module;

import java.io.Serializable;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-10 16:00
 * @description
 */
public class Device extends BaseEntity implements Serializable {
    /**
     * 设备id
     */
    private int id;
    /**
     * 设备的uuid
     */
    private String uuid;
    /**
     * 设备的mac地址
     */
    private String mac;
    /**
     * 设备的版本号
     */
    private String version;
    /**
     * 设备的图片
     */
    private String image;

    public Device() {

    }

    public Device(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
