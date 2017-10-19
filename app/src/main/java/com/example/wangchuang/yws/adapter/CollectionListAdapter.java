package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.bean.LikeGoodsModel;
import com.example.wangchuang.yws.view.CircularImage;

import java.util.ArrayList;


public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {
    public ArrayList<LikeGoodsModel> datas = null;
    Context context;

    public CollectionListAdapter(Context context, ArrayList<LikeGoodsModel> datas) {
        this.context = context;
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collection_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final LikeGoodsModel model = datas.get(position);

        Glide.with(context).load(model.getUser_info().getOss_head_img())
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(viewHolder.iv_header);
        ArrayList<String> url=  model.getArticle_content().getOss_imgs();

        if(url.size() == 1){
            viewHolder.iv_goods2.setVisibility(View.GONE);
            viewHolder.iv_goods3.setVisibility(View.GONE);
            Glide.with(context).load(url.get(0)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods1.setImageBitmap(resource);
                        }
                    });
           /* Glide.with(context).load(url[0])
                    .centerCrop()
                    .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(viewHolder.iv_goods1);*/
        }else if(url.size() == 2){
            viewHolder.iv_goods2.setVisibility(View.VISIBLE);
            viewHolder.iv_goods3.setVisibility(View.GONE);
            Glide.with(context).load(url.get(0)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods1.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(url.get(1)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods2.setImageBitmap(resource);
                        }
                    });

        }else {
            viewHolder.iv_goods2.setVisibility(View.VISIBLE);
            viewHolder.iv_goods3.setVisibility(View.VISIBLE);
            Glide.with(context).load(url.get(0)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods1.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(url.get(1)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods2.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(url.get(2)).asBitmap().placeholder(R.drawable.pic_spxqc).error(R.drawable.pic_spxqc).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            viewHolder.iv_goods3.setImageBitmap(resource);
                        }
                    });

        }
        viewHolder.tv_name.setText(model.getUser_info().getUser_name());
        viewHolder.tv_money.setText(model.getArticle_content().getPrice());
        viewHolder.tv_describe.setText(model.getArticle_content().getContent());
        viewHolder.tv_num.setText("留言" + model.getArticle_content().getComment_nums() + "   浏览" + model.getArticle_content().getNums());
        viewHolder.tv_time.setText(model.getArticle_content().getCreat_time());
        if(model.getUser_info().getSex().equals("1")){
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nan));
        }else {
            viewHolder.iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nv));
        }
        if(model.getUser_info().getType().equals("1")){
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip1));
        }else {
            viewHolder.iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip2));
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",model.getArticle_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.onCancelClick(position,model.getArticle_id());
                }
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }


    private OnCancelClickListener mOnCancelClickListener;
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }
    public static interface OnCancelClickListener {
        // true add; false cancel
        public void onCancelClick(int position,String id); //传递boolean类型数据给activity
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImage iv_header;
        public ImageView iv_goods1;
        public ImageView iv_goods2;
        public ImageView iv_goods3;
        public ImageView iv_sex;
        public ImageView iv_vip;
        public TextView tv_name;
        public TextView tv_money;
        public TextView tv_describe;
        public TextView cancel_tv;
        public TextView tv_num;
        public TextView tv_time;
        public LinearLayout layout;



        public ViewHolder(View itemView) {
            super(itemView);
            iv_header = (CircularImage) itemView.findViewById(R.id.iv_header);
            iv_goods1 = (ImageView) itemView.findViewById(R.id.iv_goods1);
            iv_goods2 = (ImageView) itemView.findViewById(R.id.iv_goods2);
            iv_goods3 = (ImageView) itemView.findViewById(R.id.iv_goods3);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            iv_vip = (ImageView) itemView.findViewById(R.id.iv_vip);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            cancel_tv = (TextView) itemView.findViewById(R.id.cancel_tv);
            tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

        }
    }
}
