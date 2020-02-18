package com.ido.otaclient.module;

import java.io.Serializable;

/**
 * Copyright (c) 2019 深圳市爱都科技有限公司. All rights reserved.
 *
 * @Author: xyb
 * @CreateDate: 2020-02-14 10:04
 * @description 单板的实体类
 */
public class Veneer extends BaseEntity implements Serializable {
    /**
     * 单板图片
     */
    private String image;
    /**
     * 单板ip
     */
    private String ip;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
