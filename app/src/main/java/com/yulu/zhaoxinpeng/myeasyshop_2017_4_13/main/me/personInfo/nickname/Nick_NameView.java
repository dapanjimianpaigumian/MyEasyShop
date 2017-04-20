package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.personInfo.nickname;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface Nick_NameView extends MvpView{

    void showProgressbar();

    void hideProgressbar();

    void showToast(String s);

    void changenickname(User user);
}
