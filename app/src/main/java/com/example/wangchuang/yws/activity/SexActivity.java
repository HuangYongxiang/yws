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
import com.example.wangchuang.yws.utils.CommonUtil;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SexActivity extends BaseActivity {
    private EditText username;
    private Button login;
    @Override
    public int getLayoutId() {
        return R.layout.activity_sex;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        username=(EditText)findViewById(R.id.phone);
        login=(Button) findViewById(R.id.btn_login);
    }

    public void back(View view) {
        finish();
    }
    //修改
    public void login(View view) {
        String phones = username.getText().toString().trim();
        if (TextUtils.isEmpty(phones)) {
            ToastUtil.show(this,"请输入手机号");
            return;
        }
        if (!CommonUtil.judgePhone(phones)) {
            ToastUtil.show(this,"请输入正确的手机号");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phone",phones);
        String url= Constants.BaseUrl+Constants.loginUrl;
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
                        ToastUtil.show(SexActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(SexActivity.this,response.msg);
                            startActivity(new Intent(SexActivity.this,MainActivity.class));
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SexActivity.this,response.msg);
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
