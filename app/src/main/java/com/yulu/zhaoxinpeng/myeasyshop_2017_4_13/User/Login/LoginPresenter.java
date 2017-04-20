package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.UserResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    // TODO: 2017/4/19 0019 环信相关

    private Call call;

    //解绑视图
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call!=null) call.cancel();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void Login(final String username, final String password) {
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

                        if (userResult.getCode()==1) {

                            // 保存用户登录信息到本地配置
                            User mUser = userResult.getUser();

                            CachePreferences.getInstance().setUser(mUser);

                            Log.e("user",mUser+mUser.getName()+mUser.getPassword()+"");

                            getView().hidePrb();
                            getView().loginSuccess();
                            getView().showMsg("登录成功");
                        }else if (userResult.getCode() == 2){
                            getView().hidePrb();
                            getView().showMsg(userResult.getMessage());
                            getView().loginFailed();
                        }else{
                            getView().hidePrb();
                            getView().showMsg("未知错误");
                        }
                    }
                });
            }
        });
    }
}
