package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;

/**
 * Created by Administrator on 2017/4/20.
 */

public class AvatarLoadOptions {

    public AvatarLoadOptions() {
    }

    private static DisplayImageOptions options;

    //用户头像加载选项
    public static DisplayImageOptions build(){
        if (options==null) {
            options=new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.user_ico)/*如果空url显示什么*/
                    .showImageOnLoading(R.drawable.user_ico)/*加载中显示什么*/
                    .showImageOnFail(R.drawable.ic_launcher)/*加载失败显示什么*/
                    .cacheOnDisk(true)/*开启硬盘缓存*/
                    .cacheInMemory(true)/*开启内存缓存*/
                    .resetViewBeforeLoading(true)/*加载前重置ImageView*/
                    .build();
        }
        return options;
    }

    private static DisplayImageOptions options_item;

    //图片加载选项
    public static DisplayImageOptions build_item(){
        if (options_item==null) {
            options_item=new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.image_error)
                    .showImageOnLoading(R.drawable.image_loding)
                    .showImageOnFail(R.drawable.image_error)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .build();
        }
        return options_item;
    }
}
