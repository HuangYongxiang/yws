package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;

/**
 * Created by Administrator on 2015/8/8.
 */
public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }
        }, 2000);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


}
