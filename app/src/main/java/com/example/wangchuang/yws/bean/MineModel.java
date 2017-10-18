package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/17.
 */

public class MineModel implements Serializable {
    private String user_name;//
    private String sex;//
    private String sex_type;//
    private String people_type;//0是没有认证 1是审核中 2是已经认证
    private String vip_type;//0不是vip1是vip
    private String oss_head_img;//
    private String background_img;//背景墙地址
    private String release_type;//1是特权vip0不是特权vip
    private String vip_time;//vip剩余时间
    private String release_time;//特权剩余时间

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

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public String getRelease_type() {
        return release_type;
    }

    public void setRelease_type(String release_type) {
        this.release_type = release_type;
    }

    public String getVip_time() {
        return vip_time;
    }

    public void setVip_time(String vip_time) {
        this.vip_time = vip_time;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }
}
