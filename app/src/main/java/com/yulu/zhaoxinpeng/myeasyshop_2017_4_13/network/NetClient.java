package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network;

import com.google.gson.Gson;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsUpLoad;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private final Gson mGson;

    public NetClient() {

        mGson = new Gson();

        //设置日志拦截器
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();

        //设置日志拦截器的拦截级别
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mHttpLoggingInterceptor)
                .build();

    }

    public static synchronized NetClient getInstance() {
        if (mNetClient == null) {
            mNetClient = new NetClient();
        }
        return mNetClient;
    }

    //登录
    public Call Login(String username, String password) {
        RequestBody mRequestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.LOGIN)
                .post(mRequestBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //注册
    public Call Registe(String username, String password) {
        RequestBody mRequestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.REGISTER)
                .post(mRequestBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //修改头像
    public Call uploadAvatar(File file) {
        MultipartBody mMultipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", mGson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.UPDATA)
                .post(mMultipartBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //修改昵称
    public Call changeNickName(User user) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", mGson.toJson(user))
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.UPDATA)
                .post(multipartBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //获取所有商品的请求
    public Call getGoods(int pageNo, String type) {
        FormBody mFormBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("type", type)
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.GETGOODS)
                .post(mFormBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //获取单个商品详情的请求
    public Call getGoodsDetail(String uuid) {
        RequestBody mRequestBody = new FormBody.Builder()
                .add("uuid", uuid)
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.DETAIL)
                .post(mRequestBody)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }

    //获取个人商品请求
    public Call getPersonGoodsData(int pageNO, String type, String master) {

        RequestBody mRequestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNO))
                .add("master", master)
                .add("type", type)
                .build();
        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.GETGOODS)
                .post(mRequestBody)
                .build();
        return mOkHttpClient.newCall(mRequest);
    }

    //我的商品页面内的删除请求
    public Call deleteGoods(String uuid) {
        RequestBody mRequestBody = new FormBody.Builder()
                .add("uuid", uuid)
                .build();

        Request mRequest = new Request.Builder()
                .url(NetApi.BASE_URL + NetApi.DELETE)
                .post(mRequestBody)
                .build();
        return mOkHttpClient.newCall(mRequest);
    }

    //我的商品界面内的上传商品请求
    public Call uploadGoods(GoodsUpLoad goodsUpLoad, ArrayList<File> files) {

        MultipartBody.Builder mBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("good", mGson.toJson(goodsUpLoad));

        //向请求体中加入多张图片
        for (File file : files) {
            mBuilder.addFormDataPart("image", file.getName(),
                    RequestBody.create(MediaType.parse("image/png"), file));
        }

        MultipartBody multipartBody = mBuilder.build();

        Request mRequest = new Request.Builder()
                .post(multipartBody)
                .url(NetApi.BASE_URL + NetApi.UPLOADGOODS)
                .build();

        return mOkHttpClient.newCall(mRequest);
    }
}
