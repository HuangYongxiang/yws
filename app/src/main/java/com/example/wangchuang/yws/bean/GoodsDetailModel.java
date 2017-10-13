package com.example.wangchuang.yws.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhaoming on 2017/10/11.
 */

public class GoodsDetailModel implements Serializable {
    private String id;
    private String content;    //宝贝的文字说明
    private String creat_time;    //
    private String price;    //价格
    private String oss_imgs;    //宝贝的图片 如果不是vip，
    private String nums;    //浏览量
    private String user_name;    //宝贝的主人 昵称
    private String user_head_img;    //宝贝主人的头像
    private String vip_type;    //主人的vip状态 0不是vip1
    private String user_sex;    //宝贝主任的性别
    private String this_user_type;    //观看者（自己）的vip状态 0是不是vip 1是vip 需要做出相应的权限控制（比如为0则需要出现观看更多图片请先开通VIP）
    private String collection_status;    //0显示收藏按钮 1不显示收藏按钮 （可以自己给自己留言，不能自己收藏自己哦！）
    private String huanxin_id;    //环信的id
    private String my_user_type;    //0不是自己1是自己

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOss_imgs() {
        return oss_imgs;
    }

    public void setOss_imgs(String oss_imgs) {
        this.oss_imgs = oss_imgs;
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



    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_head_img() {
        return user_head_img;
    }

    public void setUser_head_img(String user_head_img) {
        this.user_head_img = user_head_img;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getThis_user_type() {
        return this_user_type;
    }

    public void setThis_user_type(String this_user_type) {
        this.this_user_type = this_user_type;
    }

    public String getCollection_status() {
        return collection_status;
    }

    public void setCollection_status(String collection_status) {
        this.collection_status = collection_status;
    }

    public String getHuanxin_id() {
        return huanxin_id;
    }

    public void setHuanxin_id(String huanxin_id) {
        this.huanxin_id = huanxin_id;
    }

    public String getMy_user_type() {
        return my_user_type;
    }

    public void setMy_user_type(String my_user_type) {
        this.my_user_type = my_user_type;
    }
}
