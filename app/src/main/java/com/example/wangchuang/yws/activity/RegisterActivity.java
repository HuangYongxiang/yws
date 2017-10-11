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

public class RegisterActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    EditText tvVerifiCode;//验证码
    TextView tvGetCode;   //获取验证码
    private TimeCount timeCount;
    RadioButton nan,nv;
    RadioGroup radioGroup;
    Button enter;
    EditText phone;
    EditText password;
    EditText name;
    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        phone=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.passwords);
        name=(EditText)findViewById(R.id.name);
        timeCount = new TimeCount(60000, 1000);
        tvVerifiCode=(EditText)findViewById(R.id.tv_verifi_code);
        tvGetCode=(TextView) findViewById(R.id.tv_get_code);
        tvGetCode.setOnClickListener(this);
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        nan=(RadioButton) findViewById(R.id.nan);
        nv=(RadioButton) findViewById(R.id.nv);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.nan);
        enter=(Button) findViewById(R.id.btn_enter);
        enter.setOnClickListener(this);
    }
   String sex="1";
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.nan:
                sex="1";
                break;
            case R.id.nv:
                sex="2";
                break;
        }
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
                String url= Constants.BaseUrl+Constants.memberUrl1;
                OkHttpUtils.post()
                        .params(params)
                        .url(url)
                        .build()
                        .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                ToastUtil.show(RegisterActivity.this,"网络异常");
                            }

                            @Override
                            public void onResponse(BeanResult response, int id)
                            {
                                if (response.code.equals("200")) {
                                    ToastUtil.show(RegisterActivity.this,response.msg);
                                    nember=response.code;
                                }else{
                                    ToastUtil.show(RegisterActivity.this,response.msg);
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
                if (!CommonUtil.judgePhone(phoness)) {
                    ToastUtil.show(this,"请输入用户名");
                    return;
                }
                Map<String, String> paramss = new HashMap<>();
                paramss.put("phone",phoness);
                paramss.put("code",tvVerifiCode.getText().toString().trim());
                paramss.put("sex",sex);
                paramss.put("user_name",name.getText().toString().trim());
                paramss.put("password",mima);

                String urls= Constants.BaseUrl+Constants.registerUrl;
                OkHttpUtils.post()//
                        .params(paramss)//
                        .url(urls)//
                        .build()//
                        .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                ToastUtil.show(RegisterActivity.this,"网络异常");
                            }

                            @Override
                            public void onResponse(BeanResult response, int id)
                            {
                                if (response.code.equals("200")) {
                                    ToastUtil.show(RegisterActivity.this,response.msg);
                                    finish();
                                }else{
                                    ToastUtil.show(RegisterActivity.this,response.msg);
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
