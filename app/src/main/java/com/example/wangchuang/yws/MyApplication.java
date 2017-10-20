package com.example.wangchuang.yws;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.wangchuang.yws.huanxin.DemoDBManager;
import com.example.wangchuang.yws.huanxin.DemoHelper;
import com.example.wangchuang.yws.utils.GlideImageLoaderImagePicker;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

/**
 * APPLICATION
 */
public class MyApplication extends Application {
    public static String currentUserNick = "";
    private static MyApplication baseApplication;
    private int maxImgCount = 9;
    public static Context applicationContext;
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;
        instance = this;

        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
        EMOptions options=new EMOptions();
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);
        EMClient.getInstance().init(this,options);
        baseApplication = this;
        initImagePicker();
    }




    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoaderImagePicker());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(1000);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1250);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
