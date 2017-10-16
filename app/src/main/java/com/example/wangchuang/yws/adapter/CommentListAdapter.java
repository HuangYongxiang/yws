package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.bean.CommentAllModel;
import com.example.wangchuang.yws.bean.CommentModel;
import com.example.wangchuang.yws.bean.UserInfo;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.NoScrollListView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class CommentListAdapter extends BaseAdapter {
    private List<CommentAllModel> list;
    private Context context;


    public CommentListAdapter(List<CommentAllModel> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_comment_list, null);
            holder = new ViewHolder();
            holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.iv_vip = (ImageView) convertView.findViewById(R.id.iv_vip);
            holder.iv_header = (CircularImage) convertView.findViewById(R.id.iv_header);
            holder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.comment_list = (NoScrollListView) convertView.findViewById(R.id.comment_list);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommentAllModel model = list.get(position);
        Glide.with(context).load(model.getUser_info().getOss_head_img())
                .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(holder.iv_header);
        if(model.getUser_info().getSex().equals("1")){
            holder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nan));
        }else {
            holder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nv));
        }
        if(model.getUser_info().getType().equals("1")){
            holder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            holder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip2));
        }
        holder.nameTv.setText(model.getUser_info().getUser_name());
        holder.timeTv.setText(model.getInterval_time());
        holder.contentTv.setText(model.getContent());

        ArrayList<CommentModel> list = model.getTwo_comment();
        TwoCommentListAdapter adapter = new TwoCommentListAdapter(list,context);
        holder.comment_list.setAdapter(adapter);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentClick(position,model.getUser_info());
                }
            }
        });
        return convertView;
    }
    private OnCommentClickListener mOnCommentClickListener;
    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }
    public static interface OnCommentClickListener {
        // true add; false cancel
        public void onCommentClick(int position,UserInfo user); //传递boolean类型数据给activity
    }

    static class ViewHolder {
        CircularImage iv_header;
        ImageView iv_sex,iv_vip;
        TextView contentTv,nameTv,timeTv;
        NoScrollListView comment_list;
        RelativeLayout layout;
    }
}