package com.example.wangchuang.yws.bean;

import java.io.Serializable;

/**
 * Created by zhaoming on 2017/10/11.
 */

public class GoodsModel implements Serializable {
    private String id;    //文章id
    private String title;    //
    private String content;    //
    private String creat_time;    //
    private String price;    //
    private String[] oss_imgs;    //文章图片
    private String user_head_img;    //作者头像
    private String sex;    //作者性别
    private String user_name;    //作者昵称
    private String vip_type;    //1是VIP0不是vip
    private String nums;    //浏览数量
    private String comment_num;    //留言数量

    public String[] getOss_imgs() {
        return oss_imgs;
    }

    public void setOss_imgs(String[] oss_imgs) {
        this.oss_imgs = oss_imgs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(String creat_time) {
        this.creat_time = creat_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getUser_head_img() {
        return user_head_img;
    }

    public void setUser_head_img(String user_head_img) {
        this.user_head_img = user_head_img;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }
}
