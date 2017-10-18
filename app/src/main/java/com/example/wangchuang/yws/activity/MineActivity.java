package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.MainListAdapter;
import com.example.wangchuang.yws.adapter.TabLayoutAdpater;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.MineModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.fragment.LikeGoodsFragment;
import com.example.wangchuang.yws.fragment.PublishGoodsFragment;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.loading.ProgressView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;
import okhttp3.Call;

public class MineActivity extends BaseActivity implements View.OnClickListener{
    HaoRecyclerView hao_recycleview;
    SwipeRefreshLayout swiperefresh;
    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<GoodsModel> listData = new ArrayList<>();
    private TextView mNameTv,mAlterNameTv,mLikePersonTv,mFansTv,mCardAuthTv,mSexAuthTv;
    private CircularImage headerView;
    private ImageView mSexIv,mVipIv,mPublishIv;
    private MineModel data;
    @Override
    public int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        initViewPager();
    }

    private void initViewPager() {
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mAlterNameTv = (TextView) findViewById(R.id.tv_alter_name);
        mLikePersonTv = (TextView) findViewById(R.id.tv_my_like);
        mFansTv = (TextView) findViewById(R.id.tv_my_fans);
        mCardAuthTv = (TextView) findViewById(R.id.tv_card_auth);
        mSexAuthTv = (TextView) findViewById(R.id.tv_sex_auth);
        mSexIv = (ImageView) findViewById(R.id.iv_sex);
        mVipIv = (ImageView) findViewById(R.id.iv_vip);
        mPublishIv = (ImageView) findViewById(R.id.iv_publish);
        headerView = (CircularImage) findViewById(R.id.iv_header);

        mPublishIv.setOnClickListener(this);
        mFansTv.setOnClickListener(this);
        mLikePersonTv.setOnClickListener(this);



        hao_recycleview = (HaoRecyclerView) findViewById(R.id.hao_recycleview);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new MainListAdapter(mContext, listData);
        hao_recycleview.setAdapter(adapter);
        swiperefresh.setColorSchemeResources(R.color.btn_green_unpressed_color, R.color.btn_green_unpressed_color, R.color.btn_green_unpressed_color,
                R.color.btn_green_unpressed_color);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initNetData();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hao_recycleview.setLayoutManager(layoutManager);

        //设置自定义加载中和到底了效果
        ProgressView progressView = new ProgressView(mContext);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        hao_recycleview.setFootLoadingView(progressView);

        TextView textView = new TextView(MainActivity.this);
        textView.setText("已经到底啦~");
        hao_recycleview.setFootEndView(textView);


        hao_recycleview.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {

                pageNo++;
                hao_recycleview.refreshComplete();
                hao_recycleview.loadMoreComplete();
                if (pageNo > 1) {
                    loading = false;
                }
                getData();
            }
        });
        getData();
    }

    private void getData() {
        String url = Constants.RequestUrl + Constants.mineUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token")+"");
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
                        ToastUtil.show(MineActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<MineModel>(){}.getType();
                                data = new Gson().fromJson(dataJson, type);

                                initData();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.code.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(MineActivity.this, response.msg);
                        }
                    }
                });

    }

    private void initData() {
        Glide.with(MineActivity.this).load(data.getOss_head_img()).asBitmap().placeholder(R.drawable.pic_tx).error(R.drawable.pic_tx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        headerView.setImageBitmap(resource);
                    }
                });
        if(data.getSex().equals("1")){
            mSexIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_nan));
        }else {
            mSexIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_nv));
        }
        if(data.getVip_type() != null && data.getVip_type().equals("1")){
            mVipIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            mVipIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip2));
        }
        if (data.getPeople_type().equals("0")){
            mCardAuthTv.setText("未认证");
        }else if (data.getPeople_type().equals("1")){
            mCardAuthTv.setText("审核中");
        }else if (data.getPeople_type().equals("2")){
            mCardAuthTv.setText("已认证");
        }
        if (data.getSex_type().equals("0")){
            mSexAuthTv.setText("未认证");
        }else if (data.getSex_type().equals("1")){
            mSexAuthTv.setText("审核中");
        }else if (data.getSex_type().equals("2")){
            mSexAuthTv.setText("已认证");
        }
        mNameTv.setText(data.getUser_name());

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_my_fans:
                startActivity(new Intent(MineActivity.this,FansPersonActivity.class));
                break;
            case R.id.tv_my_like:
                startActivity(new Intent(MineActivity.this,LikePersonActivity.class));
                break;
            case R.id.iv_publish:
                startActivity(new Intent(MineActivity.this,PublishActivity.class));
                break;
        }
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
