package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/15.
 */

public class PersonModel implements Serializable {
    private String user_name; //
    private String sex; //
    private String type; //
    private String oss_head_img; //

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOss_head_img() {
        return oss_head_img;
    }

    public void setOss_head_img(String oss_head_img) {
        this.oss_head_img = oss_head_img;
    }
}
