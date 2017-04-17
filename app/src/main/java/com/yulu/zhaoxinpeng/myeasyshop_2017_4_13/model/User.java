package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/17.
 */

public class User {
/**
 "username": "xc62",
 "name": "yt59856b15cf394e7b84a7d48447d16098",   环信id
 "uuid": "0F8EC12223174657B2E842076D54C361",    环信uuid
 "password": "123456"
 */
    @SerializedName("username")
    private String name;
    @SerializedName("name")
    private String hx_Id;
    @SerializedName("uuid")
    private String table_Id;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHx_Id() {
        return hx_Id;
    }

    public void setHx_Id(String hx_Id) {
        this.hx_Id = hx_Id;
    }

    public String getTable_Id() {
        return table_Id;
    }

    public void setTable_Id(String table_Id) {
        this.table_Id = table_Id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
