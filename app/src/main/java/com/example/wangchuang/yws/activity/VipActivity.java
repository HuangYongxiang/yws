package com.example.wangchuang.yws.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.VipAdapter;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.Geren;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.PayResult;
import com.example.wangchuang.yws.bean.Price;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.content.ViewHolder;
import com.example.wangchuang.yws.utils.CommonUtil;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.CircularImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class VipActivity extends BaseActivity {
    CircularImage iv_head;
    TextView name;
    ImageView iv_vip;
    ImageView  iv_sex;
    ImageView iv_se,img;
    TextView tv_money,tv;
    LinearLayout layout,lay;
    ListView listView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void initPresenter() {
    }
    List<Price> prices=new ArrayList<Price>();
    VipAdapter vipAdapter;
    @Override
    public void initView() {
        iv_head =(CircularImage)findViewById(R.id.iv_head);
        name =(TextView) findViewById(R.id.tv_name);
        iv_vip =(ImageView) findViewById(R.id.iv_vip);
        img =(ImageView) findViewById(R.id.img);

        iv_sex =(ImageView) findViewById(R.id.iv_sex);
        iv_se =(ImageView) findViewById(R.id.iv_se);
        tv_money =(TextView) findViewById(R.id.tv_money);
        layout =(LinearLayout) findViewById(R.id.layout);
        lay =(LinearLayout) findViewById(R.id.lay);
        listView=(ListView) findViewById(R.id.list);
        login();
        prices();
    }
    protected void addview(List<Price> price) {
        lay.removeAllViews();
        for(int i=0;i<price.size();i++){
            View view = LayoutInflater.from(VipActivity.this).inflate(
                    R.layout.vip_item, null);
            TextView text1=(TextView) view.findViewById(R.id.text1);
            TextView text2=(TextView) view.findViewById(R.id.text2);
            TextView text3=(TextView) view.findViewById(R.id.text3);
            TextView buy=(TextView) view.findViewById(R.id.buy);
            final  String a=price.get(i).explains.split("月")[0];
            final String code=price.get(i).code;
            String b=price.get(i).explains.split("月")[1];
            final String c=b.split("元")[0];
            text1.setText(a+"月");
            text2.setText(c);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popwindow( a,c,code);
                }
            });
            lay.addView(view);
        }
    }
    protected void popwindow(String yue,String money,final String code){
        final PopupWindow popupWindow=new PopupWindow();

        View view = LayoutInflater.from(VipActivity.this).inflate(
                R.layout.pop_pays, null);
        TextView tv1=(TextView) view.findViewById(R.id.tv1);
        TextView tv2=(TextView) view.findViewById(R.id.tv2);
        TextView tv3=(TextView) view.findViewById(R.id.tv3);
        TextView tv4=(TextView) view.findViewById(R.id.tv4);
        TextView tv5=(TextView) view.findViewById(R.id.tv5);
        TextView tv6=(TextView) view.findViewById(R.id.tv6);
        tv1 .setText("开通"+yue+"月"+"VIP服务");
        tv2.setText("¥"+money);
        tv3.setText("<<协议>>");
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //协议
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //支付宝支付
                pay(code);
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //微信支付
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        // 设置视图
        // 设置弹出窗体的宽和高
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        popupWindow.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

    }
    public void back(View view) {
        finish();
    }
    //个人信息
    protected void login() {
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token"));
        String url= Constants.RequestUrl+Constants.mineUrl;
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
                        ToastUtil.show(VipActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson;
                                dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<Geren>(){}.getType();
                                Geren  geren = new Gson().fromJson(dataJson, type);
                                Glide.with(VipActivity.this).load(geren.oss_head_img)
                                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(iv_head);
                                Glide.with(VipActivity.this).load(geren.background_img)
                                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(img);

                                name.setText(geren.user_name);
                                if(geren.sex.equals("1")){
                                    iv_sex.setImageResource(R.drawable.icon_nan);
                                }else{
                                    iv_sex.setImageResource(R.drawable.icon_nv);
                                }
                                if(geren.people_type.equals("1")){
                                    iv_se.setImageResource(R.drawable.icon_smrzf);
                                }else{
                                    iv_se.setImageResource(R.drawable.icon_smrz);
                                }
                               /* if(geren.vip_type.equals("1")){
                                    iv_vip.setImageResource(R.drawable.icon_vip1);
                                    tv_money.setText(geren.vip_time);
                                }else{
                                    iv_vip.setImageResource(R.drawable.icon_vip2);
                                }*/
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                        }
                    }
                });
    }
    protected void prices() {
        Map<String, String> params = new HashMap<>();
        String url= Constants.RequestUrl+Constants.VipUrl;
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(VipActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson;
                                dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Price>>(){}.getType();
                                prices= new Gson().fromJson(dataJson, type);
                                addview(prices);
                               /* vipAdapter=new VipAdapter(VipActivity.this,prices);
                                listView.setAdapter(vipAdapter);
                                vipAdapter.notifyDataSetChanged();*/
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                        }
                    }
                });
    }
    //支付
    protected void pay(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token"));
        params.put("payment_type", "1");
        params.put("code", code);
        String url= Constants.zfbUrl;
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
                        ToastUtil.show(VipActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                final String dataJson;
                                dataJson = jsonObject.optString("data");
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        PayTask alipay = new PayTask(VipActivity.this);
                                        Map<String, String> result = alipay.payV2(dataJson, true);
                                        Log.i("msp", result.toString());
                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(VipActivity.this,response.msg);
                        }
                    }
                });
    }
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        login();
                        Toast.makeText(VipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(VipActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
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
