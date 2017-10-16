package com.example.wangchuang.yws.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.TabLayoutAdpater;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.fragment.LikeGoodsFragment;
import com.example.wangchuang.yws.fragment.PublishGoodsFragment;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;
import okhttp3.Call;

public class MineActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    private TabLayoutAdpater mAdapter;

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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        list_fragment.add(PublishGoodsFragment.newInstance(0));
        list_fragment.add(LikeGoodsFragment.newInstance(0));
        list_title.add("动态");
        list_title.add("同城");
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        //getFragmentManager是fragment所在父容器的碎片管理，而getChildFragmentManager是fragment所在子容器的碎片管理。
        // 如果用getFragmentManager会在viewpager中出现fragment不会加载的情况，所以切换回去就出现了白板。
        //   mAdapter = new GroupTuanPagerAdpater(getActivity(), getActivity().getSupportFragmentManager(), list_fragment, list_title);
        mAdapter = new TabLayoutAdpater(MineActivity.this, getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
