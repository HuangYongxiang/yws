package com.example.wangchuang.yws.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhaoming on 2017/10/19.
 */

public class CreditModel implements Serializable {
    private String content;//
    private ArrayList<String> oss_imgs;//

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getOss_imgs() {
        return oss_imgs;
    }

    public void setOss_imgs(ArrayList<String> oss_imgs) {
        this.oss_imgs = oss_imgs;
    }
}
