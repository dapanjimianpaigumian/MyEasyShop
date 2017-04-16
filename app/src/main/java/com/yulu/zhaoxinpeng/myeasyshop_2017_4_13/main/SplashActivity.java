package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivityUtils = new ActivityUtils(this);

        // TODO: 2017/4/14 0014 判断用户是否登录，自动登录
        // TODO: 2017/4/14 0014 环信登录相关（账号冲突踢出）

        //1.5s跳转到主页
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivityUtils.startActivity(MainActivity.class);
                finish();
            }
        },1000);
    }
}
