package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
//查找好友的结果类
@SuppressWarnings("unused")
public class GetUsersResult {

    private int code;
    @SerializedName("msg")
    private String message;

    private List<User> datas;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getDatas() {
        return datas;
    }
}
