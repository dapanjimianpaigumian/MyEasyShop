package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18.
 * 对 OKHttp 中的 Callback 重新封装
 * 使得封装后的 Callback 内的方法能够更新主线程
 */

public abstract class UICallBack implements Callback {

    //获取主线程 Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {

        //判断是否响应成功
        if (!response.isSuccessful()) {
            throw new IOException("error code " + response.code());
        }

        final String json = response.body().string();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call, json);
            }
        });
    }

    public abstract void onFailureUI(Call call, IOException e);

    public abstract void onResponseUI(Call call, String body);
}
