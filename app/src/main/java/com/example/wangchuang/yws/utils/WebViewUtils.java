package com.example.wangchuang.yws.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled") public class WebViewUtils {

	private static WebViewUtils webViewUtils;

	private WebView webView;
	private ProgressBar bar;
	private Activity activity;



	public static WebViewUtils initWebViewUtils(WebView webview, ProgressBar bar, Activity activity){
		webViewUtils = new WebViewUtils(webview,bar,activity);
		return webViewUtils;

	}
	private WebViewUtils(WebView webView, ProgressBar bar, Activity activity){
		this.webView = webView;
		this.bar = bar;
		this.activity = activity;
	}

	public void setWebView(String contentData){
		WebSettings settings;
		webView.loadUrl(contentData);
		settings = webView.getSettings();
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);
		//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDomStorageEnabled(true); 
		webView.setWebViewClient(new WebViewClientEmb());
		if(bar != null){
			webView.setWebChromeClient(new WebCromeClientEmb());
		}
	}

	public class WebCromeClientEmb extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				bar.setVisibility(View.GONE);
			} else {
				if (bar.getVisibility() == View.GONE)
					bar.setVisibility(View.VISIBLE);
				bar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
			// to do your work
			// ...
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}
		//		@Override
		//		public boolean onJsPrompt(WebView view, String url, String message,
		//				String defaultValue, JsPromptResult result) {
		//			// TODO Auto-generated method stub
		//			return super.onJsPrompt(view, url, message, defaultValue, result);
		//		}

		/*  @Override
	    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
	        lp.x = l;
	        lp.y = t;
	        progressbar.setLayoutParams(lp);
	        super.onScrollChanged(l, t, oldl, oldt);
	    }*/
	}

	public class WebViewClientEmb extends WebViewClient {
		// 在WebView中而不是系统默认浏览器中显示页面
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					try {
						if( url.startsWith("http:") || url.startsWith("https:") ) {
							return false;
						}

						// Otherwise allow the OS to handle things like tel, mailto, etc.
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						activity.startActivity( intent );
					} catch (Exception e) {
						// TODO: handle exception
					}
					Log.e("TAG", url);
					return true;
				}
			});
			return true;
		}
		// 页面载入前调用
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon){
			super.onPageStarted(view, url, favicon);   
			/*dialog = MyDialog.showProgressBarDialog(context, "正在加载网页");
			if(dialog != null && dialog.isShowing()){
				dialog.show();
			}*/
		}
		// 页面载入完成后调用
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			/*if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
				dialog = null;
			}*/
		}
	}

}
