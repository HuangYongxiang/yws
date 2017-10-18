package com.example.wangchuang.yws.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.CommentListAdapter;
import com.example.wangchuang.yws.adapter.ImageGridAdapter;
import com.example.wangchuang.yws.adapter.TwoCommentListAdapter;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.CommentAllModel;
import com.example.wangchuang.yws.bean.GoodsDetailModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.fragment.CommentFragment;
import com.example.wangchuang.yws.utils.AnimationUtil;
import com.example.wangchuang.yws.utils.StringUtil;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.BounceScrollView;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.NoScrollGridView;
import com.example.wangchuang.yws.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener,CommentListAdapter.OnCommentClickListener,TwoCommentListAdapter.TwoOnCommentClickListener{
    private ImageView mBackIv,mOptionIv,mSexIv,mVipIv,mImg1,mImg2,mImg3,mLikeIv;
    private TextView mNameTv,mPriceTv,mNumTv,mBuyTv,mContentTv,mTimeTv,mLikeTv,mRefreshTv,mDeleteTv,mCancelTv,mCommentNumTv;
    private BounceScrollView scrollview;
    private RelativeLayout commentRl,likeRl;
    private RelativeLayout emptyLayout;
    private CircularImage headerImg;
    private GoodsDetailModel data;
    private NoScrollListView commentList;
    private NoScrollGridView gv_gridview;
    private LinearLayout optionLayout;
    private String id;
    private ArrayList<CommentAllModel> commentData = new ArrayList<>();
    private static final String COMMENT_FRAGMENT = "CommentFragment";
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
        mLikeIv = (ImageView) findViewById(R.id.iv_like);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mPriceTv = (TextView) findViewById(R.id.tv_money);
        mNumTv = (TextView) findViewById(R.id.tv_num);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mBuyTv = (TextView) findViewById(R.id.go_buy_tv);
        mLikeTv = (TextView) findViewById(R.id.tv_like);
        mContentTv = (TextView) findViewById(R.id.tv_describe);
        mRefreshTv = (TextView) findViewById(R.id.tv_refresh);
        mDeleteTv = (TextView) findViewById(R.id.tv_delete);
        mCancelTv = (TextView) findViewById(R.id.tv_cancel);
        mCommentNumTv = (TextView) findViewById(R.id.tv_comment_num);
        commentRl = (RelativeLayout) findViewById(R.id.comment_rl);
        headerImg = (CircularImage) findViewById(R.id.iv_header);
        likeRl = (RelativeLayout) findViewById(R.id.like_rl);
        emptyLayout = (RelativeLayout) findViewById(R.id.empty_layout);
        commentList = (NoScrollListView) findViewById(R.id.comment_list);
        scrollview = (BounceScrollView) findViewById(R.id.scrollview);
        gv_gridview = (NoScrollGridView) findViewById(R.id.gv_gridview);
        optionLayout = (LinearLayout) findViewById(R.id.bottom_option);
        likeRl.setOnClickListener(this);
        commentRl.setOnClickListener(this);
        mOptionIv.setOnClickListener(this);
        mRefreshTv.setOnClickListener(this);
        mDeleteTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);

        getData();
        getCommentData();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.like_rl:
                sendLike();
                break;
            case R.id.comment_rl:
                showCommentFrag(id,"");
                break;
            case R.id.iv_option:
                optionLayout.setVisibility(View.VISIBLE);
                optionLayout.setAnimation(AnimationUtil.moveToViewLocation());
                break;
            case R.id.tv_refresh:
                getData();
                getCommentData();
                break;
            case R.id.tv_delete:
               // showCommentFrag(id,"");
                break;
            case R.id.tv_cancel:
                optionLayout.setVisibility(View.GONE);
                optionLayout.setAnimation(AnimationUtil.moveToViewBottom());
                break;
        }
    }

    private void sendLike() {
        String url = Constants.RequestUrl + Constants.collectionUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","e8cd6c84c0e9e5a690f5a46d0cf969afe954000b");
        params.put("id",id+"");
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
                                mLikeIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sc2));
                                mLikeTv.setText("已收藏");
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
        params.put("token","e8cd6c84c0e9e5a690f5a46d0cf969afe954000b");
        params.put("id",id+"");
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
                                mLikeIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sc1));
                                mLikeTv.setText("收藏");
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
        params.put("token","e8cd6c84c0e9e5a690f5a46d0cf969afe954000b");
        params.put("id",id+"");
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
                                if(commentData == null){
                                    mCommentNumTv.setText("留言 0" );
                                    showEmpty();
                                }else {
                                    mCommentNumTv.setText("留言 " + commentData.size());
                                    emptyLayout.setVisibility(View.GONE);
                                    CommentListAdapter adapter = new CommentListAdapter(commentData, GoodsDetailActivity.this, GoodsDetailActivity.this);
                                    commentList.setAdapter(adapter);
                                    adapter.setOnCommentClickListener(GoodsDetailActivity.this);
                                }
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

    private void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
    }

    private void getData() {
        String url = Constants.RequestUrl + Constants.mainDetailUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","e8cd6c84c0e9e5a690f5a46d0cf969afe954000b");
        params.put("id",id+"");
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
                                JSONArray array = obj.getJSONArray("oss_imgs");
                                ArrayList<String> imageList = new ArrayList<String>();
                                for(int i = 0;i<array.length();i++){
                                    imageList.add(array.getString(i));
                                }
                                data.setOss_imgs(imageList);
                                data.setContent(obj.getString("content"));
                                data.setId(obj.getString("id"));
                                data.setCreat_time(obj.getString("creat_time"));
                                data.setPrice(obj.getString("price"));
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
        Glide.with(GoodsDetailActivity.this).load(data.getOss_imgs().get(0)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImg1.setImageBitmap(resource);
                    }
                });
        Glide.with(GoodsDetailActivity.this).load(data.getOss_imgs().get(1)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImg2.setImageBitmap(resource);
                    }
                });
        Glide.with(GoodsDetailActivity.this).load(data.getOss_imgs().get(2)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImg3.setImageBitmap(resource);
                    }
                });

        if(data.getMy_user_type().equals("0")){
            mOptionIv.setVisibility(View.GONE);
            likeRl.setVisibility(View.VISIBLE);
        }else {
            mOptionIv.setVisibility(View.VISIBLE);
            likeRl.setVisibility(View.GONE);
        }
        Glide.with(GoodsDetailActivity.this).load(data.getUser_head_img()).asBitmap().placeholder(R.drawable.pic_tx).error(R.drawable.pic_tx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        headerImg.setImageBitmap(resource);
                    }
                });

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
        if (data.getCollection_status().equals("0")){
            mLikeIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sc1));
            mLikeTv.setText("收藏");
        }else {
            mLikeIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sc2));
            mLikeTv.setText("已收藏");
        }
        if(data.getMy_user_type().equals("1")){//是自己
            mOptionIv.setVisibility(View.VISIBLE);
            initGridData(data.getOss_imgs(), "1");
        }else {
            mOptionIv.setVisibility(View.GONE);
            if(data.getThis_user_type().equals("0")){
                initGridData(data.getOss_imgs(),"0");   // bushi vip
            }else {
                initGridData(data.getOss_imgs(), "1");
            }

        }
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.scrollTo(0,0);
            }
        });
    }

    private void initGridData(ArrayList<String> images, String type) {
        ArrayList<String> list = new ArrayList<>();
        if(type.equals("0")){
            list.add("");
            list.add("");
            list.add("");
            list.add("");
        }else {
            for (int i = 3; i < images.size(); i++) {
                String url = images.get(i);
                list.add(url);
            }
        }
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(GoodsDetailActivity.this,list,type);
        gv_gridview.setAdapter(imageGridAdapter);
    }


    /**
     * 评论框
     * @param id
     * @param name
     */
    public void showCommentFrag(String id,String name) {
        getCommentFragment(id,name).show(getSupportFragmentManager(), COMMENT_FRAGMENT);
    }

    private CommentFragment mCommentFragment;

    private CommentFragment getCommentFragment(String id,String name) {
        if (mCommentFragment == null) {
            mCommentFragment = (CommentFragment) getSupportFragmentManager().
                    findFragmentByTag(COMMENT_FRAGMENT);
            if (mCommentFragment == null) {
                mCommentFragment = new CommentFragment();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        mCommentFragment.setArguments(bundle);
        return mCommentFragment;
    }
    public void sendCommend(String id, String msg) {
        String url = Constants.RequestUrl + Constants.PushCommentUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token","e8cd6c84c0e9e5a690f5a46d0cf969afe954000b");
        params.put("id",id+"");
        params.put("comment_content",msg+"");
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
                           //     JSONObject obj = jsonObject.getJSONObject("data");
                                getCommentData();
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
    @Override
    public void onCommentClick(int position, String id, String name) {
        showCommentFrag(id,name);
    }

    @Override
    public void onTwoCommentClick(int position, String id, String name) {
        showCommentFrag(id,name);
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
