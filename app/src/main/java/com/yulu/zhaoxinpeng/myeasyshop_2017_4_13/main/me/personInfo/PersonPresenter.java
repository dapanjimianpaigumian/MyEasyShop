package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.UserResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PersonPresenter extends MvpNullObjectBasePresenter<PersonView> {

    private Call call;

    //解绑视图
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call!=null) call.cancel();
    }

    //上传头像
    public void updateAvatar(File file){

        getView().showProgressbar();
        call= NetClient.getInstance().uploadAvatar(file);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgressbar();
                getView().showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgressbar();
                UserResult mUserResult = new Gson().fromJson(body, UserResult.class);

                if (mUserResult==null) {
                    getView().showToast("未知错误");
                }else if(mUserResult.getCode()!=1){
                    getView().showToast(mUserResult.getMessage());
                }

                User mUser = mUserResult.getUser();
                CachePreferences.setUser(mUser);

                //上传成功，触发UI操作（更新头像）
                getView().updateAvatar(mUserResult.getUser().getHead_Image());

                // TODO: 2017/4/20 0020 环信更新头像
            }
        });
    }
}
