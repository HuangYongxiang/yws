package com.example.wangchuang.yws.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.utils.CommonUtil;
import com.example.wangchuang.yws.utils.CommonUtils;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class RefactActivity extends BaseActivity implements View.OnClickListener{
    EditText tvVerifiCode;//验证码
    TextView tvGetCode;   //获取验证码
    private TimeCount timeCount;
    Button enter;
    EditText phone;
    EditText password;
    @Override
    public int getLayoutId() {
        return R.layout.activity_refact;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        phone=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.passwords);
        timeCount = new TimeCount(60000, 1000);
        tvVerifiCode=(EditText)findViewById(R.id.tv_verifi_code);
        tvGetCode=(TextView) findViewById(R.id.tv_get_code);
        tvGetCode.setOnClickListener(this);
        enter=(Button) findViewById(R.id.btn_enter);
        enter.setOnClickListener(this);
    }



    String nember="";
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:
                String phones = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.show(this,"请输入手机号");
                    return;
                }
                if (!CommonUtil.judgePhone(phones)) {
                    ToastUtil.show(this,"请输入正确的手机号");
                    return;
                }
                //将光标移到验证码栏
                tvVerifiCode.requestFocus();
                timeCount.start();
                Map<String, String> params = new HashMap<>();
                params.put("phone",phones);
                String url= Constants.BaseUrl+Constants.memberUrl2;
                OkHttpUtils.post()
                        .params(params)
                        .url(url)
                        .build()
                        .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                ToastUtil.show(RefactActivity.this,"网络异常");
                            }

                            @Override
                            public void onResponse(BeanResult response, int id)
                            {
                                if (response.code.equals("200")) {
                                    ToastUtil.show(RefactActivity.this,response.msg);
                                }else{
                                    ToastUtil.show(RefactActivity.this,response.msg);
                                }
                            }
                        });
                break;
            case R.id.btn_enter:
                String mima=password.getText().toString().trim();
                String phoness = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoness)) {
                    ToastUtil.show(this,"请输入手机号");
                    return;
                }
                if (!CommonUtil.judgePhone(phoness)) {
                    ToastUtil.show(this,"请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(mima)) {
                    ToastUtil.show(this,"密码不能为空");
                    return;
                }
                Map<String, String> paramss = new HashMap<>();
                paramss.put("phone",phoness);
                paramss.put("code",tvVerifiCode.getText().toString().trim());
                paramss.put("password",mima);
                String urls= Constants.BaseUrl+Constants.refactUrl;
                OkHttpUtils.post()//
                        .params(paramss)//
                        .url(urls)//
                        .build()//
                        .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                ToastUtil.show(RefactActivity.this,"网络异常");
                            }

                            @Override
                            public void onResponse(BeanResult response, int id)
                            {
                                if (response.code.equals("200")) {
                                    ToastUtil.show(RefactActivity.this,response.msg);
                                    finish();
                                }else{
                                    ToastUtil.show(RefactActivity.this,response.msg);
                                }
                            }
                        });
                break;
        }
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
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            tvGetCode.setText("获取验证码");
            tvGetCode.setClickable(true);
        }

        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText(millisUntilFinished / 1000  +"s"+"后重新获取");
        }
    }
}
