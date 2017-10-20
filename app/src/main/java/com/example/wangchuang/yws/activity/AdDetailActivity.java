package com.example.wangchuang.yws.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.utils.WebViewUtils;

public class AdDetailActivity extends Activity{

	private ProgressBar progressBar;
	private WebView webView;
	private Intent intent;
	private LinearLayout _layout;
	private boolean is_forword;
    private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fx_detail);
		//当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏 g
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		_layout = (LinearLayout) findViewById(R.id.root_ll);

		webView = (WebView) findViewById(R.id.webView_info);
		progressBar = (ProgressBar) findViewById(R.id.web_progressBar);
		back = (ImageView) findViewById(R.id.tv_regir);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(webView != null){
					if(webView.canGoBack()){
						webView.goBack();
					}else{
						finish();
					}
				}
				else{
					finish();
				}
			}
		});
		intent = getIntent();
		Bundle bun = intent.getExtras();
		String url = bun.getString("url");
		String title = bun.getString("title");
		WebViewUtils.initWebViewUtils(webView, progressBar, AdDetailActivity.this).setWebView(url);
	}
	public static void startAdDetailActivity(Context context, String title, String url){
		Intent intent = new Intent(context,AdDetailActivity.class);
		intent.putExtra("url",url);
		intent.putExtra("title",title);
		context.startActivity(intent);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(webView != null){
			_layout.removeView(webView);  
			webView.removeAllViews(); 
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode  == KeyEvent.KEYCODE_BACK){
			if(webView != null){
				if(webView.canGoBack() && !is_forword){
					webView.goBack();
					return true;
				}
			}
			else{
				finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
