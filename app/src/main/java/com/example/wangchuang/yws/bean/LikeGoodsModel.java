package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/18.
 */

public class LikeGoodsModel implements Serializable {
    private String article_id;
    private LikeGoodsContentModel article_content;
    private UserInfo user_info;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public LikeGoodsContentModel getArticle_content() {
        return article_content;
    }

    public void setArticle_content(LikeGoodsContentModel article_content) {
        this.article_content = article_content;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }
}
