package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.wangchuang.yws.bean.OtherPeopleModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.fragment.LikeGoodsFragment;
import com.example.wangchuang.yws.fragment.PublishGoodsFragment;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.ObservableScrollView;
import com.example.wangchuang.yws.view.loading.ProgressView;
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

import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;
import okhttp3.Call;

public class OtherPeopleActivity extends BaseActivity implements View.OnClickListener,ObservableScrollView.OnObservableScrollViewScrollChanged{
    private ObservableScrollView sv_contentView;
    private LinearLayout ll_topView,ll_fixedView;
    private ImageView backgroundLayout,backIv;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    HaoRecyclerView hao_recycleview;
    SwipeRefreshLayout swiperefresh;
    private TextView mNameTv,mAddLikeTv,mCardAuthTv,mSexAuthTv,mOptionTv;
    private ImageView mSexIv,mVipIv;
    private CircularImage headerView;
    private RelativeLayout creditLayout;
    View view_tip;
    private TabLayoutAdpater mAdapter;
    private OtherPeopleModel data;
    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<GoodsModel> listData = new ArrayList<>();
    private List<Fragment> list_fragment = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    private MainListAdapter adapter;
    private boolean loading = false;
    private int currentPageSize;
    private String uid;
    public static String othersId;
    private int mHeight;
    private boolean isAddLike = false;
    @Override
    public int getLayoutId() {
        return R.layout.activity_other_people;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("uid");
            setUid(uid);
        }
        backgroundLayout = (ImageView) findViewById(R.id.pic_background);
        backIv = (ImageView) findViewById(R.id.iv_back);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        sv_contentView= (ObservableScrollView) findViewById(R.id.sv_contentView);
        ll_topView= (LinearLayout) findViewById(R.id.ll_topView);
        ll_fixedView= (LinearLayout) findViewById(R.id.ll_fixedView);
        sv_contentView.setOnObservableScrollViewScrollChanged(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager_contentView);

        mNameTv = (TextView) findViewById(R.id.tv_name);
        mOptionTv = (TextView) findViewById(R.id.tv_option);
        mOptionTv.setText("举报");
        mAddLikeTv = (TextView) findViewById(R.id.add_like_tv);
        mCardAuthTv = (TextView) findViewById(R.id.tv_card_auth);
        mSexAuthTv = (TextView) findViewById(R.id.tv_sex_auth);
        headerView = (CircularImage) findViewById(R.id.iv_header);
        mSexIv = (ImageView) findViewById(R.id.iv_sex);
        mVipIv = (ImageView) findViewById(R.id.iv_vip);
        creditLayout = (RelativeLayout) findViewById(R.id.rl_reputation);
        creditLayout.setOnClickListener(this);
        backIv.setOnClickListener(this);
        mAddLikeTv.setOnClickListener(this);
        mOptionTv.setOnClickListener(this);
        initViewPager();
    }
    private void initViewPager() {

        list_fragment.add(PublishGoodsFragment.newInstance(0));
        list_title.add("他/她的发布");
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        //getFragmentManager是fragment所在父容器的碎片管理，而getChildFragmentManager是fragment所在子容器的碎片管理。
        // 如果用getFragmentManager会在viewpager中出现fragment不会加载的情况，所以切换回去就出现了白板。
        //   mAdapter = new GroupTuanPagerAdpater(getActivity(), getActivity().getSupportFragmentManager(), list_fragment, list_title);
        mAdapter = new TabLayoutAdpater(OtherPeopleActivity.this, getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getPersonData();
    }


    public void setUid(String uid){
        this.othersId = uid;
    }
    public static String getUid(){
        return othersId;
    }

    private void getPersonData() {
        String url = Constants.RequestUrl + Constants.otherPeopleUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token",ValueStorage.getString("token")+"");
        params.put("uid", uid+"");
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
                        ToastUtil.show(OtherPeopleActivity.this,"网络异常");
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
                                Type type = new TypeToken<OtherPeopleModel>(){}.getType();
                                data = new Gson().fromJson(dataJson, type);
                                initData();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(OtherPeopleActivity.this, response.msg);
                        }
                    }
                });

    }

    private void initData() {
        Glide.with(OtherPeopleActivity.this).load(data.getOss_head_img()).asBitmap().placeholder(R.drawable.pic_tx).error(R.drawable.pic_tx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        headerView.setImageBitmap(resource);
                    }
                });
        Glide.with(OtherPeopleActivity.this).load(data.getOss_background_img()).asBitmap().placeholder(R.drawable.pic_grzx).error(R.drawable.pic_grzx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        backgroundLayout.setImageBitmap(resource);
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
        if(data.getFollow_status() != null && data.getFollow_status().equals("1")){
            isAddLike = true;
            mAddLikeTv.setText("已关注");
        }else {
            isAddLike = false;
            mAddLikeTv.setText("关注");
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
        sv_contentView.post(new Runnable() {
            @Override
            public void run() {
                sv_contentView.scrollTo(0,0);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_like_tv:
                if(isAddLike){
                    cancelLike();
                }else {
                    addLike();
                }
                break;
            case R.id.rl_reputation:
                Intent intent = new Intent();
                intent.setClass(OtherPeopleActivity.this,OtherCreditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid",uid);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_option:
                Intent intent1 = new Intent();
                intent1.setClass(OtherPeopleActivity.this,ReportActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("uid",uid);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void cancelLike() {
        String url = Constants.RequestUrl + Constants.cancelLikeUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token",ValueStorage.getString("token")+"");
        params.put("uid", uid+"");
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
                        ToastUtil.show(OtherPeopleActivity.this,"网络异常");
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
                                Type type = new TypeToken<OtherPeopleModel>(){}.getType();
                                isAddLike = false;
                                mAddLikeTv.setText("关注");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(OtherPeopleActivity.this, response.msg);
                        }
                    }
                });
    }
    private void addLike() {
        String url = Constants.RequestUrl + Constants.addLikeUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token",ValueStorage.getString("token")+"");
        params.put("uid", uid+"");
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
                        ToastUtil.show(OtherPeopleActivity.this,"网络异常");
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
                                Type type = new TypeToken<OtherPeopleModel>(){}.getType();
                                isAddLike = true;
                                mAddLikeTv.setText("已关注");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(OtherPeopleActivity.this, response.msg);
                        }
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            //获取HeaderView的高度，当滑动大于等于这个高度的时候，需要把topView移除当前布局，放入到外层布局
            mHeight=ll_topView.getTop();
        }
    }
    /**
     * @param l Current horizontal scroll origin. 当前滑动的x轴距离
     * @param t Current vertical scroll origin. 当前滑动的y轴距离
     * @param oldl Previous horizontal scroll origin. 上一次滑动的x轴距离
     * @param oldt Previous vertical scroll origin. 上一次滑动的y轴距离
     */
    @Override
    public void onObservableScrollViewScrollChanged(int l, int t, int oldl, int oldt) {
        if(t>=mHeight){
            if(tabLayout.getParent()!=ll_fixedView){
              //  ll_topView.setVisibility(View.VISIBLE);
                ll_topView.removeView(tabLayout);
                ll_fixedView.addView(tabLayout);
            }
        }else{
            if(tabLayout.getParent()!=ll_topView){
                ll_fixedView.removeView(tabLayout);
                ll_topView.addView(tabLayout);
                //ll_topView.setVisibility(View.GONE);
            }
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
