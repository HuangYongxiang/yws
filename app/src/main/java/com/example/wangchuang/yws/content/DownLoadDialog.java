package com.example.wangchuang.yws.content;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.utils.DialogTool;


public class DownLoadDialog extends Dialog {

	private String initstr;
	private String color;
	private String strTitle;

	public DownLoadDialog(Activity activity, String initstr, String color) {
		super(activity);
		this.activity = activity;
		this.initstr = initstr;
		this.color = color;
		this.setCanceledOnTouchOutside(false);
	}

	TextView tv_down;
	TextView tv_title;
	ImageView iv_pro;
	LinearLayout lin_title;
	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.loc_background);
		setContentView(R.layout.download_dialog);
		tv_down = (TextView) findViewById(R.id.tv_down);
		iv_pro = (ImageView) findViewById(R.id.iv_pro);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lin_title = (LinearLayout) findViewById(R.id.lin_title);
		tv_down.setText(initstr);
		int strokeColor = Color.parseColor(color);// 边框颜色
		iv_pro.setBackgroundColor(strokeColor);
		tv_down.setTextColor(strokeColor);
		if (this.strTitle != null) {
			lin_title.setVisibility(View.VISIBLE);
			tv_title.setText(strTitle);
		}
	}

	public void setProValue(int value) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				DialogTool.dip2px(activity, 290 * value / 100),
				DialogTool.dip2px(activity, 1));
		iv_pro.setLayoutParams(lp);
	}

	public void setMyTitle(String title) {
		this.strTitle = title;
	}

	public void setTv(String str) {
		tv_down.setText(str);
	}

}
