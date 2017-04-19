package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.UserResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;

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

    // TODO: 2017/4/19 0019 环信相关

    public Call call;

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void Registe(String username,String password){

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

                            getView().showMsg("注册成功");
                            // TODO: 2017/4/19 0019 用户信息保存到本地配置当中
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


}
