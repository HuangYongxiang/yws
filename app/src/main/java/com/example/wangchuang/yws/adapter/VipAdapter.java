package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.bean.Geren;
import com.example.wangchuang.yws.bean.Price;
import com.example.wangchuang.yws.content.ViewHolder;

import java.util.List;


public class VipAdapter extends BaseAdapter {

    private Context context;
    private List<Price> lists;
    private LayoutInflater inflater;

    public VipAdapter(Context context, List<Price> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vip_item, null);
        }
        String a=lists.get(position).explains.split("月")[0];
        String b=lists.get(position).explains.split("月")[1];
        String c=b.split("元")[0];
        TextView text1 = ViewHolder.get(convertView,R.id.text1);
        TextView text2 = ViewHolder.get(convertView,R.id.text2);
        TextView text3 = ViewHolder.get(convertView,R.id.text3);
        text1.setText(a+"月");
        text2.setText(c);

        return convertView;
    }
}
