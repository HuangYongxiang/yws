package com.example.wangchuang.yws.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;

public class SexActivity2 extends BaseActivity {
   @Override
    public int getLayoutId() {
        return R.layout.activity_sex2;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        }

    public void back(View view) {
        finish();
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
