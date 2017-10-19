package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.utils.CommonUtil;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class FeedbackActivity extends BaseActivity {
    private EditText username,yijian,phone;
    private Button login;
    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        username=(EditText)findViewById(R.id.name);
        yijian=(EditText)findViewById(R.id.yijian);
        phone=(EditText)findViewById(R.id.phone);

        login=(Button) findViewById(R.id.btn_login);
    }

    public void back(View view) {
        finish();
    }
    //tijiao
    public void login(View view) {
        if (TextUtils.isEmpty(username.getText().toString().trim())) {
            ToastUtil.show(this,"请输入问题描述");
            return;
        }
        if (TextUtils.isEmpty(yijian.getText().toString().trim())) {
            ToastUtil.show(this,"请输意见反馈");
            return;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            ToastUtil.show(this,"");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phone",phone.getText().toString().trim());
        params.put("problem",username.getText().toString().trim());
        params.put("proposal",yijian.getText().toString().trim());
        params.put("token", ValueStorage.getString("token"));

        String url= Constants.BaseUrl+Constants.yijianUrl;
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(FeedbackActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(FeedbackActivity.this,response.msg);
                            finish();
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(FeedbackActivity.this,response.msg);
                        }
                    }
                });
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
