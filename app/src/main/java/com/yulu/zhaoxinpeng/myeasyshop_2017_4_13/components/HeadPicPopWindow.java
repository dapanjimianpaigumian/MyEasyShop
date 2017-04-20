package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.components;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/20.
 * 自定义PopupWindow,用于图片的选择
 */

public class HeadPicPopWindow extends PopupWindow {

    public interface Listener {
        void toGallery();

        void toCamera();
    }

    private Activity mActivity;

    private Listener mListener;

    @SuppressWarnings("all")
    public HeadPicPopWindow(Activity activity, Listener listener) {
        super(activity.getLayoutInflater().inflate(R.layout.layout_popu, null),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this,getContentView());
        this.mActivity=activity;
        this.mListener=listener;

        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void show(){
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    @OnClick({R.id.btn_popu_album, R.id.btn_popu_camera, R.id.btn_popu_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_popu_album:
                mListener.toGallery();
                break;
            case R.id.btn_popu_camera:
                mListener.toCamera();
                break;
            case R.id.btn_popu_cancel:
                break;
        }
        dismiss();
    }
}
