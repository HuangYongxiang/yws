package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class XiugaiActivity extends BaseActivity {
    private EditText username;
    private Button login;
    @Override
    public int getLayoutId() {
        return R.layout.activity_xiugai;
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
            ToastUtil.show(this,"请输入昵称");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token"));
        params.put("user_name",phones);
        String url= Constants.BaseUrl+Constants.nameUrl;
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
                        ToastUtil.show(XiugaiActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(XiugaiActivity.this,response.msg);
                           /* EventCenter eventCenter = new EventCenter(Constants.EB_CODE_REFRESH_MINE);
                            EventBus.getDefault().post(eventCenter);*/
                            finish();
                         }else{
                            dismissLoadingDialog();
                            ToastUtil.show(XiugaiActivity.this,response.msg);
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
