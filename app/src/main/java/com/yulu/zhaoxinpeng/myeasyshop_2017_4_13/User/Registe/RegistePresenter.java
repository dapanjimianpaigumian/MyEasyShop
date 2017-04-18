package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

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

public class RegistePresenter {

    private RegisteView mRegisteView;

    private Handler mHandler=new Handler(Looper.getMainLooper());

    public RegistePresenter(RegisteView RegisteView) {
        this.mRegisteView = RegisteView;
    }

    public void Registe(String username,String password){

        mRegisteView.showProgressbar();

        NetClient.getInstance().Registe(username, password).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRegisteView.hideProgressbar();
                        mRegisteView.showToast("注册失败："+e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRegisteView.hideProgressbar();
                        mRegisteView.showToast("注册成功："+response.code());
                    }
                });
            }
        });
    }


}
