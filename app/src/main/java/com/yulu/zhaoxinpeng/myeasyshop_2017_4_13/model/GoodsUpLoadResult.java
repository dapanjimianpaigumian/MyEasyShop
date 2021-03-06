package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model;

import com.google.gson.annotations.SerializedName;

/**
 * 上传商品时,网络请求返回的实体
 */
@SuppressWarnings("unused")
public class GoodsUpLoadResult {

    private int code;
    @SerializedName("msg")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
