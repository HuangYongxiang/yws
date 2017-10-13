package com.example.wangchuang.yws.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.Banben;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.DownLoadDialog;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.utils.CommonUtil;
import com.example.wangchuang.yws.utils.DialogTool;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SettingActivity extends BaseActivity {

    private TextView yijina,huancun,banben;
    private Button loginout;
    private CheckBox xuan;
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        yijina=(TextView) findViewById(R.id.yijian);
        huancun=(TextView) findViewById(R.id.huancun);
        banben=(TextView) findViewById(R.id.banben);
        loginout=(Button) findViewById(R.id.btn_loginout);
        xuan=(CheckBox) findViewById(R.id.xuan);
        xuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xuan.isChecked()){
                    xuan.setBackgroundResource(R.drawable.switch_off);
                }else{
                    xuan.setBackgroundResource(R.drawable.switch_on);
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }
    //退出登录
    public void login(View view) {
        //杀掉之前的所有activity*/
        Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void yijian(View view) {
        startActivity(new Intent(SettingActivity.this,RegisterActivity.class));
    }

    public void huancun(View view) {
        showLoadingDialog("正在清除缓存");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                ToastUtil.show(SettingActivity.this,"清除完成");
            }
        }, 2000);
    }
    public void banben(View view) {
      final Banben banbens=new Banben();
        DialogTool.showAlertDialogOptionFours(SettingActivity.this,
                "版本更新", "发现新版本，是否更新？" ,
                "确定", "取消", new DialogTool.OnAlertDialogOptionListener() {
                    @Override
                    protected void onClickOption(int p) {
                        super.onClickOption(p);
                        if (p == 0) {
                            dtapk = new DownLoadTaskApk();
                           /* myApkPath = getApkPath() + File.separator +
                                    banbens.banben + ".apk";*/
                            dtapk.execute(banbens.url);
                        }
                    }
                });
    }
    DownLoadTaskApk dtapk;
    DownLoadDialog downLoadingDialogapk;
    String myApkPath;
    //版本更新
    protected void gengxin(final Banben banbens){
        if(banbens.banben.equals(getVersion())){
            ToastUtil.show(SettingActivity.this,"已是最新版本");
        }else {

            DialogTool.showAlertDialogOptionFours(SettingActivity.this,
                    "版本更新", "发现新版本，是否更新？" ,
                    "确定", "取消", new DialogTool.OnAlertDialogOptionListener() {
                        @Override
                        protected void onClickOption(int p) {
                            super.onClickOption(p);
                            if (p == 0) {
                                dtapk = new DownLoadTaskApk();
                                myApkPath = getApkPath() + File.separator +
                                        banbens.banben + ".apk";
                                dtapk.execute(banbens.url);
                            }
                        }
                    });}
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
    //版本更新下载
    // 设置三种类型参数分别为String,Integer,String
    class DownLoadTaskApk extends AsyncTask<String, Integer, File> {

        // 可变长的输入参数，与AsyncTask.exucute()对应
        @Override
        protected File doInBackground(String... params) {
            String urlStr = params[0];
            OutputStream output = null;
            File file = null;
            try {
                file = new File(myApkPath);
                if (file.exists()) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
                file.createNewFile();// 新建文件
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                InputStream input = conn.getInputStream();
                long total = 1l;
                try {
                    total = conn.getContentLength();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                output = new FileOutputStream(file);
                int length = 0;
                int count = 0;
                // 读取大文件
                byte[] buffer = new byte[4 * 1024];
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                    try {
                        count += length;
                        // 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                        publishProgress((int) ((count / (float) total) * 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return file;
        }

        protected void onPostExecute(File result) {
            try {
                // 返回HTML页面的内容
                // 刷新页面
                downLoadingDialogapk.dismiss();
                if (null == result) {
                    ToastUtil.show(SettingActivity.this,"访问超时！");
                } else if (result.length() == 0) {
                    ToastUtil.show(SettingActivity.this,"下载失败！");
                } else {
                    Intent intent = new Intent();
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(result),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                downLoadingDialogapk.setTv("下载文件...\t\t" + values[0] + "%");
                downLoadingDialogapk.setProValue(values[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                super.onPreExecute();
                // 设置进度条风格，风格为圆形，旋转的
               /* downLoadingDialogapk = new DownLoadDialog(ctx,
                        "下载文件...\t\t0%", ctx.getResources().getString(
                        R.color.my_blue)+"");*/
                downLoadingDialogapk = new DownLoadDialog(SettingActivity.this,
                        "下载文件...\t\t0%","#42d4fd");
                downLoadingDialogapk.setCancelable(true);
                downLoadingDialogapk
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                try {
                                    dtapk.cancel(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                downLoadingDialogapk.setCanceledOnTouchOutside(false);
                downLoadingDialogapk.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }}
}
