package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/15.
 */

public class PersonModel implements Serializable {
    private String uid; //
    private UserInfo user_info;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }
}
