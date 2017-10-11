package com.example.wangchuang.yws.content;

import android.content.Context;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.utils.StringUtil;

/**
 * Created by Administrator on 2017/10/11.
 */

public class Constants {
    public static final String BaseUrl="47.95.118.150/index.php/App/Members/";
    public static final String RequestUrl="http://wc306.com/index.php/App/Index/";


    public static final String loginUrl="member_login";//登录
    public static final String registerUrl="members_register";//注册
    public static final String mainListUrl="index";//首页










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
