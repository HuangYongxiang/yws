package com.example.wangchuang.yws.utils;
/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wangchuang.yws.R;


public class GlideImageLoaderImagePicker implements com.lzy.imagepicker.loader.ImageLoader {

    Activity activity;
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        this.activity = activity;
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(false)
                //.centerCrop()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        if(activity!=null){
            Glide.get(activity).clearMemory();
        }

    }
}