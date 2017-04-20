package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/20.
 */

public interface PersonView extends MvpView{

    void showProgressbar();

    void hideProgressbar();

    void showToast(String s);

    void updateAvatar(String url);
}
