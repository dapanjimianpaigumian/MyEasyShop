package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

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

public class RegistePresenter extends MvpNullObjectBasePresenter<RegisteView>{

    private Handler mHandler=new Handler(Looper.getMainLooper());

    //业务，执行网络请求，完成注册
    //在特定的地方，触发对应UI操作

    //  环信相关
    private String hxPassword;
    public Call call;

    @Override
    public void attachView(RegisteView view) {
        super.attachView(view);
        EventBus.getDefault().register(this);
    }

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
        EventBus.getDefault().unregister(this);
    }

    public void Registe(String username,String password){
        hxPassword=password;
        getView().showPrb();

        Call call = NetClient.getInstance().Registe(username, password);
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

                        getView().hidePrb();

                        try {
                            userResult = new Gson().fromJson(response.body().string(), UserResult.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (userResult.getCode()==1) {

                            User user = userResult.getUser();
                            CachePreferences.setUser(user);

                            //登录环信相关操作（会通过EventBus返回值）
                            EaseUser easeUser = CurrentUser.convert(user);
                            HxUserManager.getInstance().asyncLogin(easeUser,hxPassword);

                            getView().showMsg("注册成功");
                            getView().registerSuccess();
                        }else if(userResult.getCode()==2){

                            getView().showMsg(userResult.getMessage());

                            getView().registerFailed();
                        }else {
                            getView().showMsg("未知错误！");
                        }
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        //调用注册成功的方法
        getView().registerSuccess();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        getView().hidePrb();
        getView().showMsg(event.toString());
    }

}
