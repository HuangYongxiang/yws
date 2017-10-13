package com.example.wangchuang.yws.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 */

public class CommentAllModel implements Serializable {
    private String id; //
    private String content; //
    private UserInfo user_info; //
    private String interval_time; //
    private ArrayList<CommentModel> two_comment;

    public ArrayList<CommentModel> getTwo_comment() {
        return two_comment;
    }

    public void setTwo_comment(ArrayList<CommentModel> two_comment) {
        this.two_comment = two_comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

    public String getInterval_time() {
        return interval_time;
    }

    public void setInterval_time(String interval_time) {
        this.interval_time = interval_time;
    }
}
