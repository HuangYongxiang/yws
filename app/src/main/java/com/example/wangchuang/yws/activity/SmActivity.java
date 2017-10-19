package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SmActivity extends BaseActivity {
    private ImageView img_zeng,img_fan;
    private TextView zeng,fan;
    String url1,url2;
    @Override
    public int getLayoutId() {
        return R.layout.activity_sr;
    }

    @Override
    public void initPresenter() {
    }
    String biaoshi="0";
    @Override
    public void initView() {
        img_zeng=(ImageView) findViewById(R.id.img_zeng);
        img_fan=(ImageView) findViewById(R.id.img_fan);
        zeng=(TextView) findViewById(R.id.zeng);
        fan=(TextView) findViewById(R.id.fan);
        img_zeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectGallery(1);
                biaoshi="1";
            }
        });
        img_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectGallery(1);
                biaoshi="2";
            }
        });


    }

    private void goSelectGallery(int num) {
        ImagePicker.getInstance().setSelectLimit(num);
        if (num == 1) {
            ImagePicker.getInstance().setMultiMode(false);
        } else {
            ImagePicker.getInstance().setMultiMode(true);
        }
        Intent intent = new Intent(SmActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, Constants.IMAGE_PICKER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == Constants.IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(biaoshi.equals("1")){
                    url1 = images.get(0).path;
                    Glide.with(SmActivity.this).load(url1).centerCrop().crossFade().error(R.drawable.icon_tjtp).skipMemoryCache(false).into(img_zeng);
                     if(url1==null){
                         zeng.setVisibility(View.VISIBLE);
                     }else{
                         zeng.setVisibility(View.GONE);

                     }
                }
                if(biaoshi.equals("2")){
                    url2 = images.get(0).path;
                    Glide.with(SmActivity.this).load(url1).centerCrop().crossFade().error(R.drawable.icon_tjtp).skipMemoryCache(false).into(img_fan);
                }
            } else {

            }
        }
    }

    public void back(View view) {
        finish();
    }
    //修改
    public void login(View view) {
        File file1=new File(url1);
        File file2=new File(url2);

        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token"));
        String url= Constants.BaseUrl+Constants.SfUrl;
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
                .params(params)
                .addFile("just","zeng.jpg",file1)
                .addFile("back","fan.jpg",file2 )
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(SmActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(SmActivity.this,response.msg);
                            startActivity(new Intent(SmActivity.this,MainActivity.class));
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SmActivity.this,response.msg);
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
