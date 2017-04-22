package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model;

import com.google.gson.annotations.SerializedName;

/**
 * 单个商品详情的（响应）实体类
 *
 * code": 1,
 "msg": " success",
 "datas"
 */

public class GoodsDetailResult {

    private int code;
    @SerializedName("msg")
    private String message;
    @SerializedName("datas")
    private GoodsDetail goodsDetailDatas;
    //发布者的信息
    private User user;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public GoodsDetail getgoodsDetailDatas() {
        return goodsDetailDatas;
    }

    public User getUser() {
        return user;
    }
}
