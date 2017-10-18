package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created on 16/12/2.
 */

public class ImageGridAdapter extends BaseAdapter {

    private List<String> data;
    private LayoutInflater mInflater;
    private Context mContext;
    private String type;

    public ImageGridAdapter(Context context, List<String> data, String type) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        this.type = type;
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_image, parent, false);
            holder.mImg = (ImageView) convertView.findViewById(R.id.item_img);
            holder.mGoVip = (TextView) convertView.findViewById(R.id.tv_go_vip);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(type.equals("0")){   //模糊
            holder.layout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load("")
                    .placeholder(R.drawable.pic_spxqc)
                    .error(R.drawable.pic_spxqc)
                    .crossFade(1000)
                    .bitmapTransform(new BlurTransformation(mContext, 1, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                    .into(holder.mImg);
        }else {
            String url = data.get(position);
            holder.layout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.pic_spxqc)
                    .crossFade()
                    .error(R.drawable.pic_spxqc)
                    .into(holder.mImg);
        }
        return convertView;
    }





    class ViewHolder {
        TextView mGoVip;
        ImageView mImg;
        LinearLayout layout;
    }
}
