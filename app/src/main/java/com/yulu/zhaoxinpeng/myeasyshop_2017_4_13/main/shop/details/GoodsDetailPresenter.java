package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsDetail;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsDetailResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/22.
 * 商品详情页的业务类
 */

public class GoodsDetailPresenter extends MvpNullObjectBasePresenter<GoodsDetailView>{

    private Call call;
    private Call deleteCall;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(call!=null) call.cancel();
    }

    public void getGoodsDetail(String uuid){

        getView().showProgressbar();

        call= NetClient.getInstance().getGoodsDetail(uuid);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {

                getView().hideProgressbar();
                getView().showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {

                getView().hideProgressbar();

                GoodsDetailResult goodsDetailResult = new Gson().fromJson(body, GoodsDetailResult.class);

                if (goodsDetailResult.getCode()==1) {

                    //单个商品的所有信息
                    GoodsDetail goodsDetail = goodsDetailResult.getgoodsDetailDatas();

                    //用来存放单个商品的所有图片集合
                    ArrayList<String> mArrayList = new ArrayList<>();

                    for (int i = 0; i < goodsDetail.getPages().size(); i++) {
                        String uri = goodsDetail.getPages().get(i).getUri();
                        mArrayList.add(uri);
                    }

                    getView().setImageUrlData(mArrayList);
                    getView().setData(goodsDetail,goodsDetailResult.getUser());
                }else {
                    getView().showErro(body.toString());
                }


            }
        });
    }

    public void delete(String uuid){
        getView().showProgressbar();
        deleteCall=NetClient.getInstance().deleteGoods(uuid);
        deleteCall.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgressbar();
                getView().showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgressbar();
                GoodsDetailResult result=new Gson().fromJson(body,GoodsDetailResult.class);
                if (result.getCode()==1) {
                    getView().deleteGoods();
                    getView().showToast("删除成功");
                }else {
                    getView().showToast("删除失败");
                }
            }
        });
    }
}
