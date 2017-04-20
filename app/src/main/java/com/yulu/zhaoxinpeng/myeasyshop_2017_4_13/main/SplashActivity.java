package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivityUtils = new ActivityUtils(this);

        // TODO: 2017/4/14 0014 环信登录相关（账号冲突踢出）

        // 判断一下用户是不是已经登录过了
        if (CachePreferences.getUser().getName() != null) {

            mActivityUtils.startActivity(MainActivity.class);
            finish();
        } else {

            //如果未登录，那么1s后跳转到主页
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mActivityUtils.startActivity(MainActivity.class);
                    finish();
                }
            }, 1000);
        }


    }
}
