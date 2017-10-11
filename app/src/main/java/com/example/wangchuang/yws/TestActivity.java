package com.example.wangchuang.yws;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;
import okhttp3.Call;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> params = new HashMap<>();
        params.put("uname",getIntent().getStringExtra("operationUserId"));
        params.put("pid","groupId");
        //showLoadingDialog("请求中....");
        OkHttpUtils.post()//
                .params(params)//
                .url("http://47.93.189.208:8083/message/group/updateRemark")//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //dismissLoadingDialog();
                        ToastUtil.show(TestActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.status.equals("200")) {
                            //dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson;
                                dataJson = jsonObject.optString("data");

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(TestActivity.this, response.msg);
                        }
                    }
                });
    }
}
