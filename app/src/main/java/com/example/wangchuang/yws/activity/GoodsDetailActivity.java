package com.example.wangchuang.yws.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.CommentListAdapter;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.CommentAllModel;
import com.example.wangchuang.yws.bean.GoodsDetailModel;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.UserInfo;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.NoScrollListView;
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

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mBackIv,mOptionIv,mSexIv,mVipIv,mImg1,mImg2,mImg3;
    private TextView mNameTv,mPriceTv,mNumTv,mBuyTv,mContentTv,mTimeTv;
    private RelativeLayout commentRl,likeRl;
    private CircularImage headerImg;
    private GoodsDetailModel data;
    private NoScrollListView commentList;
    private String id;
    private ArrayList<CommentAllModel> commentData = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getString("id");
        }
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mOptionIv = (ImageView) findViewById(R.id.iv_option);
        mSexIv = (ImageView) findViewById(R.id.iv_sex);
        mVipIv = (ImageView) findViewById(R.id.iv_vip);
        mImg1 = (ImageView) findViewById(R.id.imageView1);
        mImg2 = (ImageView) findViewById(R.id.imageView2);
        mImg3 = (ImageView) findViewById(R.id.imageView3);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mPriceTv = (TextView) findViewById(R.id.tv_money);
        mNumTv = (TextView) findViewById(R.id.tv_num);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mBuyTv = (TextView) findViewById(R.id.go_buy_tv);
        mContentTv = (TextView) findViewById(R.id.tv_describe);
        commentRl = (RelativeLayout) findViewById(R.id.comment_rl);
        headerImg = (CircularImage) findViewById(R.id.iv_header);
        likeRl = (RelativeLayout) findViewById(R.id.like_rl);
        commentList = (NoScrollListView) findViewById(R.id.comment_list);
        likeRl.setOnClickListener(this);
        getData();
        getCommentData();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.like_rl:
                sendLike();
                break;
        }
    }

    private void sendLike() {
        String url = Constants.RequestUrl + Constants.collectionUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","00d51e2300352fa36131780f24bbc5e3e4265a43");
        params.put("id",21+"");
        //showLoadingDialog("请求中....");
        OkHttpUtils.post()//
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //dismissLoadingDialog();
                        ToastUtil.show(GoodsDetailActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<CommentAllModel>>(){}.getType();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(GoodsDetailActivity.this, response.msg);
                        }
                    }
                });
    }
    private void cancelCollection() {
        String url = Constants.RequestUrl + Constants.cancelCollectionUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","00d51e2300352fa36131780f24bbc5e3e4265a43");
        params.put("id",21+"");
        //showLoadingDialog("请求中....");
        OkHttpUtils.post()//
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //dismissLoadingDialog();
                        ToastUtil.show(GoodsDetailActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<CommentAllModel>>(){}.getType();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(GoodsDetailActivity.this, response.msg);
                        }
                    }
                });
    }

    /**
     * 获取评论数据
     */
    private void getCommentData() {
        String url = Constants.RequestUrl + Constants.DetailCommentUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","00d51e2300352fa36131780f24bbc5e3e4265a43");
        params.put("id",21+"");
        //showLoadingDialog("请求中....");
        OkHttpUtils.post()//
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //dismissLoadingDialog();
                        ToastUtil.show(GoodsDetailActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<CommentAllModel>>(){}.getType();
                                commentData = new Gson().fromJson(dataJson, type);
                                CommentListAdapter adapter = new CommentListAdapter(commentData,GoodsDetailActivity.this);
                                commentList.setAdapter(adapter);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(GoodsDetailActivity.this, response.msg);
                        }
                    }
                });
    }

    private void getData() {
        String url = Constants.RequestUrl + Constants.mainDetailUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","00d51e2300352fa36131780f24bbc5e3e4265a43");
        params.put("id",21+"");
        //showLoadingDialog("请求中....");
        OkHttpUtils.post()//
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //dismissLoadingDialog();
                        ToastUtil.show(GoodsDetailActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            try {
                                data = new GoodsDetailModel();
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                JSONObject obj = jsonObject.getJSONObject("data");
                                data.setContent(obj.getString("content"));
                                data.setId(obj.getString("id"));
                                data.setCreat_time(obj.getString("creat_time"));
                                data.setPrice(obj.getString("price"));
                                data.setOss_imgs(obj.getString("oss_imgs"));
                                data.setNums(obj.getString("nums"));
                                data.setCollection_status(obj.getString("collection_status"));
                                data.setUser_name(obj.getString("user_name"));
                                data.setUser_head_img(obj.getString("user_head_img"));
                                data.setVip_type(obj.getString("vip_type"));
                                data.setUser_sex(obj.getString("user_sex"));
                                data.setHuanxin_id(obj.getString("huanxin_id"));
                                data.setMy_user_type(obj.getString("my_user_type"));
                                data.setThis_user_type(obj.getString("this_user_type"));
                               /* String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<GoodsDetailModel>>(){}.getType();
                                data = new Gson().fromJson(dataJson, type);*/
                                initData();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(GoodsDetailActivity.this, response.msg);
                        }
                    }
                });
    }
    private void initData() {
        if(data.getMy_user_type().equals("0")){
            mOptionIv.setVisibility(View.GONE);
            likeRl.setVisibility(View.VISIBLE);
        }else {
            mOptionIv.setVisibility(View.VISIBLE);
            likeRl.setVisibility(View.GONE);
        }
        Glide.with(GoodsDetailActivity.this).load(data.getUser_head_img())
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(headerImg);
        mNameTv.setText(data.getUser_name());
        mPriceTv.setText(data.getPrice());
        mContentTv.setText(data.getContent());
        mNumTv.setText("浏览量 " + data.getNums());
        mTimeTv.setText(data.getCreat_time());
        if(data.getUser_sex().equals("1")){
            mSexIv.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_nan));
        }else {
            mSexIv.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_nv));
        }
        if(data.getVip_type().equals("1")){
            mVipIv.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            mVipIv.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_vip2));
        }
    }
    public void sendCommend(UserInfo userInfo, String msg) {

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
