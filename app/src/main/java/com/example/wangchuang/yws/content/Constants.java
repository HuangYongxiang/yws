package com.example.wangchuang.yws.content;

import android.content.Context;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.utils.StringUtil;

/**
 * Created by Administrator on 2017/10/11.
 */

public class Constants {

    public static final int IMAGE_PICKER = 1;


    public static final String BaseUrl="http://47.95.118.150/index.php/App/";
    public static final String RequestUrl="http://wc306.com/index.php/App/";


    public static final String loginUrl="Members/member_login";//登录
    public static final String registerUrl="Members/members_register";//注册
    public static final String memberUrl1="Members/send_sms";//验证码
    public static final String memberUrl2="Members/back_sms";//找回密码验证码
    public static final String refactUrl="Members/back_password";//修改密码
    public static final String publishUrl="User/user_release";//发布

    public static final String mainListUrl="Index/index";//首页
    public static final String mainDetailUrl="UserDetails/article_details";//首页
    public static final String DetailCommentUrl="UserDetails/article_commonet_list";//评论列表
    public static final String PushCommentUrl="UserDetails/user_commonet";//发表评论
    public static final String collectionUrl="UserDetails/collection";//添加收藏
    public static final String cancelCollectionUrl="UserDetails/delect_collection";//取消收藏
    public static final String userFollowUrl="Others/user_follow";//我的关注
    public static final String userFansUrl="Others/user_fans";//我的粉丝
    public static final String otherPublishUrl="Others/user_index_release";//他人发布得
    public static final String otherPeopleUrl="Others/index";//他人资料
    public static final String cancelLikeUrl="Others/delect_follow";//取消关注
    public static final String addLikeUrl="Others/follow";//关注
    public static final String myPublishUrl="User/user_index_release";//我发布的
    public static final String myCollectionUrl="User/user_collection";//我收藏的
    public static final String mineUrl="User/user_info";//我的




    public static final String yijianUrl="User/feedback";//意见反馈
    public static final String mineUrl="User/user_info"; //个人信息
    public static final String TequanUrl="Index/release_vip_price";//特权价格表
    public static final String VipUrl="Index/vip_price";//vip价格表
    public static final String SfUrl="User/people_testing";//实名认证
    public static final String SexUrl="User/sex_testing";//性别认证
    public static final String nameUrl="User/save_name";//修改昵称
    public static final String zfbUrl="http://wc306.com/App/Pay/user_alipay";//修改昵称





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
