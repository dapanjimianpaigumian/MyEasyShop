package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import android.os.Handler;
import android.os.Looper;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18.
 */

public class LoginPresenter {

    private LoginView mLoginView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public LoginPresenter(LoginView LoginView) {
        this.mLoginView = LoginView;
    }

    public void Login(String username, String password) {
        mLoginView.showProgressbar();

        NetClient.getInstance().Login(username, password).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.hideProgressbar();
                        mLoginView.showToast("登录失败: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.hideProgressbar();
                        mLoginView.showToast("登录成功: " + response.code());
                    }
                });
            }
        });
    }
}
