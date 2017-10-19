package com.example.wangchuang.yws.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.base.BaseActivity;
import com.example.wangchuang.yws.bean.BeanResult;
import com.example.wangchuang.yws.bean.CommentAllModel;
import com.example.wangchuang.yws.content.Constants;
import com.example.wangchuang.yws.content.JsonGenericsSerializator;
import com.example.wangchuang.yws.content.ValueStorage;
import com.example.wangchuang.yws.utils.CashierInputFilter;
import com.example.wangchuang.yws.utils.StringUtil;
import com.example.wangchuang.yws.utils.ToastUtil;
import com.example.wangchuang.yws.utils.eventbus.EventCenter;
import com.example.wangchuang.yws.utils.netstatus.NetUtils;
import com.example.wangchuang.yws.view.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class PublishActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mIvBack;

    private EditText mTitleEt,mContentEt,mMoneyEt;
    private TextView mOptionTv;
    private Button publishBtn;
    private NoScrollGridView gridView;
    private int MAX_IMGS = 9;
    private GridAdapter mAdapter;
    private String blankImg = "addImg";
    private ArrayList<String> imgUrl = new ArrayList<>();
    public static final String ARG_ITEM_INFO = "item_info";
    public static final String ARG_ITEM_INDEX = "item_index";
    @Override
    public int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mOptionTv = (TextView) findViewById(R.id.tv_option);
        mTitleEt = (EditText) findViewById(R.id.title_et);
        mContentEt = (EditText) findViewById(R.id.content_et);
        mMoneyEt = (EditText) findViewById(R.id.money_et);
        publishBtn = (Button) findViewById(R.id.btn_publish);
        gridView = (NoScrollGridView) findViewById(R.id.gv_gridview);
        InputFilter[] filters = {new CashierInputFilter()};
        mMoneyEt.setFilters(filters);
        imgUrl.add(blankImg);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GridAdapter(mContext);
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == imgUrl.size() - 1 && imgUrl.size() < MAX_IMGS) {
                    int canUploadImgSize = MAX_IMGS - imgUrl.size();
                    if (imgUrl.contains(blankImg)) {
                        canUploadImgSize += 1;
                    }
                    goSelectGallery(canUploadImgSize);
                } else if (arg2 == MAX_IMGS - 1 && imgUrl.contains(blankImg)) {//第九个是选择图片图标
                    goSelectGallery(1);
                } else {
                    Bundle args = new Bundle();
                    args.putStringArrayList(ARG_ITEM_INFO, imgUrl);
                    args.putInt(ARG_ITEM_INDEX, arg2);
                    //   readyGo(PhotoViewActivity.class, args);
                }
            }
        });
        publishBtn.setOnClickListener(this);
        mOptionTv.setOnClickListener(this);
    }

    private void publish() {
        HashMap<String,File> files = new HashMap<>();
        if(imgUrl.size()<MAX_IMGS){
            imgUrl.remove(blankImg);
        }
        for (int i = 0;i<imgUrl.size();i++){
            File file = new File(imgUrl.get(i));
            files.put(i+"",file);
        }
        String url = Constants.BaseUrl + Constants.publishUrl;
        Map<String, String> params = new HashMap<>();
        params.put("token", ValueStorage.getString("token") + "");
        params.put("title",mTitleEt.getText().toString()+"");
        params.put("price",mMoneyEt.getText().toString()+"");
        params.put("content",mContentEt.getText().toString()+"");
        //showLoadingDialog("请求中....");
        showLoadingDialog("上传中....");
        OkHttpUtils.post()//
                .params(params)//
                .files("imgs[]",files)
                .url(url)//
                .build()//
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(PublishActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<CommentAllModel>>(){}.getType();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        if (response.status.equals("400")) {
                            dismissLoadingDialog();
                            ToastUtil.show(PublishActivity.this, response.msg);
                        }
                    }
                });
    }

    private void goSelectGallery(int num) {
        ImagePicker.getInstance().setSelectLimit(num);
        if (num == 1) {
            ImagePicker.getInstance().setMultiMode(false);
        } else {
            ImagePicker.getInstance().setMultiMode(true);
        }
        Intent intent = new Intent(PublishActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, Constants.IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == Constants.IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(images!=null&&images.size()>0){
                    imgUrl.remove(blankImg);
                    for (ImageItem bean : images) {
                        imgUrl.add(bean.path);
                    }
                    if (imgUrl.size() < MAX_IMGS) {
                        imgUrl.add(blankImg);
                    }
//                for (String bean : imgUrl) {
//                    LogUtil.d("bean:" + bean);
//                }
                    mAdapter.notifyDataSetChanged();
                }
            } else {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_option:
                startActivity(new Intent(PublishActivity.this,PublishMsgActivity.class));
                break;
            case R.id.btn_publish:
                if (StringUtil.isEmpty(mTitleEt.getText().toString())){
                    ToastUtil.show(PublishActivity.this,"标题不能为空");
                }else if (StringUtil.isEmpty(mContentEt.getText().toString())){
                    ToastUtil.show(PublishActivity.this,"内容不能为空");
                }else if (StringUtil.isEmpty(mMoneyEt.getText().toString())){
                    ToastUtil.show(PublishActivity.this,"价格不能为空");
                }else {
                    publish();
                }
                break;
        }
    }


    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }


        public int getCount() {
            return (imgUrl.size());
            //  return 3;
        }

        public Object getItem(int arg0) {
            return imgUrl.get(arg0);
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
            Glide.with(PublishActivity.this).load(R.drawable.icon_tjtp).crossFade().skipMemoryCache(false).into(holder.image);
            holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.imgClose.setVisibility(View.GONE);
            if (imgUrl.get(position).equals(blankImg)) {
//                holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
                Glide.with(PublishActivity.this).load(R.drawable.icon_tjtp).crossFade().skipMemoryCache(false).into(holder.image);
                holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.imgClose.setVisibility(View.GONE);
            } else {
                Glide.with(PublishActivity.this).load(imgUrl.get(position)).centerCrop().crossFade().error(R.drawable.icon_tjtp).skipMemoryCache(false).into(holder.image);
                holder.imgClose.setVisibility(View.VISIBLE);

            }
            holder.imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgUrl.remove(position);
                    if (!imgUrl.contains(blankImg)) {
                        imgUrl.add(blankImg);
                    }
                    notifyDataSetChanged();
                }
            });
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

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
