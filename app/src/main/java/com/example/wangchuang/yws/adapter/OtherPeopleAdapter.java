package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.wangchuang.yws.view.CircularImage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;



public class OtherPeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<GoodsModel> datas = null;
    Context context;
    ArrayList<String> images;
    String key;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        TITLE,
        ITEM
    }
    public OtherPeopleAdapter(Context context, ArrayList<GoodsModel> datas, ArrayList<String> images, String key) {
        this.context = context;
        this.datas = datas;
        this.images = images;
        this.key = key;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == ITEM_TYPE.TITLE.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.items_people_title, viewGroup, false);
            TitleVideoHolder holder = new TitleVideoHolder(view);
            return holder ;
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_goods_list, viewGroup, false);
            ItemViewHolder viewHolder = new ItemViewHolder(itemView);

            return viewHolder ;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof TitleVideoHolder){
           // ((TitleVideoHolder) viewHolder).banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

        }else {
            final GoodsModel model = datas.get(position-1);

            Glide.with(context).load(model.getUser_head_img())
                    .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(((ItemViewHolder) viewHolder).iv_header);
            String[] url=  model.getOss_imgs();
            if(url.length == 1){
                ((ItemViewHolder) viewHolder).iv_goods2.setVisibility(View.GONE);
                ((ItemViewHolder) viewHolder).iv_goods3.setVisibility(View.GONE);
                Glide.with(context).load(url[0])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods1);
            }else if(url.length == 2){
                ((ItemViewHolder) viewHolder).iv_goods2.setVisibility(View.VISIBLE);
                ((ItemViewHolder) viewHolder).iv_goods3.setVisibility(View.GONE);
                Glide.with(context).load(url[0])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods1);
                Glide.with(context).load(url[1])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods2);
            }else {
                ((ItemViewHolder) viewHolder).iv_goods2.setVisibility(View.VISIBLE);
                ((ItemViewHolder) viewHolder).iv_goods3.setVisibility(View.VISIBLE);
                Glide.with(context).load(url[0])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods1);
                Glide.with(context).load(url[1])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods2);
                Glide.with(context).load(url[2])
                        .placeholder(R.drawable.pic_spxqc).crossFade().error(R.drawable.pic_spxqc).into(((ItemViewHolder) viewHolder).iv_goods3);
            }
            ((ItemViewHolder) viewHolder).tv_name.setText(model.getUser_name());
            ((ItemViewHolder) viewHolder).tv_money.setText(model.getPrice());
            ((ItemViewHolder) viewHolder).tv_describe.setText(model.getContent());
            ((ItemViewHolder) viewHolder).tv_num.setText("留言" + model.getComment_num() + "   浏览" + model.getNums());
            ((ItemViewHolder) viewHolder).tv_time.setText(model.getCreat_time());
            if(model.getSex().equals("1")){
                ((ItemViewHolder) viewHolder).iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nan));
            }else {
                ((ItemViewHolder) viewHolder).iv_sex.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nv));
            }
            if(model.getVip_type().equals("1")){
                ((ItemViewHolder) viewHolder).iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip1));
            }else {
                ((ItemViewHolder) viewHolder).iv_vip.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_vip2));
            }
            ((ItemViewHolder) viewHolder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",model.getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }




    public int getItemViewType(int position) {

        return position  == 0 ? ITEM_TYPE.TITLE.ordinal() : ITEM_TYPE.ITEM.ordinal();
    }



    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size() +1;
    }

    public class TitleVideoHolder extends RecyclerView.ViewHolder {
        //自定义的ViewHolder，持有每个Item的的所有界面元素

        public TextView mTv_type2;
        public TitleVideoHolder(View itemView) {
            super(itemView);
        }

    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public CircularImage iv_header;
        public ImageView iv_goods1;
        public ImageView iv_goods2;
        public ImageView iv_goods3;
        public ImageView iv_sex;
        public ImageView iv_vip;
        public TextView tv_name;
        public TextView tv_money;
        public TextView tv_describe;
        public TextView tv_num;
        public TextView tv_time;
        public LinearLayout layout;

        public ItemViewHolder(View view) {
            super(view);
            iv_header = (CircularImage) itemView.findViewById(R.id.iv_header);
            iv_goods1 = (ImageView) itemView.findViewById(R.id.iv_goods1);
            iv_goods2 = (ImageView) itemView.findViewById(R.id.iv_goods2);
            iv_goods3 = (ImageView) itemView.findViewById(R.id.iv_goods3);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            iv_vip = (ImageView) itemView.findViewById(R.id.iv_vip);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
