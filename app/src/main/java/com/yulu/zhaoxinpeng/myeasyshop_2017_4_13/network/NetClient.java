package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/4/17.
 * 网络请求模块
 */

public class NetClient {

    private static NetClient mNetClient;
    private final OkHttpClient mOkHttpClient;

    public NetClient() {

        //设置日志拦截器
        HttpLoggingInterceptor mHttpLoggingInterceptor=new HttpLoggingInterceptor();

        //设置日志拦截器的拦截级别
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mHttpLoggingInterceptor)
                .build();
    }

    public static NetClient getInstance(){
        if (mNetClient==null) {
            mNetClient = new NetClient();
        }
        return mNetClient;
    }

    public Call Login(String username,String password){
        RequestBody mRequestBody=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        Request mRequest=new  Request.Builder()
                .url(NetApi.BASE_URL+NetApi.LOGIN)
                .post(mRequestBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    public Call Registe(String username,String password){
        RequestBody mRequestBody=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        Request mRequest=new  Request.Builder()
                .url(NetApi.BASE_URL+NetApi.REGISTER)
                .post(mRequestBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }


}
