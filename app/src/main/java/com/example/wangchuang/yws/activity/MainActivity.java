package com.example.wangchuang.yws.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.LocalBroadcastManager;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.adapter.MainListAdapter;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.PermissionsManager;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.huanxin.Constant;
import com.example.wangchuang.yws.huanxin.DemoHelper;
import com.example.wangchuang.yws.huanxin.InviteMessgeDao;
import com.example.wangchuang.yws.huanxin.UserDao;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.loading.ProgressView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
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
    private ImageView iv_message,mineIv;
    private int pageNo = 0;
    private int pageSize = 10;
    private ArrayList<GoodsModel> listData = new ArrayList<>();
    private MainListAdapter adapter;
    private boolean loading = false;
    private int currentPageSize;
    private RelativeLayout emptyLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        showExceptionDialogFromIntent(getIntent());
        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);
        registerBroadcastReceiver();


        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        //debug purpose only
        registerInternalDebugReceiver();
        emptyLayout = (RelativeLayout) findViewById(R.id.empty_layout);
        hao_recycleview = (HaoRecyclerView) findViewById(R.id.hao_recycleview);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        mineIv = (ImageView) findViewById(R.id.iv_mine);
        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValueStorage.getString("islogin") != null && ValueStorage.getString("islogin").equals("1")) {
                    startActivity(new Intent(MainActivity.this, ConversationActivity.class));
                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
        mineIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,MineActivity.class));

                if(ValueStorage.getString("islogin") != null && ValueStorage.getString("islogin").equals("1")) {
                    startActivity(new Intent(MainActivity.this,MineTestActivity.class));
                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
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
        params.put("limit",pageSize+"");
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


    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };
    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
            }
        });
    }
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        //intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();

                String action = intent.getAction();
               /* if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }*/
               /* //red packet code : 处理红包回执透传消息
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
                    if (conversationListFragment != null){
                        conversationListFragment.refresh();
                    }
                }*/
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onFriendRequestAccepted(String username) {}
        @Override
        public void onFriendRequestDeclined(String username) {}
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        /*if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }*/
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                /*if (count > 0) {
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }*/
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }
    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
               }
        }
    }
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private void showExceptionDialogFromIntent(Intent intent) {
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {}

                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
