package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/18.
 * 登录的视图接口
 */

public interface LoginView extends MvpView{

    void showPrb();

    void hidePrb();

    void loginFailed();

    void loginSuccess();

    void showMsg(String msg);
}
