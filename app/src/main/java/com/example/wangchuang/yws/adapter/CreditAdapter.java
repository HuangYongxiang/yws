package com.example.wangchuang.yws.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;
import com.example.wangchuang.yws.bean.CreditModel;
import com.example.wangchuang.yws.bean.GoodsModel;
import com.example.wangchuang.yws.view.CircularImage;
import com.example.wangchuang.yws.view.NoScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {
    public ArrayList<CreditModel> datas = null;
    Context context;
    private GridAdapter mAdapter;
    public CreditAdapter(Context context, ArrayList<CreditModel> datas) {
        this.context = context;
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_credit_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CreditModel model = datas.get(position);

        viewHolder.tv_content.setText(model.getContent());
        ArrayList<String> imgUrl = model.getOss_imgs();
        mAdapter = new GridAdapter(imgUrl,context,position);
        viewHolder.gv_gridview.setAdapter(mAdapter);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }




    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;
        private NoScrollGridView gv_gridview;



        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            gv_gridview = (NoScrollGridView) itemView.findViewById(R.id.gv_gridview);

        }
    }


    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private int impPosition;// 选中item的位置
        private boolean shape;
        private ArrayList<String> imgeUrl;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(ArrayList<String> imgUrl, Context context, int position) {
            inflater = LayoutInflater.from(context);
            this.imgeUrl = imgUrl;
            this.impPosition = position;
        }


        public int getCount() {

            return (imgeUrl.size());
        }

        public Object getItem(int arg0) {
            return imgeUrl.get(arg0);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grid, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.imgClose = (ImageView) convertView.findViewById(R.id.img_close);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setBackground(context.getDrawable(R.drawable.shape_line_1px));
            Glide.with(context).load(imgeUrl.get(position)).centerCrop().crossFade().error(R.drawable.pic_mh_1).skipMemoryCache(false).into(holder.image);
            holder.imgClose.setVisibility(View.GONE);



            // 根据屏幕宽度动态设置图片宽高
//            int width = MeasureUtils.getWidth(UiUtils.getContext());
//            int imageWidth = (mScreenWidth / 3 - 40);
//            holder.image.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView imgClose;
        }
    }
}
