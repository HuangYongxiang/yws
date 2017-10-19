package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/18.
 */

public class OtherPeopleModel implements Serializable {
    private String user_name;//
    private String sex;//
    private String sex_type;//
    private String people_type;//
    private String vip_type;//
    private String oss_head_img;//
    private String oss_background_img;//
    private String uid;//
    private String follow_status;//

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

    public String getSex_type() {
        return sex_type;
    }

    public void setSex_type(String sex_type) {
        this.sex_type = sex_type;
    }

    public String getPeople_type() {
        return people_type;
    }

    public void setPeople_type(String people_type) {
        this.people_type = people_type;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getOss_head_img() {
        return oss_head_img;
    }

    public void setOss_head_img(String oss_head_img) {
        this.oss_head_img = oss_head_img;
    }

    public String getOss_background_img() {
        return oss_background_img;
    }

    public void setOss_background_img(String oss_background_img) {
        this.oss_background_img = oss_background_img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }
}
