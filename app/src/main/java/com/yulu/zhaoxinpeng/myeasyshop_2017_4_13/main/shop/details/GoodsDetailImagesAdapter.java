package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main.shop.details;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/22.
 */

public class GoodsDetailImagesAdapter extends PagerAdapter {

    private ArrayList<ImageView> mList;

    public GoodsDetailImagesAdapter(ArrayList<ImageView> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //实例化item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView mImageView = mList.get(position);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick();
            }
        });

        container.addView(mImageView);

        return mImageView;
    }

    //销毁item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //自定义点击监听接口
    public interface OnItemClickListener {
        void onItemClick();
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
