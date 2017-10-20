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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;
import com.example.wangchuang.yws.activity.OtherPeopleActivity;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_person, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final PersonModel model = datas.get(position);

        Glide.with(context).load(model.getUser_info().getOss_head_img())
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(viewHolder.iv_header);

        viewHolder.tv_name.setText(model.getUser_info().getUser_name());
        if(model.getUser_info().getSex()!=null&&model.getUser_info().getSex().equals("1")){
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nan));
        }else {
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nv));
        }
        if(model.getUser_info().getType()!=null&&model.getUser_info().getType().equals("1")){
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip2));
        }
        if(model.getUser_info().getPeople_type()!=null&&model.getUser_info().getType().equals("1")){
            viewHolder.iv_people_type.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_smrzf));

        }else {
            viewHolder.iv_people_type.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_smrz));
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, OtherPeopleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid",model.getUser_info().getUid());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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
        private ImageView iv_people_type;
        public TextView tv_name;
        public RelativeLayout layout;




        public ViewHolder(View itemView) {
            super(itemView);
            iv_header = (CircularImage) itemView.findViewById(R.id.iv_header);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            iv_people_type = (ImageView) itemView.findViewById(R.id.iv_people_type);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);


        }
    }
}
