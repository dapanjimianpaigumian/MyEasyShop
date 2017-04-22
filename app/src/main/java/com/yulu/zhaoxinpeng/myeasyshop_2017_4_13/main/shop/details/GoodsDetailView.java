package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsDetail;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/22.
 * 商品详情页的视图接口
 */

public interface GoodsDetailView extends MvpView{

    void showProgressbar();

    void hideProgressbar();

    //设置商品详情页内的图片链接集合
    void setImageUrlData(ArrayList<String> arrayList);

    //设置商品信息
    void setData(GoodsDetail goodsDetail, User user);

    //商品不存在
    void showErro(String s);

    void showToast(String s);

    //删除商品
    void deleteGoods();

}
