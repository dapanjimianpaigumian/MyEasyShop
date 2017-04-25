package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.uploadGoods;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface UploadGoodsView extends MvpView {

    void showProgressbar();

    void hideProgressbar();

    void uploadSuccess();

    void ShowToast(String s);
}
