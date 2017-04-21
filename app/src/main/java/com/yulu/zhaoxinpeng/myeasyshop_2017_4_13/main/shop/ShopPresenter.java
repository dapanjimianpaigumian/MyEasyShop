package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView>{

    private Call call;
    private int pageInt = 1;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call!=null) call.cancel();
    }

    public void refreshData(String type){

        getView().showRefresh();

        //因为刷新数据肯定是获取第一页数据，也就是说永远请求第一页
        call= NetClient.getInstance().getGoods(1,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {

                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {

                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);

                switch (goodsResult.getCode()) {
                    //成功获取数据
                    case 1:
                        //如果服务器没有数据时
                        if (goodsResult.getDatas().size()==0) {
                            getView().showRefreshEnd();
                        }else {
                            getView().addRefreshData(goodsResult.getDatas());
                            getView().showRefreshEnd();
                        }
                        //分页改为2，之后要加载更多数据了
                        pageInt = 2;
                        break;
                    default:
                        //失败或其他
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }

    public void loadData(String type){
        getView().showLoadMoreLoading();
        call=NetClient.getInstance().getGoods(pageInt,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {

                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);

                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getDatas().size()==0) {
                            getView().showLoadMoreEnd();
                        }else {

                            getView().addMoreData(goodsResult.getDatas());
                            getView().showLoadMoreEnd();
                        }

                        //采取分页加载方法，之后加载新的一页数据
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }
}
