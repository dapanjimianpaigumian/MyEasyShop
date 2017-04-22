package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取商品列表时返回的实体类
 *
 * "code": 1,
 "msg": " success",
 "datas"
 */

public class GoodsResult {

    private int code;
    @SerializedName("msg")
    private String message;
    private List<GoodsInfo> datas;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<GoodsInfo> getDatas() {
        return datas;
    }
}
