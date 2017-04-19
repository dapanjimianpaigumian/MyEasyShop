package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.User.Registe;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface RegisteView extends MvpView{

    //显示进度条
    void showPrb();

    //隐藏进度条
    void hidePrb();

    //注册成功
    void registerSuccess();

    //注册失败
    void registerFailed();

    //提示信息
    void showMsg(String msg);

}
