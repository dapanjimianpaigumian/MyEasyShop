package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.os.Handler;
import android.os.Looper;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.hyphenate.easeui.domain.EaseUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.CurrentUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.UserResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    // 环信相关
    private String hxPassword;
    private Call call;

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        EventBus.getDefault().register(this);
    }

    //解绑视图
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
        EventBus.getDefault().unregister(this);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void Login(final String username, final String password) {
        hxPassword = password;
        getView().showPrb();

        call = NetClient.getInstance().Login(username, password);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().hidePrb();
                        getView().showMsg(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {

                    private UserResult userResult;

                    @Override
                    public void run() {

                        try {
                            userResult = new Gson().fromJson(response.body().string(), UserResult.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (userResult.getCode() == 1) {

                            // 保存用户登录信息到本地配置
                            User mUser = userResult.getUser();
                            CachePreferences.getInstance().setUser(mUser);

                            //执行环信登录相关
                            EaseUser easeUser = CurrentUser.convert(mUser);
                            HxUserManager.getInstance().asyncLogin(easeUser, hxPassword);

                            getView().hidePrb();
                            getView().loginSuccess();
                            getView().showMsg("登录成功");
                        } else if (userResult.getCode() == 2) {
                            getView().hidePrb();
                            getView().showMsg(userResult.getMessage());
                            getView().loginFailed();
                        } else {
                            getView().hidePrb();
                            getView().showMsg("未知错误");
                        }
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        //判断是否登录成功
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        //调用登录成功的方法
        getView().loginSuccess();
        getView().showMsg("登录成功");

        EventBus.getDefault().post(new UserResult());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        //判断是否登录成功
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        getView().hidePrb();
        getView().showMsg(event.toString());
    }
}
