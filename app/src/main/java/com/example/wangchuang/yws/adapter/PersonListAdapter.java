package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.PersonModel;
import com.example.wangchuang.yws.view.CircularImage;

import java.util.ArrayList;


public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {
    public ArrayList<PersonModel> datas = null;
    Context context;

    public PersonListAdapter(Context context, ArrayList<PersonModel> datas) {
        this.context = context;
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final PersonModel model = datas.get(position);

        Glide.with(context).load(model.getOss_head_img())
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(viewHolder.iv_header);

        viewHolder.tv_name.setText(model.getUser_name());
        if(model.getSex().equals("1")){
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nan));
        }else {
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nv));
        }
        if(model.getType().equals("1")){
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip2));
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }




    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImage iv_header;
        private ImageView iv_sex;
        private ImageView iv_vip;
        public TextView tv_name;




        public ViewHolder(View itemView) {
            super(itemView);
            iv_header = (CircularImage) itemView.findViewById(R.id.iv_header);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            iv_vip = (ImageView) itemView.findViewById(R.id.iv_vip);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);


        }
    }
}
