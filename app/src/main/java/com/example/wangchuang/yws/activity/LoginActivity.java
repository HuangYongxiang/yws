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
import com.example.wangchuang.yws.utils.CommonUtils;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends BaseActivity {
    private EditText username;
    private EditText password;
    private TextView rember;
    private Button login;
    private Button regist;
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        username=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.passwords);
        rember=(TextView) findViewById(R.id.rember);
        login=(Button) findViewById(R.id.btn_login);
        regist=(Button) findViewById(R.id.btn_register);
    }
    public void back(View view) {
        finish();
    }
    //登录
    public void login(View view) {
        String phones = username.getText().toString().trim();
        String passswords = password.getText().toString().trim();
        if (TextUtils.isEmpty(phones)) {
            ToastUtil.show(this,"请输入手机号");
            return;
        }
        if (!CommonUtils.judgePhone(phones)) {
            ToastUtil.show(this,"请输入正确的手机号");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phone",phones);
        params.put("password",passswords);
        String url= Constants.BaseUrl+Constants.loginUrl;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        ToastUtil.show(LoginActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            ToastUtil.show(LoginActivity.this,response.msg);
                        }else{
                            ToastUtil.show(LoginActivity.this,response.msg);
                        }
                    }
                });
    }
    //注册
    public void register(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
    //忘记密码
    public void rembers(View view) {
        startActivity(new Intent(LoginActivity.this,RefactActivity.class));
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
