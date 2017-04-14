package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons;

/**
 * Created by Administrator on 2017/4/14.
 * 检测字符串是否为空，为空则抛出错误信息
 */

public class Preconditions {

    public static void checkNonNull(Object object, String info){
        if (object == null){
            throw new RuntimeException("CheckNonNull fail: "+ info);
        }
    }
}
