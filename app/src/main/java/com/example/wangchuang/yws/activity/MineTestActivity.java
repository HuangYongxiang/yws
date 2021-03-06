package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.example.wangchuang.yws.utils.DialogTool;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.ObservableScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MineTestActivity extends BaseActivity implements View.OnClickListener,ObservableScrollView.OnObservableScrollViewScrollChanged{
    private ObservableScrollView sv_contentView;
    private LinearLayout ll_topView,ll_fixedView;
    private ImageView alterBackImg,alterHeaderImg;
    private RelativeLayout creaidLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    private TabLayoutAdpater mAdapter;
    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<GoodsModel> listData = new ArrayList<>();
    private TextView mNameTv,mAlterNameTv,mLikePersonTv,mFansTv,mCardAuthTv,mSexAuthTv;
    private CircularImage headerView;
    private ImageView mSexIv,mVipIv,mPublishIv;
    private MineModel data;
    //用来记录内层固定布局到屏幕顶部的距离
    private int mHeight;
    private ImageView backgroundIv;
    private ImageView backIv;
    private String alterMsg = "";
    private ArrayList<String> imgUrl = new ArrayList<>();

    private TextView tv_option;
    private RelativeLayout rl_sex_auth,rl_vip_auth,rl_publish_service,rl_card_auth;
    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_test;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        alterBackImg = (ImageView) findViewById(R.id.alter_background_img);
        alterHeaderImg = (ImageView) findViewById(R.id.alter_header);
        backgroundIv = (ImageView) findViewById(R.id.pic_background);
        sv_contentView= (ObservableScrollView) findViewById(R.id.sv_contentView);
        ll_topView= (LinearLayout) findViewById(R.id.ll_topView);
        ll_fixedView= (LinearLayout) findViewById(R.id.ll_fixedView);
        sv_contentView.setOnObservableScrollViewScrollChanged(this);
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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager_contentView);
        backIv = (ImageView) findViewById(R.id.iv_back);
        creaidLayout = (RelativeLayout) findViewById(R.id.rl_reputation);

        tv_option = (TextView) findViewById(R.id.tv_option);
        rl_sex_auth = (RelativeLayout) findViewById(R.id.rl_sex_auth);
        rl_vip_auth = (RelativeLayout) findViewById(R.id.rl_vip_auth);
        rl_publish_service = (RelativeLayout) findViewById(R.id.rl_publish_service);
        rl_card_auth = (RelativeLayout) findViewById(R.id.rl_card_auth);

        rl_vip_auth.setOnClickListener(this);
        rl_publish_service.setOnClickListener(this);
        rl_card_auth.setOnClickListener(this);
        creaidLayout.setOnClickListener(this);
        backIv.setOnClickListener(this);
        mPublishIv.setOnClickListener(this);
        mAlterNameTv.setOnClickListener(this);
        tv_option.setOnClickListener(this);
        rl_sex_auth.setOnClickListener(this);

        mFansTv.setOnClickListener(this);
        mLikePersonTv.setOnClickListener(this);
        alterBackImg.setOnClickListener(this);
        alterHeaderImg.setOnClickListener(this);
        initViewPager();
    }

    private void initViewPager() {

        list_fragment.add(PublishGoodsFragment.newInstance(0));
        list_fragment.add(LikeGoodsFragment.newInstance(0));
        list_title.add("我的发布");
        list_title.add("我的收藏");
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        //getFragmentManager是fragment所在父容器的碎片管理，而getChildFragmentManager是fragment所在子容器的碎片管理。
        // 如果用getFragmentManager会在viewpager中出现fragment不会加载的情况，所以切换回去就出现了白板。
        //   mAdapter = new GroupTuanPagerAdpater(getActivity(), getActivity().getSupportFragmentManager(), list_fragment, list_title);
        mAdapter = new TabLayoutAdpater(MineTestActivity.this, getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 0)
                    sv_contentView.smoothScrollTo(0, mHeight);
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
                        ToastUtil.show(MineTestActivity.this,"网络异常");
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
                            ToastUtil.show(MineTestActivity.this, response.msg);
                        }
                    }
                });
    }

    private void initData() {
        Glide.with(MineTestActivity.this).load(data.getOss_head_img()).asBitmap().placeholder(R.drawable.pic_tx).error(R.drawable.pic_tx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        headerView.setImageBitmap(resource);
                    }
                });
        Glide.with(MineTestActivity.this).load(data.getOss_background_img()).asBitmap().placeholder(R.drawable.pic_grzx).error(R.drawable.pic_grzx).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        backgroundIv.setImageBitmap(resource);
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
            case R.id.rl_vip_auth:
                startActivity(new Intent(MineTestActivity.this,VipActivity.class));
                break;
            case R.id.rl_publish_service:
                startActivity(new Intent(MineTestActivity.this,TequanActivity.class));
                break;
            case R.id.rl_card_auth:
                if (data.getPeople_type().equals("0")){
                    startActivity(new Intent(MineTestActivity.this,SmActivity.class));
                }else if (data.getPeople_type().equals("1")){
                    //mCardAuthTv.setText("审核中");
                }else if (data.getPeople_type().equals("2")){
                    startActivity(new Intent(MineTestActivity.this,SmActivity2.class));
                }

                break;
            case R.id.rl_sex_auth:
                if (data.getSex_type().equals("0")){
                    startActivity(new Intent(MineTestActivity.this,SexActivity.class));
                }else if (data.getSex_type().equals("1")){
                    //mSexAuthTv.setText("审核中");
                }else if (data.getSex_type().equals("2")){
                    startActivity(new Intent(MineTestActivity.this,SexActivity2.class));
                }

                break;
            case R.id.tv_alter_name:
                startActivity(new Intent(MineTestActivity.this,XiugaiActivity.class));
                break;
            case R.id.tv_option:
                startActivity(new Intent(MineTestActivity.this,SettingActivity.class));
                break;
            case R.id.tv_my_fans:
                startActivity(new Intent(MineTestActivity.this,FansPersonActivity.class));
                break;
            case R.id.tv_my_like:
                startActivity(new Intent(MineTestActivity.this,LikePersonActivity.class));
                break;
            case R.id.iv_publish:

                startActivity(new Intent(MineTestActivity.this,PublishActivity.class));
                break;
            case R.id.rl_reputation:
                startActivity(new Intent(MineTestActivity.this,CreditActivity.class));
                break;
            case R.id.alter_background_img:
                alterMsg = "backgroundImg";
                imgUrl.clear();
                goSelectGallery(1);
                break;
            case R.id.alter_header:
                alterMsg = "headerImg";
                imgUrl.clear();
                goSelectGallery(1);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
    private void goSelectGallery(int num) {
        ImagePicker.getInstance().setSelectLimit(num);
        if (num == 1) {
            ImagePicker.getInstance().setMultiMode(false);
        } else {
            ImagePicker.getInstance().setMultiMode(true);
        }
        Intent intent = new Intent(MineTestActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, Constants.IMAGE_PICKER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == Constants.IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(images!=null&&images.size()>0){
                    for (ImageItem bean : images) {
                        imgUrl.add(bean.path);
                    }
                    if (alterMsg.equals("headerImg")){
                        updateHeader(imgUrl);
                    }else {
                        updateBackImg(imgUrl);
                    }
                }
            } else {

            }
        }
    }

    private void updateBackImg(final ArrayList<String> imgUrls) {
        File headerImg = new File(imgUrls.get(0));
        String url = Constants.BaseUrl + Constants.updateBackUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token")+"");
        showLoadingDialog("上传中....");
        OkHttpUtils.post()//
                .addFile("background_img","background_img.png",headerImg)
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(MineTestActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<MineModel>(){}.getType();
                                Glide.with(MineTestActivity.this).load(imgUrls.get(0))
                                        .placeholder(R.drawable.pic_grzx).crossFade().error(R.drawable.pic_grzx).into(backgroundIv);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.code.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(MineTestActivity.this, response.msg);
                        }
                    }
                });
    }

    private void updateHeader(final ArrayList<String> imgUrls) {
        File headerImg = new File(imgUrls.get(0));
        String url = Constants.BaseUrl + Constants.updateHeaderUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token")+"");
        showLoadingDialog("上传中....");
        OkHttpUtils.post()//
                .addFile("head_img","head_img.png",headerImg)
                .params(params)//
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(MineTestActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<MineModel>(){}.getType();
                                Glide.with(MineTestActivity.this).load(imgUrls.get(0))
                                        .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(headerView);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.code.equals("400")) {
                            dismissLoadingDialog();
                            ToastUtil.show(MineTestActivity.this, response.msg);
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
                ll_topView.removeView(tabLayout);
                ll_fixedView.addView(tabLayout);
            }
        }else{
            if(tabLayout.getParent()!=ll_topView){
                ll_fixedView.removeView(tabLayout);
                ll_topView.addView(tabLayout);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        /*if(eventCenter != null && eventCenter.getEventCode() == Constants.EB_CODE_REFRESH_MINE){
            getData();
        }*/
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }



}
