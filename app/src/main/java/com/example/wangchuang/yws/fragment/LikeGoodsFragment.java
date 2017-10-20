package com.example.wangchuang.yws.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.OtherPeopleActivity;
import com.example.wangchuang.yws.adapter.CollectionListAdapter;
import com.example.wangchuang.yws.adapter.MainListAdapter;
import com.example.wangchuang.yws.base.BaseFragment;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.LikeGoodsModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
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

import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;
import okhttp3.Call;

/**
 * Created by zhaoming on 2017/10/14.
 */

public class LikeGoodsFragment extends BaseFragment implements CollectionListAdapter.OnCancelClickListener{
    HaoRecyclerView hao_recycleview;
    SwipeRefreshLayout swiperefresh;
    private RelativeLayout emptyLayout;

    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<LikeGoodsModel> listData = new ArrayList<>();
    private CollectionListAdapter adapter;
    private boolean loading = false;
    private int currentPageSize;

    public static LikeGoodsFragment mLikeGoodsFragment;
    public static final String GOODS_GID = "goods_id";

    public static LikeGoodsFragment newInstance(int mid) {
        Bundle args = new Bundle();
        args.putInt(LikeGoodsFragment.GOODS_GID, mid);
        mLikeGoodsFragment = new LikeGoodsFragment();
        mLikeGoodsFragment.setArguments(args);
        return mLikeGoodsFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_goods;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        emptyLayout = (RelativeLayout) rootView.findViewById(R.id.empty_layout);
        hao_recycleview = (HaoRecyclerView) rootView.findViewById(R.id.hao_recycleview);
        swiperefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        adapter = new CollectionListAdapter(getActivity(), listData);
        hao_recycleview.setAdapter(adapter);
        adapter.setOnCancelClickListener(this);
        swiperefresh.setColorSchemeResources(R.color.btn_green_unpressed_color, R.color.btn_green_unpressed_color, R.color.btn_green_unpressed_color,
                R.color.btn_green_unpressed_color);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initNetData();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hao_recycleview.setLayoutManager(layoutManager);

        //设置自定义加载中和到底了效果
        ProgressView progressView = new ProgressView(getActivity());
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        hao_recycleview.setFootLoadingView(progressView);

        TextView textView = new TextView(getActivity());
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
        String url = Constants.RequestUrl + Constants.myCollectionUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token")+"");
        params.put("limit",pageSize+"");
        params.put("p", pageNo + "");
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
                        ToastUtil.show(getActivity(),"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            //Type type = new TypeToken<Logins>(){}.getType();
                            //ToastUtil.show(RegisterActivity.this,"保存成功");
                            try {
                                ArrayList<LikeGoodsModel> list;
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<LikeGoodsModel>>(){}.getType();
                                list = new Gson().fromJson(dataJson, type);


                                if (pageNo == 1) {
                                    refresh(list);
                                    boolean showEmpty;
                                    if (listData == null || listData.size() == 0) {
                                        emptyLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        emptyLayout.setVisibility(View.GONE);
                                    }


                                } else {
                                    loadMore(list);
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.code.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(getActivity(), response.msg);
                        }
                    }
                });

    }

    public void refresh(ArrayList<LikeGoodsModel> requestInfo) {


        //注意此处
        hao_recycleview.setCanloadMore(true);
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        swiperefresh.setRefreshing(false);
        listData.clear();
        if(requestInfo != null&&requestInfo.size() == pageSize) {
            if (requestInfo != null && requestInfo.size() > 0) {
                listData.addAll(requestInfo);
               /* if (currentPageSize < pageSize) {
                    hao_recycleview.loadMoreEnd();
                    hao_recycleview.setCanloadMore(false);
                }*/
            } else {
                hao_recycleview.loadMoreEnd();
                hao_recycleview.setCanloadMore(false);
            }
        }else if (requestInfo != null && requestInfo.size() < pageSize) {
            listData.addAll(requestInfo);
            hao_recycleview.loadMoreEnd();
            hao_recycleview.setCanloadMore(false);
        }else {
            hao_recycleview.loadMoreEnd();
            hao_recycleview.setCanloadMore(false);
        }

        adapter.notifyDataSetChanged();
        hao_recycleview.smoothScrollToPosition(0);
    }
    public void loadMore(List<LikeGoodsModel> data) {
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
    public void onCancelClick(int position, String id) {
        String url = Constants.RequestUrl + Constants.cancelCollectionUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token",ValueStorage.getString("token")+"");
        params.put("id",id +"");
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
                        ToastUtil.show(getActivity(),"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            //dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                //     JSONObject obj = jsonObject.getJSONObject("data");
                                initNetData();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.code.equals("400")) {
                            //dismissLoadingDialog();
                            ToastUtil.show(getActivity(), response.msg);
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
