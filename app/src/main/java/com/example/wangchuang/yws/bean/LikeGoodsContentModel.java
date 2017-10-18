package com.example.wangchuang.yws.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhaoming on 2017/10/18.
 */

public class LikeGoodsContentModel {
    private String title; //
    private String content; //
    private String creat_time; //
    private String price; //
    private ArrayList<String> oss_imgs; //文章前三张图片
    private String nums; //文章浏览量
    private String comment_nums; //文章评论数量

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

    public ArrayList<String> getOss_imgs() {
        return oss_imgs;
    }

    public void setOss_imgs(ArrayList<String> oss_imgs) {
        this.oss_imgs = oss_imgs;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getComment_nums() {
        return comment_nums;
    }

    public void setComment_nums(String comment_nums) {
        this.comment_nums = comment_nums;
    }
}
