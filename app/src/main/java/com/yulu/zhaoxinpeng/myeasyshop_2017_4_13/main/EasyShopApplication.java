package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.app.Application;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

/**
 * Created by Administrator on 2017/4/20.
 */

public class EasyShopApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CachePreferences.init(this);
    }
}
