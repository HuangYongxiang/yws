package com.example.wangchuang.yws.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 */

public class UserInfo implements Serializable, Parcelable {
    public String user_name;
    private String sex;
    private String type;
    private String oss_head_img;
    private String uid;
    private String people_type;

    public String getPeople_type() {
        return people_type;
    }

    public void setPeople_type(String people_type) {
        this.people_type = people_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_name);
        dest.writeString(this.sex);
        dest.writeString(this.type);
        dest.writeString(this.oss_head_img);
    }

    protected UserInfo(Parcel in) {
        this.user_name = in.readString();
        this.sex = in.readString();
        this.type = in.readString();
        this.oss_head_img = in.readString();
    }
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
