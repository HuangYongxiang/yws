package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.MineActivity;

import java.util.List;

/**
 * Created by kenneth on 2017/3/24.
 * 我的团打卡和未打卡的adapter
 */

public class TabLayoutAdpater extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表
    private Context mContext;

    public TabLayoutAdpater(FragmentManager fm) {
        super(fm);
    }

    private int[] imageView = {R.drawable.icon_fh, R.drawable.icon_fh};

    public TabLayoutAdpater(Context context, FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
        this.mContext = context;
    }



    /**
     * @param position
     * @return
     */
    public View getTabView(int position) {
        //首先为子tab布置一个布局
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tableyout, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_text);
        ImageView iv = (ImageView) v.findViewById(R.id.tab_img);
        tv.setText(list_Title.get(position));
        iv.setImageResource(imageView[position]);

        if (position == 0) {
            tv.setSelected(true);
            iv.setSelected(true);
        } else {
            tv.setSelected(false);
            iv.setSelected(false);
        }
        return v;
    }

    public View setTabViewNoSelect(int position) {
        //首先为子tab布置一个布局
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tableyout, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_text);
        ImageView iv = (ImageView) v.findViewById(R.id.tab_img);
        tv.setText(list_Title.get(position));
        iv.setImageResource(imageView[position]);
        tv.setSelected(false);
        iv.setSelected(false);
        return v;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }
}



