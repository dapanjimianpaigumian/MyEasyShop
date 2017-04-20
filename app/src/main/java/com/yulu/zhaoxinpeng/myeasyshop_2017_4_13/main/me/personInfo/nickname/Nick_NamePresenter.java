package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo.nickname;

import android.util.Log;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.CachePreferences;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.UserResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */

public class Nick_NamePresenter extends MvpNullObjectBasePresenter<Nick_NameView>{

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call!=null) call.cancel();
    }

    public void changeNickName(User user){

        getView().showProgressbar();

        call= NetClient.getInstance().changeNickName(user);

        Log.e("user",user.getNick_name());

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
                    getView().showToast("修改昵称时发生未知错误");
                }else if(mUserResult.getCode()!=1){
                    getView().showToast(mUserResult.getMessage());
                }

                User mUser = mUserResult.getUser();

                getView().showToast(mUser.getNick_name());

                Log.e("Nick_name",mUser.getNick_name());

                getView().changenickname(mUser);
            }
        });
    }
}
