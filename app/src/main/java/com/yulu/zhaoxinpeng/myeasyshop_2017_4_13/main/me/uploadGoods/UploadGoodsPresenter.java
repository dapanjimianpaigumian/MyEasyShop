package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.me.uploadGoods;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.MyFileUtils;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsUpLoad;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GoodsUpLoadResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.ImageItem;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.UICallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/25.
 * 删除商品的业务类
 */

public class UploadGoodsPresenter extends MvpNullObjectBasePresenter<UploadGoodsView> {

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(call!=null) call.cancel();
    }

    private ArrayList<File> getFiles(List<ImageItem> list){
        ArrayList<File> files=new ArrayList<>();
        for (ImageItem imageItem :
                list) {
            //根据图片路径，拿到图片文件
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }

    public void uploadgoods(GoodsUpLoad goodsUpLoad, List<ImageItem> itemList){
        getView().showProgressbar();
        call= NetClient.getInstance().uploadGoods(goodsUpLoad,getFiles(itemList));
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgressbar();
                getView().ShowToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgressbar();
                GoodsUpLoadResult goodsUpLoadResult = new Gson().fromJson(body, GoodsUpLoadResult.class);
                getView().ShowToast(goodsUpLoadResult.getMessage());

                //上传成功
                if (goodsUpLoadResult.getCode()==1) {
                    getView().uploadSuccess();
                }
            }
        });
    }
}
