package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.hyphenate.easeui.domain.EaseUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.CurrentUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import static com.baidu.location.b.g.t;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivityUtils = new ActivityUtils(this);

        EventBus.getDefault().register(this);

        //环信登录相关（账号冲突踢出）
        if (getIntent().getBooleanExtra("AUTO_LOGIN",false)) {
            //清除本地缓存的用户信息
            CachePreferences.clearAllData();
            //踢出时，退出环信
            HxUserManager.getInstance().asyncLogout();
        }

        // 判断一下用户是不是已经登录过了
        if (CachePreferences.getUser().getName() != null
                && !HxUserManager.getInstance().isLogin()) {

            User user = CachePreferences.getUser();
            EaseUser easeUser = CurrentUser.convert(user);
            HxUserManager.getInstance().asyncLogin(easeUser,user.getPassword());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event){
        //判断是否登录成功
        if(event.type!= HxEventType.LOGIN) return;
        mActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event){
        //判断是否是登录失败事件
        if(event.type!=HxEventType.LOGIN) return;
        throw new RuntimeException("login error");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
