package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

/**
 * Created by Administrator on 2017/4/20.
 */

public class EasyShopApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CachePreferences.init(this);

        //-------------------ImageLoader初始化相关-----------------------------------------
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)//开启硬盘缓存
                .cacheInMemory(true)//开启内存缓存
                .resetViewBeforeLoading(true)//加载前重置ImageView
                .build();

        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(1024 * 1024 * 5)//设置内存缓存的大小，5M
                .defaultDisplayImageOptions(displayImageOptions)//默认加载选项
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }
}
