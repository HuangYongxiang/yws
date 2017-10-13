package com.example.wangchuang.yws.content;

import android.content.Context;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.utils.StringUtil;

/**
 * Created by Administrator on 2017/10/11.
 */

public class Constants {

    public static final int IMAGE_PICKER = 1;


    public static final String BaseUrl="http://47.95.118.150/index.php/App/Members/";
    public static final String RequestUrl="http://wc306.com/index.php/App/";


    public static final String loginUrl="member_login";//登录
    public static final String registerUrl="members_register";//注册
    public static final String memberUrl1="send_sms";//验证码
    public static final String memberUrl2="back_sms";//找回密码验证码
    public static final String refactUrl="back_password";//修改密码

    public static final String mainListUrl="Index/index";//首页
    public static final String mainDetailUrl="UserDetails/article_details";//首页
    public static final String DetailCommentUrl="UserDetails/article_commonet_list";//评论列表
    public static final String collectionUrl="UserDetails/collection";//添加收藏
    public static final String cancelCollectionUrl="UserDetails/delect_collection";//取消收藏










    public static String subException(Context context, Exception e){
        if (e!=null&&e instanceof RuntimeException||e instanceof IllegalStateException) {
            if(StringUtil.isNotEmpty(e.toString())){
                int index = e.toString().lastIndexOf(':');
                return e.toString().substring(index+1,e.toString().length());
            }
        } else {
            return context.getString(R.string.net_error);
        }
        return e.toString();
    }

}
