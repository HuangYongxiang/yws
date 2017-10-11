package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.MainListAdapter;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
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

import butterknife.BindView;
import butterknife.OnClick;
import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;
import okhttp3.Call;

public class MainActivity extends BaseActivity {

    HaoRecyclerView hao_recycleview;
    SwipeRefreshLayout swiperefresh;
    View view_tip;

    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<GoodsModel> listData = new ArrayList<>();
    private MainListAdapter adapter;
    private boolean loading = false;
    private int currentPageSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        hao_recycleview = (HaoRecyclerView) findViewById(R.id.hao_recycleview);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        view_tip = (View) findViewById(R.id.view_tip);

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
        initNetData();
    }



    private void initNetData() {
        pageNo = 1;
        loading = false;
        swiperefresh.setProgressViewOffset(false, 0, 30);
        swiperefresh.setRefreshing(true);//直接这样吧
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        getData();
    }
    private void getData() {
        String url = Constants.RequestUrl + Constants.mainListUrl;
        Map<String, String> params = new HashMap<>();
        params.put("limit",10+"");
        params.put("p",pageNo + "");
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
                        ToastUtil.show(MainActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                ArrayList<GoodsModel> list;
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<GoodsModel>>(){}.getType();
                                list = new Gson().fromJson(dataJson, type);


                                if (pageNo == 1) {
                                    refresh(list);
                                    boolean showEmpty;
                                    if (listData == null || listData.size() == 0) {
                                        showEmpty = true;
                                    } else {
                                        showEmpty = false;
                                    }


                                } else {
                                    loadMore(list);
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(MainActivity.this, response.msg);
                        }
                    }
                });

    }

    public void refresh(ArrayList<GoodsModel> requestInfo) {


        //注意此处
        hao_recycleview.setCanloadMore(true);
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        swiperefresh.setRefreshing(false);
        listData.clear();
        if (requestInfo != null  && requestInfo.size() > 0) {
            listData.addAll(requestInfo);
            if (currentPageSize < pageSize) {
                hao_recycleview.loadMoreEnd();
                hao_recycleview.setCanloadMore(false);
            }
        } else {

            hao_recycleview.setCanloadMore(false);
        }

        adapter.notifyDataSetChanged();
        hao_recycleview.smoothScrollToPosition(0);
    }
    public void loadMore(List<GoodsModel> data) {
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        swiperefresh.setRefreshing(false);
        if (data == null && data.size() == 0) {
            hao_recycleview.setCanloadMore(false);
            if (currentPageSize < pageSize) {
                hao_recycleview.loadMoreEnd();
                hao_recycleview.setCanloadMore(false);
            }
        } else {
            hao_recycleview.setCanloadMore(true);
            listData.addAll(data);
            adapter.notifyDataSetChanged();
            hao_recycleview.loadMoreComplete();
            if (currentPageSize < pageSize) {
                hao_recycleview.loadMoreEnd();
                hao_recycleview.setCanloadMore(false);
            }
        }
    }


    private void showError(Exception e) {
        listData.clear();
        adapter.notifyDataSetChanged();
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        hao_recycleview.setCanloadMore(false);
        swiperefresh.setRefreshing(false);
        loading = false;
       /* toggleShowEmpty(true, Constants.subException(mContext, e), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, R.drawable.pic_none);*/
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
